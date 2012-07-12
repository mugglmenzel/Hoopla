package de.eorganization.hoopla.server.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.datanucleus.jdo.NucleusJDOHelper;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.eorganization.hoopla.client.services.HooplaService;
import de.eorganization.hoopla.server.jdo.PersistenceManagerFactoryHelper;
import de.eorganization.hoopla.server.logic.ahp.AnalyticHierarchyProcess;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;
import de.eorganization.hoopla.shared.model.ahp.values.Evaluation;
import de.eorganization.hoopla.shared.model.ahp.values.EvaluationResult;

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
 *         Revision: $Revision: 245 $
 * 
 *         Date: $Date: 2011-09-26 15:25:40 +0200 (Mo, 26 Sep 2011) $
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
 *         src/main/java/de/fzi /aotearoa/server/services/HooplaServiceImpl.java
 *         $
 * 
 */

public class HooplaServiceImpl extends RemoteServiceServlet implements
		HooplaService {

	/**
	 * 
	 */

	private static final long serialVersionUID = -4582394872006617063L;

	private static Logger log = Logger.getLogger(HooplaServiceImpl.class
			.getName());

	/**
	 * 
	 */
	public HooplaServiceImpl() {
		super();
		// this.pm = PersistenceManagerFactoryHelper.getPM();
	}

	public Decision storeDecision(Decision newDecision) {
		Decision result = null;
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		// Transaction tx = pm.currentTransaction();
		try {

			log.info("storing " + newDecision);
			// tx.begin();
			result = pm.makePersistent(newDecision);
			// tx.commit();

			result = detachDecision(result, pm);

		} catch (Exception e) {
			e.printStackTrace();
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
			// tx.rollback();
		} finally {
			pm.close();
		}

		return result;
	}

	public DecisionTemplate storeDecisionTemplate(DecisionTemplate newTemplate) {
		DecisionTemplate result = null;
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		// Transaction tx = pm.currentTransaction();
		try {

			// tx.begin();
			result = pm.makePersistent(newTemplate);
			// tx.commit();

			result = detachDecisionTemplate(result, pm);

		} catch (Exception e) {
			e.printStackTrace();
			// tx.rollback();
		} finally {
			pm.close();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Decision> getDecisions() {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		// String query = "select from " + Decision.class.getName() +
		// " where userId == " + AotearoaSmart.user.getEmailAddress();
		// temporaer folgende Abfrage, da sonst templates nicht geladne werden.
		// String query = "select from " + Decision.class.getName();

		List<Decision> list = new ArrayList<Decision>();
		try {

			Collection<Decision> results = (Collection<Decision>) pm.newQuery(
					Decision.class).execute();
			if (results.size() > 0)
				for (Decision dec : results)
					list.add(detachDecision(dec, pm));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		return list;

	}

	@SuppressWarnings("unchecked")
	public List<DecisionTemplate> getDecisionTemplates() {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		// String query = "select from " + DecisionTemplate.class.getName() +
		// " where userId == " + AotearoaSmart.user.getEmailAddress();
		// String query = "select from " + DecisionTemplate.class.getName();
		// List<DecisionTemplate> list = new ArrayList<DecisionTemplate>();
		// list.addAll((List<DecisionTemplate>) pm.newQuery(query).execute());
		List<DecisionTemplate> list = new ArrayList<DecisionTemplate>();
		try {

			Collection<DecisionTemplate> results = (Collection<DecisionTemplate>) pm
					.newQuery(DecisionTemplate.class).execute();
			if (results.size() > 0)
				for (DecisionTemplate dec : results)
					list.add(detachDecisionTemplate(dec, pm));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		return list;

	}

	public Decision getDecision(Decision dec) {

		if (dec != null) {
			PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
			Decision detached = null;
			try {

				// pm.retrieve(dec);

				detached = detachDecision(dec, pm);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pm.close();
			}

			return detached;
		} else
			return null;
	}

	private Decision detachDecision(Decision dec, PersistenceManager pm) {
		Decision detached = null;
		try {

			detached = pm.detachCopy(dec);

			log.fine("loaded decision fields: "
					+ Arrays.toString(NucleusJDOHelper
							.getDetachedObjectLoadedFields(detached, pm)));

			enrichDecision(detached);

			touchDecision(detached);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return detached;
	}

	private DecisionTemplate detachDecisionTemplate(DecisionTemplate dec,
			PersistenceManager pm) {
		DecisionTemplate detached = null;
		try {

			detached = pm.detachCopy(dec);

			log.fine("loaded decision template fields: "
					+ Arrays.toString(NucleusJDOHelper
							.getDetachedObjectLoadedFields(detached, pm)));

			enrichDecision(detached);

			touchDecision(detached);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return detached;
	}

	public DecisionTemplate getDecisionTemplate(DecisionTemplate dec) {

		if (dec != null) {
			DecisionTemplate detached = null;
			PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();

			try {

				// pm.retrieve(dec);

				detached = detachDecisionTemplate(dec, pm);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pm.close();
			}

			return detached;
		} else
			return null;
	}

	private Decision enrichDecision(Decision dec) {

		// set parents
		for (Criterion g : dec.getGoals()) {
			setCriteriaParents(g);
		}

		return dec;
	}

	private void touchDecision(Decision dec) {
		for (Criterion g : dec.getGoals()) {
			if (g.hasChildren())
				for (Criterion c : g.getChildren())
					touchCriterionChild(c);
		}
	}

	private void touchCriterionChild(Criterion c) {
		c.getImportanceAlternatives().size();
		c.getImportanceChildren().size();
		c.getValuesAlternatives().size();
		if (c.hasChildren())
			for (Criterion cr : c.getChildren())
				touchCriterionChild(cr);
	}

	private void setCriteriaParents(Criterion parent) {
		for (Criterion c : parent.getChildren()) {
			if (!c.hasParent())
				c.setParent(parent);
			if (c.hasChildren())
				setCriteriaParents(c);
		}
	}

	public Decision getDecision(Long id) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Decision decision, detached = null;
		try {

			decision = pm.getObjectById(Decision.class,
					KeyFactory.createKey(Decision.class.getSimpleName(), id));
			// decision = pm.getObjectById(Decision.class, id);
			// decision = (Decision)
			// pm.getObjectById(KeyFactory.stringToKey(id));

			// detach decision object
			detached = pm.detachCopy(decision);

			enrichDecision(detached);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		return detached;
	}

	public DecisionTemplate getDecisionTemplate(Long id) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		DecisionTemplate decision, detached = null;
		try {

			decision = pm.getObjectById(DecisionTemplate.class, KeyFactory
					.createKey(DecisionTemplate.class.getSimpleName(), id));
			// decision = pm.getObjectById(Decision.class, id);
			// decision = (Decision)
			// pm.getObjectById(KeyFactory.stringToKey(id));

			// detach decision object
			detached = pm.detachCopy(decision);

			enrichDecision(detached);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		return detached;
	}

	public EvaluationResult evaluate(Decision decision, List<Evaluation> eval,
			int precision) throws Exception {
		try {
			AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(
					decision);
			return ahp.evaluate(eval, precision);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
