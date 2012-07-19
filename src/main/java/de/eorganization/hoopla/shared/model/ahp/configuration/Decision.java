package de.eorganization.hoopla.shared.model.ahp.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import de.eorganization.hoopla.shared.model.Member;
import de.eorganization.hoopla.shared.model.ahp.configuration.requirement.Requirement;
import de.eorganization.hoopla.shared.model.ahp.values.GoalImportance;

/**
 * @author mugglmenzel A Decision is the main class of the data model. It
 *         represents a decision itself and encapsulates multiple Goals that are
 *         pursued and multiple Alternatives that are potential solutions.
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
 *         /aotearoa/de.eorganization.hoopla.shared.model/model
 *         /ahp/configuration/Decision.java $
 * 
 */

@PersistenceCapable(detachable = "true", table = "Decision")
@Inheritance(customStrategy = "complete-table")
public class Decision implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4411517735777225339L;

	private static Logger log = Logger.getLogger(Decision.class.getName());

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	protected String id;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.pk-id", value = "true")
	private Long keyId;

	@Persistent
	@Embedded
	protected Member member;

	@Persistent
	protected String name;

	@Persistent
	protected String description;

	@Persistent
	protected String comment;

	@Persistent(mappedBy = "decision", dependentElement = "true", defaultFetchGroup = "true")
	protected List<Alternative> alternatives = new ArrayList<Alternative>();

	@Persistent(mappedBy = "decision", dependentElement = "true", defaultFetchGroup = "true")
	protected List<Goal> goals = new ArrayList<Goal>();

	@Persistent(mappedBy = "decision", dependentElement = "true", defaultFetchGroup = "true")
	protected List<Requirement<?>> requirements = new ArrayList<Requirement<?>>();

	@Persistent(mappedBy = "decision", dependentElement = "true", defaultFetchGroup = "true")
	protected List<GoalImportance> importanceGoals = new ArrayList<GoalImportance>();

	public Decision() {
		super();
	}

	public Decision(String decisionName) {
		super();
		this.name = decisionName;
	}

	public void addAlternative(Alternative alternative) {
		getAlternatives().add(alternative);
	}

	public void addGoal(Goal goal) {
		getGoals().add(goal);
	}

	public int countGoals() {
		return getGoals().size();
	}

	public Alternative getAlternative(String alternativeName) {
		for (Alternative a : getAlternatives())
			if (a.getName().equals(alternativeName))
				return a;
		return null;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public Criterion getCriterion(String criterionName) {
		if (getGoal(criterionName) != null)
			return getGoal(criterionName);
		for (Goal goal : getGoals()) {
			Criterion a = getCriterion(goal, criterionName);
			if (a != null)
				return a;
		}
		return null;
	}

	public Criterion getCriterion(String goalName, String criterionName) {
		if (goalName.equals(criterionName) && getGoal(criterionName) != null)
			return getGoal(criterionName);
		return getCriterion(getGoal(goalName), criterionName);
	}

	public Criterion getCriterion(Criterion goal, String criterionName) {
		if (goal.getName().equals(criterionName))
			return goal;
		return goal.getCriterion(criterionName);
	}

	public Criterion getParentCriterion(String criterionName) {
		if (getGoal(criterionName) != null)
			return null;
		for (Goal goal : getGoals()) {
			Criterion a = getParentCriterion(goal, criterionName);
			if (a != null)
				return a;
		}
		return null;
	}

	public Criterion getParentCriterion(String goalName, String criterionName) {
		if (goalName.equals(criterionName))
			return null;
		return getParentCriterion(getGoal(goalName), criterionName);
	}

	public Criterion getParentCriterion(Criterion goal, String criterionName) {
		if (goal.getName().equals(criterionName))
			return null;
		return goal.getParentCriterion(criterionName);
	}

	public List<List<Criterion>> getCriteriaByLevels() {
		List<List<Criterion>> result = new ArrayList<List<Criterion>>();
		result.add(new ArrayList<Criterion>(getGoals()));
		for (Goal g : getGoals())
			result.addAll(g.getCriteriaByLevels());

		return result;
	}

	public List<Criterion> getLeafCriteria() {
		List<Criterion> result = new ArrayList<Criterion>();
		for (Goal g : getGoals())
			result.addAll(g.getLeafCriteria());

		return result;
	}

	public List<Criterion> getLeafCriteria(CriterionType type) {
		List<Criterion> result = new ArrayList<Criterion>();
		for (Goal g : getGoals())
			result.addAll(g.getLeafCriteria(type));

		return result;
	}

	public String getDescription() {
		return description;
	}

	// add Wang
	public String getComment() {
		return comment;
	}

	public Goal getGoal(String name) {
		Iterator<Goal> iti = getGoals().iterator();
		Goal help = null;
		while (iti.hasNext()) {
			help = iti.next();
			if (help.getName().equals(name))
				return help;
		}
		return null;
	}

	public String deleteGoal(String goalName) {
		Iterator<Goal> iti = this.getGoals().iterator();
		while (iti.hasNext()) {
			if (goalName.equals(iti.next().getName())) {
				iti.remove();
				return "removed";
			}
		}
		return null;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public String getName() {
		return name;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// add Wang
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	public void setName(String name) {
		this.name = name;
	};

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + ", " + getAlternatives() + ", " + getGoals() + ", "
				+ getImportanceGoals();
	}

	/**
	 * @param keyId
	 *            the keyId to set
	 */
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return the keyId
	 */
	public Long getKeyId() {
		return keyId;
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @param member
	 *            the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Decision clone() {
		Decision dec = new Decision(getName());
		dec.setDescription(getDescription());
		dec.setMember(getMember());
		// add Wang
		dec.setComment(getComment());

		for (Alternative a : getAlternatives())
			dec.addAlternative(a.clone());
		for (Goal g : getGoals())
			dec.addGoal(g.clone());

		return dec;
	}

	/**
	 * @return the importanceGoals
	 */
	public List<GoalImportance> getImportanceGoals() {
		return importanceGoals;
	}

	/**
	 * @param importanceGoals
	 *            the importanceGoals to set
	 */
	public void setImportanceGoals(List<GoalImportance> importanceGoals) {
		this.importanceGoals = importanceGoals;
	}

	public GoalImportance getImportanceGoal(int i, int j) {
		log.fine("goal importance set: " + getImportanceGoals());
		GoalImportance test = new GoalImportance(i, j, null, null);
		test.setDecision(this);
		if (!getImportanceGoals().contains(test))
			return null;
		for (GoalImportance gi : getImportanceGoals())
			if (test.equals(gi))
				return gi;
		return null;
	}

	public void insertImportanceGoal(GoalImportance gi) {
		if (getImportanceGoals().contains(gi)) {
			for (GoalImportance goali : getImportanceGoals())
				if (goali.equals(gi)) {
					goali.setComparisonAToB(gi.getComparisonAToB());
					goali.setComment(gi.getComment());
				}

		} else
			getImportanceGoals().add(gi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Decision))
			return false;
		Decision other = (Decision) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		return true;
	}

	/**
	 * @return the requirements
	 */
	public List<Requirement<?>> getRequirements() {
		return requirements;
	}

	/**
	 * @param requirements
	 *            the requirements to set
	 */
	public void setRequirements(List<Requirement<?>> requirements) {
		this.requirements = requirements;
	}

}
