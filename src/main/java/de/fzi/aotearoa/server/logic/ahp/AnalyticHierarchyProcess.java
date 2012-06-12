package de.fzi.aotearoa.server.logic.ahp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.CriterionType;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;
import de.fzi.aotearoa.shared.model.ahp.configuration.GoalType;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeEvaluation;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeValuesMatrix;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeWeightsMatrix;
import de.fzi.aotearoa.shared.model.ahp.values.CriterionImportance;
import de.fzi.aotearoa.shared.model.ahp.values.CriterionWeightsMatrix;
import de.fzi.aotearoa.shared.model.ahp.values.Evaluation;
import de.fzi.aotearoa.shared.model.ahp.values.EvaluationResult;
import de.fzi.aotearoa.shared.model.ahp.values.GoalImportance;
import de.fzi.aotearoa.shared.model.ahp.values.GoalWeightsMatrix;
import de.fzi.aotearoa.shared.model.jama.Matrix;

/**
 * 
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel@gmail.com $
 * 
 *         Revision: $Revision: 277 $
 * 
 *         Date: $Date: 2012-06-06 15:20:30 +0200 (Mi, 06 Jun 2012) $
 * 
 *         License:
 * 
 *         Copyright 2011 Forschungszentrum Informatik FZI / Karlsruhe Institute
 *         of Technology
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 * 
 *         SVN URL: $HeadURL:
 *         https://aotearoadecisions.googlecode.com/svn/trunk/
 *         src/main/java/de/fzi
 *         /aotearoa/server/logic/ahp/AnalyticHierarchyProcess.java $
 * 
 */

public class AnalyticHierarchyProcess {

	private final int DEFAULT_PRECISION = 5;

	private Decision decision;

	private Set<AlternativeEvaluation> alternativeEvaluations = new HashSet<AlternativeEvaluation>();

	private Logger log = Logger.getLogger(AnalyticHierarchyProcess.class
			.getName());

	/**
	 * @return the alternativeEvaluations
	 */
	private Set<AlternativeEvaluation> getAlternativeEvaluations() {
		return alternativeEvaluations;
	}

	public AnalyticHierarchyProcess(Decision decision) {
		super();
		this.decision = decision;
		log.info("created new AHP with decision: " + decision);
	}

	public EvaluationResult evaluate(List<Evaluation> evaluation)
			throws Exception {
		return this.evaluate(evaluation, DEFAULT_PRECISION);
	}

