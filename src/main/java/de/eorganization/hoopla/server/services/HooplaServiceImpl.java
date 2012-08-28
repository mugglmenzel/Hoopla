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
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.eorganization.hoopla.client.services.HooplaService;
import de.eorganization.hoopla.server.jdo.PersistenceManagerFactoryHelper;
import de.eorganization.hoopla.server.logic.ahp.AnalyticHierarchyProcess;
import de.eorganization.hoopla.shared.model.Member;
import de.eorganization.hoopla.shared.model.UserRole;
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
	}

	public Decision storeDecision(Decision newDecision) {
		Decision result = null;
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		try {

			result = pm.makePersistent(newDecision);

			result = detachDecision(result, pm);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return result;
	}
	
	public boolean deleteDecision(Decision decision) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		try {

			pm.deletePersistent(decision);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
			return false;
		} finally {
			pm.close();
		}

		return true;
	}

	public DecisionTemplate storeDecisionTemplate(DecisionTemplate newTemplate) {
		DecisionTemplate result = null;
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		try {

			result = pm.makePersistent(newTemplate);

			result = detachDecisionTemplate(result, pm);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return result;
	}
	
	public boolean deleteDecisionTemplate(DecisionTemplate decision) {
		log.info("deleting " + decision);
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		try {

			pm.deletePersistent(decision);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
			return false;
		} finally {
			pm.close();
		}
		log.info("deleted " + decision);

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Decision> getDecisions(String userId) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		List<Decision> list = new ArrayList<Decision>();
		try {

			Collection<Decision> results = (Collection<Decision>) pm.newQuery(
					Decision.class, "member.email == '" + userId + "'")
					.execute();
			if (results.size() > 0)
				for (Decision dec : results)
					list.add(detachDecision(dec, pm));

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		log.info("returning list " + list);
		return list;

	}

	@SuppressWarnings("unchecked")
	public List<DecisionTemplate> getDecisionTemplates() {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		List<DecisionTemplate> list = new ArrayList<DecisionTemplate>();
		try {

			Collection<DecisionTemplate> results = (Collection<DecisionTemplate>) pm
					.newQuery(DecisionTemplate.class).execute();
			if (results.size() > 0)
				for (DecisionTemplate dec : results)
					list.add(detachDecisionTemplate(dec, pm));
		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
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

				detached = detachDecision(dec, pm);

			} catch (Exception e) {
				log.log(Level.WARNING, e.getLocalizedMessage(), e);
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
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
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
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
		return detached;
	}

	public DecisionTemplate getDecisionTemplate(DecisionTemplate dec) {

		if (dec != null) {
			DecisionTemplate detached = null;
			PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();

			try {

				detached = detachDecisionTemplate(dec, pm);

			} catch (Exception e) {
				log.log(Level.WARNING, e.getLocalizedMessage(), e);
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
			detached = pm.detachCopy(decision);

			enrichDecision(detached);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
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
			detached = pm.detachCopy(decision);

			enrichDecision(detached);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return detached;
	}

	public Member getMember(String email) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Member mbr, detached = null;
		try {

			mbr = pm.getObjectById(Member.class,
					KeyFactory.createKey(Member.class.getSimpleName(), email));
			detached = pm.detachCopy(mbr);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return detached;

	}

	public Member saveOrGetMember(User user) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Member mbr, detached = null;
		try {

			mbr = pm.getObjectById(
					Member.class,
					KeyFactory.createKey(Member.class.getSimpleName(),
							user.getEmail()));
			if (mbr == null)
				detached = registerMember(new Member(user.getEmail(),
						user.getNickname(), UserRole.USER));
			else
				detached = pm.detachCopy(mbr);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return detached;
	}

	public Member registerMember(Member member) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Member mbr, detached = null;
		try {

			mbr = pm.makePersistent(member);
			detached = pm.detachCopy(mbr);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return detached;
	}

	public Member updateMember(Member member) {
		return registerMember(member);
	}

	public EvaluationResult evaluate(Decision decision, List<Evaluation> eval,
			int precision) throws Exception {
		try {
			AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(
					decision);
			return ahp.evaluate(eval, precision);
		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public Member findMemberBySocialId(String socialId) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Member mbr, detached = null;
		try {

			mbr = new ArrayList<Member>(((Collection<Member>) pm.newQuery(
					Member.class, "socialId == '" + socialId + "'").execute())).get(0);

			detached = pm.detachCopy(mbr);

		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		} finally {
			pm.close();
		}

		return detached;
	}

}
