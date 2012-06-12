/**
 * 
 */
package de.fzi.aotearoa.server.logic.ahp;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import de.fzi.aotearoa.server.jdo.PersistenceManagerFactoryHelper;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.DecisionTemplate;

/**
 * @author mugglmenzel
 *
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel $ 
 *         
 *           Revision: $Revision: 170 $ 
 *         
 *           Date: $Date: 2011-08-05 16:48:05 +0200 (Fr, 05 Aug 2011) $
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/server/logic/ahp/ModelController.java $
 *
 */
public class ModelController {

	
	public Decision storeDecision(Decision newDecision){
		System.out.println("persisting: " + newDecision);
		PersistenceManager pm = PersistenceManagerFactoryHelper.get().getPersistenceManager();
		Decision result = pm.makePersistent(newDecision);
		System.out.println("persisted: " + result);
		result = pm.detachCopy(result);
		System.out.println("detached: " + result);
		pm.close();
		
		return result;
	}
	
	public List<Decision> getDecisions(){
		
		//String query = "select from " + Decision.class.getName() + " where userId == " + AotearoaSmart.user.getEmailAddress();
		//temporaer folgende Abfrage, da sonst templates nicht geladne werden.
		String query = "select from " + Decision.class.getName() ;
		List<Decision> list = new ArrayList<Decision>();
		list.addAll((List<Decision>) PersistenceManagerFactoryHelper.get().getPersistenceManager().newQuery(query).execute());
		
		return list;
		
	}
	
	public List<DecisionTemplate> getDecisionTemplates(){
		
	//	String query = "select from " + DecisionTemplate.class.getName() + " where userId == " + AotearoaSmart.user.getEmailAddress();
		String query = "select from " + DecisionTemplate.class.getName();
		List<DecisionTemplate> list = new ArrayList<DecisionTemplate>();
		list.addAll((List<DecisionTemplate>) PersistenceManagerFactoryHelper.get().getPersistenceManager().newQuery(query).execute());
		
		return list;
		
	}

	public Decision getDecision(String id) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.get().getPersistenceManager();
	    Decision decision, detached = null;
	    try {
	        decision = pm.getObjectById(Decision.class, id);

	        //detach decision object
	        detached = pm.detachCopy(decision);
	    } finally {
	        pm.close();
	    }
	    return detached;
	}
	
}