	// TODO: neuen Exceptiontyp einf¸hren!
	public EvaluationResult evaluate(List<Evaluation> evals, int precision)
			throws Exception {

		log.info("---------- new evaluation ----------");

		if (!sanityCheck(evals))
			throw new Exception("Given decision model not complete.");

		Map<Alternative, Double> alternativeMultiplicativeMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativeAdditiveMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativePositiveMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativeNegativeMap = new HashMap<Alternative, Double>();

		
		// Filter Alternatives
		
		
		// GoalWeights
		GoalWeightsMatrix critM = new GoalWeightsMatrix(decision.getGoals()
				.size(), new HashSet<GoalImportance>(
				decision.getImportanceGoals()));
		Vector<Double> goalsWeights = calculateEigenvector(critM.getMatrix(),
				precision);
		if (goalsWeights.size() < decision.getGoals().size())
			for (int i = goalsWeights.size() - 1; i < decision.getGoals()
					.size(); i++)
				goalsWeights.add(0D);
		for (int i = 0; i < decision.getGoals().size(); i++)
			decision.getGoals().get(i).setWeight(goalsWeights.get(i));
		log.info("goals weights: " + goalsWeights);

		int g = 0;
		for (Goal goal : decision.getGoals()) {

			log.info("processing goal " + goal.getName());

			// ChildrenWeights
			setChildrenCriteriaWeights(goal);
			for (Criterion c : goal.getAllDescendants())
				setChildrenCriteriaWeights(c);

			List<Criterion> leafCriteria = goal.getLeafCriteria();

			setAlternativeEvaluations(new HashSet<AlternativeEvaluation>());

			Evaluation evaluation = evals != null && evals.size() > g ? evals
					.get(g) : null;

			if (evaluation == null) {
				evaluation = new Evaluation();
				for (Criterion leaf : leafCriteria) {
					if (CriterionType.QUALITATIVE.equals(leaf.getType())) {
						AlternativeWeightsMatrix m = new AlternativeWeightsMatrix(
								decision.getAlternatives().size(),
										leaf.getImportanceAlternatives(), leaf);
						evaluation.getEvaluations().add(m.getMatrix());
						log.info("created matrix for " + leaf.getName());
						log.info(m.getMatrix().toString());
					} else {
						AlternativeValuesMatrix m = new AlternativeValuesMatrix(
								decision.getAlternatives().size(),
								leaf.getValuesAlternatives(), leaf);
						evaluation.getEvaluations().add(m.getMatrix());
						log.info("created matrix for " + leaf.getName());
						log.info(m.getMatrix().toString());
					}

				}
			} else {
				// TODO: check if evaluation is correct/complete - sanity
				log.info("no health check for manual evaluations, yet");
			}

			int i = 0;
			for (Matrix m : evaluation.getEvaluations()) {
				// System.out.print("Matrix to Eigen for "
				// + getDecision().getAlternatives().size()
				// + " alternatives, criterion " + leafCriteria.get(i) + ":");
				// m.print(2, 2);
				if (m != null) {
					Vector<Double> normalized = calculateEigenvector(m,
							precision);
					log.info("normalization for criterion "
							+ leafCriteria.get(i) + ": "
							+ normalized.toString());

					for (int j = 0; j < normalized.size(); j++)
						getAlternativeEvaluations().add(
								new AlternativeEvaluation(leafCriteria.get(i),
										getDecision().getAlternatives().get(j),
										normalized.get(j)));
				}
				i++;
			}

			log.info("evaluations for goal " + g + ": "
					+ getAlternativeEvaluations());

			Map<Alternative, Double> alternativeGoalMap = new HashMap<Alternative, Double>();

			for (AlternativeEvaluation ce : getAlternativeEvaluations()) {
				alternativeGoalMap
						.put(ce.getAlternative(),
								new Double(
										(alternativeGoalMap.get(ce
												.getAlternative()) != null ? alternativeGoalMap
												.get(ce.getAlternative())
												.doubleValue() : 0D)
												+ (ce.getValue() * ce
														.getCriterion()
														.getGlobalWeight())));
			}
			log.info("goalMap: " + alternativeGoalMap);
			for (Alternative a : alternativeGoalMap.keySet()) {
				log.info("Aggregating for Alternative " + a);
				if (goal.getGoalType().equals(GoalType.POSITIVE))
					alternativePositiveMap
							.put(a,
									(alternativePositiveMap.get(a) != null ? alternativePositiveMap
											.get(a).doubleValue() : 0D)
											+ alternativeGoalMap.get(a));
				else
					alternativeNegativeMap
							.put(a,
									(alternativeNegativeMap.get(a) != null ? alternativeNegativeMap
											.get(a).doubleValue() : 0D)
											+ alternativeGoalMap.get(a));
			}
			g++;
		}

		log.info("Positive Map " + alternativePositiveMap);
		log.info("Negative Map " + alternativeNegativeMap);

		for (Alternative a : decision.getAlternatives()) {
			log.info("Generating Indices for Alternative " + a);
			alternativeMultiplicativeMap
					.put(a,
							(alternativePositiveMap.containsKey(a) ? alternativePositiveMap
									.get(a) : 1D)
									/ (alternativeNegativeMap.containsKey(a) ? alternativeNegativeMap
											.get(a) : 1D));

			alternativeAdditiveMap
					.put(a,
							(alternativePositiveMap.containsKey(a) ? alternativePositiveMap
									.get(a) : 0D)
									- (alternativeNegativeMap.containsKey(a) ? alternativeNegativeMap
											.get(a) : 0D));
		}

		log.info("---------- end evaluation ----------");

		return new EvaluationResult(decision, alternativeMultiplicativeMap,
				alternativeAdditiveMap, alternativePositiveMap,
				alternativeNegativeMap);
	}

	/**
	 * @param alternativeEvaluations
	 *            the alternativeEvaluations to set
	 */
	private void setAlternativeEvaluations(
			Set<AlternativeEvaluation> alternativeEvaluations) {
		this.alternativeEvaluations = alternativeEvaluations;
	}

	private boolean sanityCheck(List<Evaluation> evals) {
		if (evals == null || !(evals.size() > 0))
			return true;
		for (Evaluation eval : evals)
			if (!sanityCheck(eval))
				return false;
		return true;
	}

	// TODO: Sanity-Check implementieren
	private boolean sanityCheck(Evaluation eval) {
		boolean result = getDecision() != null;
		result &= getDecision().getAlternatives().size() > 0;
		result &= getDecision().getGoals().size() > 0;
		/*
		 * result &= eval.getEvaluations().size() ==
		 * getDecision().getGoals().get(0).getLeafCriteria().size(); for (Matrix
		 * m : eval.getEvaluations()) { result &= m.getColumnDimension() ==
		 * m.getRowDimension(); result &= m.getColumnDimension() ==
		 * getDecision().getAlternatives() .size(); }
		 */
		return result;
	}

	public void setChildrenCriteriaWeights(Criterion parent) {
		setChildrenCriteriaWeights(parent, DEFAULT_PRECISION);
	}

