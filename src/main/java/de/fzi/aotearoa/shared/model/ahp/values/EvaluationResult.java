/**
 * 
 */
package de.fzi.aotearoa.shared.model.ahp.values;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;

/**
 * @author mugglmenzel
 * 
 */
public class EvaluationResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7851506834319269271L;

	private Decision decision;

	private Map<Alternative, Double> resultMultiplicativeIndexMap = new HashMap<Alternative, Double>();

	private Map<Alternative, Double> resultAdditiveIndexMap = new HashMap<Alternative, Double>();

	private Map<Alternative, Double> resultPositiveGoalsMap = new HashMap<Alternative, Double>();

	private Map<Alternative, Double> resultNegativeGoalsMap = new HashMap<Alternative, Double>();

	/**
	 * 
	 */
	public EvaluationResult() {
		super();
	}

	/**
	 * @param decision
	 * @param resultMap
	 */
	public EvaluationResult(Decision decision,
			Map<Alternative, Double> resultMultiplicativeIndexMap,
			Map<Alternative, Double> resultAdditiveIndexMap,
			Map<Alternative, Double> resultPositiveGoalsMap,
			Map<Alternative, Double> resultNegativeGoalsMap) {
		super();
		this.decision = decision;
		this.resultMultiplicativeIndexMap = resultMultiplicativeIndexMap;
		this.resultAdditiveIndexMap = resultAdditiveIndexMap;
		this.resultPositiveGoalsMap = resultPositiveGoalsMap;
		this.resultNegativeGoalsMap = resultNegativeGoalsMap;
	}

	/**
	 * @return the decision
	 */
	public Decision getDecision() {
		return decision;
	}

	/**
	 * @param decision
	 *            the decision to set
	 */
	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	/**
	 * @return the resultMultiplicativeIndexMap
	 */
	public Map<Alternative, Double> getResultMultiplicativeIndexMap() {
		return resultMultiplicativeIndexMap;
	}

	/**
	 * @return the resultAdditiveIndexMap
	 */
	public Map<Alternative, Double> getResultAdditiveIndexMap() {
		return resultAdditiveIndexMap;
	}

	/**
	 * @return the resultPositiveGoalsMap
	 */
	public Map<Alternative, Double> getResultPositiveGoalsMap() {
		return resultPositiveGoalsMap;
	}

	/**
	 * @return the resultNegativeGoalsMap
	 */
	public Map<Alternative, Double> getResultNegativeGoalsMap() {
		return resultNegativeGoalsMap;
	}

	/**
	 * @param resultMultiplicativeIndexMap the resultMultiplicativeIndexMap to set
	 */
	public void setResultMultiplicativeIndexMap(
			Map<Alternative, Double> resultMultiplicativeIndexMap) {
		this.resultMultiplicativeIndexMap = resultMultiplicativeIndexMap;
	}

	/**
	 * @param resultAdditiveIndexMap the resultAdditiveIndexMap to set
	 */
	public void setResultAdditiveIndexMap(
			Map<Alternative, Double> resultAdditiveIndexMap) {
		this.resultAdditiveIndexMap = resultAdditiveIndexMap;
	}

	/**
	 * @param resultPositiveGoalsMap the resultPositiveGoalsMap to set
	 */
	public void setResultPositiveGoalsMap(
			Map<Alternative, Double> resultPositiveGoalsMap) {
		this.resultPositiveGoalsMap = resultPositiveGoalsMap;
	}

	/**
	 * @param resultNegativeGoalsMap the resultNegativeGoalsMap to set
	 */
	public void setResultNegativeGoalsMap(
			Map<Alternative, Double> resultNegativeGoalsMap) {
		this.resultNegativeGoalsMap = resultNegativeGoalsMap;
	}


}
