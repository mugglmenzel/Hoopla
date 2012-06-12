package de.fzi.aotearoa.shared.model.ahp.values;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;

/**
 * 
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/values/AlternativeImportance.java $
 *
 */

@PersistenceCapable(detachable = "true")
// @Inheritance(customStrategy = "complete-table")
public class AlternativeImportance implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8084248328316108208L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent(defaultFetchGroup = "true")
	private Criterion criterion;

	@Persistent
	private int altA;

	@Persistent
	private int altB;

	@Persistent
	private Double comparisonAToB;
	
	@Persistent
	private String description;

	/**
	 * 
	 */
	public AlternativeImportance() {
		super();
	}

	/**
	 * 
	 * @param altA
	 * @param altB
	 * @param comparisonAToB
	 */
	public AlternativeImportance(int altA, int altB, Criterion c, Double comparisonAToB, String description) {
		super();
		this.altA = altA;
		this.altB = altB;
		this.criterion = c;
		this.comparisonAToB = comparisonAToB;
		this.description = description;
	}

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

	/**
	 * 
	 * @return the Alternative A
	 */
	public int getAltA() {
		return altA;
	}

	/**
	 * 
	 * @param altA
	 *            the Alternative A to set
	 */
	public void setAltA(int altA) {
		this.altA = altA;
	}

	/**
	 * 
	 * @return the Alternative B
	 */
	public int getAltB() {
		return altB;
	}

	/**
	 * 
	 * @param altB
	 *            the Alternative B to set
	 */
	public void setAltB(int altB) {
		this.altB = altB;
	}

	/**
	 * 
	 * @return the comparison value between alternative A and B
	 */
	public Double getComparisonAToB() {
		return comparisonAToB;
	}

	/**
	 * 
	 * @param comparisonAToB
	 *            the comparison value between alternative A and B to set
	 */
	public void setComparisonAToB(Double comparisonAToB) {
		this.comparisonAToB = comparisonAToB;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param criterion
	 *            the criterion to set
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	public AlternativeImportance clone() {
		return new AlternativeImportance(getAltA(), getAltB(), getCriterion(),
				getComparisonAToB(), getDescription());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + altA;
		result = prime * result + altB;
		result = prime * result
				+ ((criterion == null) ? 0 : criterion.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlternativeImportance other = (AlternativeImportance) obj;
		if (altA != other.altA)
			return false;
		if (altB != other.altB)
			return false;
		if (criterion == null) {
			if (other.criterion != null)
				return false;
		} else if (!criterion.equals(other.criterion))
			return false;
		return true;
	}

}
