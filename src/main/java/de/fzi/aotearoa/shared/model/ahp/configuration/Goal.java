package de.fzi.aotearoa.shared.model.ahp.configuration;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.fzi.aotearoa.shared.model.ahp.values.AlternativeImportance;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeValue;
import de.fzi.aotearoa.shared.model.ahp.values.CriterionImportance;

/**
 * 
 * @author mugglmenzel While a Decision can be made under consideration of multiple
 *         Goals, the fitness of an Alternative regarding one Goal is determined
 *         by evaluating an Alternative under consideration of many aspects, the
 *         Criteria.
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 220 $ 
 *         
 *           Date: $Date: 2011-09-16 18:58:00 +0200 (Fr, 16 Sep 2011) $
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
 *         SVN URL: 
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/configuration/Goal.java $
 *
 */

@PersistenceCapable(detachable = "true", table = "Goal")
// @Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
public class Goal extends Criterion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1654628294916029944L;

	@Persistent(defaultFetchGroup = "true")
	private Decision decision;

	@Persistent
	private GoalType goalType = GoalType.POSITIVE;
	
	
	public Goal(Criterion c) {
		this.setDescription(c.getDescription());
		this.setType(c.getType());
		this.setWeight(c.getWeight());
		for (Criterion ch : c.getChildren())
			this.addChild(ch.clone());
		for (CriterionImportance ci : c.getImportanceChildren())
			this.getImportanceChildren().add(ci.clone());
		for (AlternativeImportance ai : c.getImportanceAlternatives())
			this.getImportanceAlternatives().add(ai.clone());
		for (AlternativeValue av : c.getValuesAlternatives())
			this.getValuesAlternatives().add(av.clone());
	}
	
	
	/**
	 * @param decision
	 *            the decision to set
	 */
	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	/**
	 * @return the decision
	 */
	public Decision getDecision() {
		return decision;
	}

	public Goal() {
		super();
	};

	public Goal(String name) {
		super(name);
	}
	
	public boolean hasDecision() {
		return getDecision() != null;
	}

	public Goal clone() {
		Goal g = new Goal(getName());
		g.setDescription(getDescription());
		g.setType(getType());
		g.setGoalType(getGoalType());
		g.setWeight(getWeight());
		for (Criterion ch : getChildren())
			g.addChild(ch.clone());
		for (CriterionImportance ci : getImportanceChildren())
			g.getImportanceChildren().add(ci.clone());
		for (AlternativeImportance ai : getImportanceAlternatives())
			g.getImportanceAlternatives().add(ai.clone());
		for (AlternativeValue av : getValuesAlternatives())
			g.getValuesAlternatives().add(av.clone());

		return g;
	}

	/**
	 * @param goalType the goalType to set
	 */
	public void setGoalType(GoalType goalType) {
		this.goalType = goalType;
	}

	/**
	 * @return the goalType
	 */
	public GoalType getGoalType() {
		return goalType;
	}

}
