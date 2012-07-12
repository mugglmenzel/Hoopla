package de.eorganization.hoopla.shared.model.ahp.values;

import java.io.Serializable;

import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;

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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/de.eorganization.hoopla.shared.model/model/ahp/values/AlternativeEvaluation.java $
 *
 */

public class AlternativeEvaluation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7866386142825158814L;

	private Criterion criterion;
	
	private Alternative alternative;
	
	private Double value;

	/**
	 * @param criterion
	 * @param alternative
	 * @param value
	 */
	public AlternativeEvaluation(Criterion criterion, Alternative alternative,
			Double value) {
		super();
		this.criterion = criterion;
		this.alternative = alternative;
		this.value = value;
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * @param criterion the criterion to set
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * @return the alternative
	 */
	public Alternative getAlternative() {
		return alternative;
	}

	/**
	 * @param alternative the alternative to set
	 */
	public void setAlternative(Alternative alternative) {
		this.alternative = alternative;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAlternative().toString() + "/" + getCriterion().toString() + ":=" + value.toString();
	}
	
	
	
}