	public void setChildrenCriteriaWeights(Criterion parent, int precision) {
		if (parent.hasChildren()) {
			CriterionWeightsMatrix critM = new CriterionWeightsMatrix(parent
					.getChildren().size(), new HashSet<CriterionImportance>(
					parent.getImportanceChildren()));

			setChildrenCriteriaWeights(parent, critM.getMatrix(), precision);
		}
	}

	public void setChildrenCriteriaWeights(Criterion parent, Matrix m) {
		this.setChildrenCriteriaWeights(parent, m, DEFAULT_PRECISION);
	}

	public void setChildrenCriteriaWeights(Criterion parent, Matrix m,
			int precision) {
		Vector<Double> criteriaWeights = calculateEigenvector(m, precision);

		if (criteriaWeights.size() > 0
				&& criteriaWeights.size() >= parent.getChildren().size()) {
			Iterator<Criterion> itiLeaf = parent.getChildren().iterator();
			int i = 0;
			while (itiLeaf.hasNext()) {
				Criterion leafCriterion = itiLeaf.next();
				leafCriterion.setWeight(criteriaWeights.get(i));
				log.info(leafCriterion.getName() + ": local weight = "
						+ leafCriterion.getWeight() + ", global weight = "
						+ leafCriterion.getGlobalWeight());
				i++;
			}
		}
	}

	private Vector<Double> calculateEigenvector(Matrix m, int preciseness) {

		// System.out.print("m: " );
		// m.print(3, 2);

		// Variablen zur späteren Berechnung des normalisierten Eigenvektors
		Matrix normalisierterEigenVektorAlt = new Matrix(m.getRowDimension(), 1);
		for (int i = 0; i < m.getRowDimension(); i++) {
			normalisierterEigenVektorAlt.set(i, 0, 0.0);
		}
		Matrix normalisierterEigenVektor = null;

		// die Matrix wird so oft quadriert bis nur noch eine geringe
		// Abweichungen
		// beim Eigenvektor auftreten (maximal 1000 mal)
		for (int a = 0; a < 1000; a++) {

			// Quadratur der Matrix
			m = m.times(m);
			// System.out.print("m²: " );
			// m.print(3, 2);

			// Variablen zur Berechnung des Eigenvektors
			int columnDimension = m.getColumnDimension();
			int rowDimension = m.getRowDimension();
			double reihenGesamtSumme = 0.0;
			Matrix eigenVektor = new Matrix(rowDimension, 1);
			normalisierterEigenVektor = new Matrix(rowDimension, 1);

			// berechnet den Eigenvektor
			for (int i = 0; i < rowDimension; i++) {
				double reihenSumme = 0.0;
				for (int j = 0; j < columnDimension; j++) {
					reihenSumme += m.get(i, j);
				}
				eigenVektor.set(i, 0, reihenSumme);
				reihenGesamtSumme += reihenSumme;
			}

			// normalisiert den Eigenvektor
			for (int i = 0; i < eigenVektor.getRowDimension(); i++) {
				double value = eigenVektor.get(i, 0) / reihenGesamtSumme;
				normalisierterEigenVektor.set(i, 0, value);
			}
			// System.out.print("eigenvektor: ");
			// eigenVektor.print(3, 2);
			// System.out.print(", normalisiert: ");
			// normalisierterEigenVektor.print(3, 2);

			// Differenz zwischen neuen und altem Eigenvektor
			Matrix eigenVektorDifferenz = normalisierterEigenVektor
					.minus(normalisierterEigenVektorAlt);

			// falls ein Wert der Eigenvektordifferenz kleiner als 0.0001 ist
			// wird die Schleife beendet
			boolean isFinished = true;
			for (int i = 0; i < rowDimension; i++) {
				if (Math.abs(eigenVektorDifferenz.get(i, 0)) >= Math.pow(10, -1
						* preciseness)) {
					isFinished = false;
				}
			}
			if (isFinished) {
				break;
			}

			normalisierterEigenVektorAlt = normalisierterEigenVektor;
		}

		Vector<Double> eigenVektor = new Vector<Double>();
		for (int i = 0; i < normalisierterEigenVektor.getRowDimension(); i++) {
			eigenVektor.add(normalisierterEigenVektor.get(i, 0));
		}

		return eigenVektor;

	}

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	// TODO: wegen nicht möglicher JAMA lib Nutzung erstmal auskommentiert
	// public void calculateLocalCriteriaWeight(CriteriaMatrix criteriaMatrix) {
	//
	// Vector<Double> normalizedEigenVector =
	// calculateEigenvector(criteriaMatrix.getMatrix());
	// List<Criterion> criteriaList = criteriaMatrix.getCriteriaList();
	// for (int i = 0; i < criteriaList.size(); i++) {
	// criteriaList.get(i).setLocalWeight(normalizedEigenVector.get(i));
	// }
	//
	// }

}
