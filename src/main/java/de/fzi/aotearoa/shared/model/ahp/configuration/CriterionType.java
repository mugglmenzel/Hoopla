/**
 * 
 */
package de.fzi.aotearoa.shared.model.ahp.configuration;

import java.io.Serializable;

/**
 * @author menzel
 *
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 253 $ 
 *         
 *           Date: $Date: 2011-10-04 11:20:29 +0200 (Di, 04 Okt 2011) $
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/configuration/CriterionType.java $
 * 
 */

public enum CriterionType implements Serializable {

	QUANTITATIVE("quantitative"), QUALITATIVE("qualitative"), BENCHMARK("benchmark");
	
	String typeName;
	
	private CriterionType(String type) {
		setTypeName(type);
	}

	/**
	 * @return the type
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param type the type to set
	 */
	private void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return getTypeName();
	}
	
	
	
	
}
