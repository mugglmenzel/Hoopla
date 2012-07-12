/**
 * 
 */
package de.eorganization.hoopla.shared.model.ahp.values;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;

/**
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 241 $ 
 *         
 *           Date: $Date: 2011-09-24 15:39:41 +0200 (Sa, 24 Sep 2011) $
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/de.eorganization.hoopla.shared.model/model/ahp/values/CriterionImportance.java $
 *
 */
@PersistenceCapable(detachable = "true")
// @Inheritance(customStrategy = "complete-table")
public class CriterionImportance implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8980041770341071607L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	protected String id;

	@Persistent(defaultFetchGroup = "true")
	private Criterion parent;

	@Persistent
	private int critA;

	@Persistent
	private int critB;

	@Persistent
	private Double comparisonAToB;
	
	@Persistent
	private String comment;

	/**
	 * 
	 */
	public CriterionImportance() {
		super();
	}

	/**
	 * @param critA
	 * @param critB
	 * @param comparisonAToB
	 */
	public CriterionImportance(int critA, int critB, Double comparisonAToB, String comment) {
		super();
		this.critA = critA;
		this.critB = critB;
		this.comparisonAToB = comparisonAToB;
		this.comment = comment;
	}

	public CriterionImportance clone() {
		return new CriterionImportance(getCritA(), getCritB(),
				getComparisonAToB(), getComment());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CriterionImportance other = (CriterionImportance) obj;
		if (critA != other.critA) {
			return false;
		}
		if (critB != other.critB) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#getComment()
	 */
	public String getComment() {
		return comment;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#getComparisonAToB()
	 */
	public Double getComparisonAToB() {
		return comparisonAToB;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#getCritA()
	 */
	public int getCritA() {
		return critA;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#getCritB()
	 */
	public int getCritB() {
		return critB;
	}


	/**
	 * @return the parent
	 */
	public Criterion getParent() {
		return parent;
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
		result = prime * result + critA;
		result = prime * result + critB;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#setComment(java.lang.String)
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#setComparisonAToB(java.lang.Double)
	 */
	public void setComparisonAToB(Double comparisonAToB) {
		this.comparisonAToB = comparisonAToB;
	}


	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#setCritA(int)
	 */
	public void setCritA(int critA) {
		this.critA = critA;
	}

	/* (non-Javadoc)
	 * @see de.fzi.aotearoa.shared.model.ahp.values.Importance#setCritB(int)
	 */
	public void setCritB(int critB) {
		this.critB = critB;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Criterion parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CriterionImportance [comparisonAToB=" + comparisonAToB
				+ ", critA=" + critA + ", critB=" + critB + "]";
	}
}
