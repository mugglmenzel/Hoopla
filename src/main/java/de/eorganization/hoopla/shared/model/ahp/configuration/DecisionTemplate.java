package de.eorganization.hoopla.shared.model.ahp.configuration;

import java.io.Serializable;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;

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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/de.eorganization.hoopla.shared.model/model/ahp/configuration/DecisionTemplate.java $
 *
 */


@PersistenceCapable(detachable = "true")
@Inheritance(customStrategy = "complete-table")
public class DecisionTemplate extends Decision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3869418492398074682L;
	
	
	private String templateName;

	public DecisionTemplate() {
		super();
	}

	public DecisionTemplate(Decision dec) {
		super();
		Decision decision = dec.clone();
		setAlternatives(decision.getAlternatives());
		setGoals(decision.getGoals());
		setUserId(decision.getUserId());
		setName(decision.getName());
		setDescription(decision.getDescription());
		setTemplateName("New Template");

	}

	public Decision getDecision() {
		return (Decision) clone();
	}

	/**
	 * @param templateName
	 *            the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

}
