/**
 * 
 */
package de.eorganization.hoopla.server.jdo;

import java.util.logging.Logger;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel@gmail.com $
 * 
 *         Revision: $Revision: 226 $
 * 
 *         Date: $Date: 2011-09-19 22:48:20 +0200 (Mo, 19 Sep 2011) $
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
 *         /aotearoa/server/jdo/PersistenceManagerFactoryHelper.java $
 * 
 */
public class PersistenceManagerFactoryHelper {

	private static Logger log = Logger
			.getLogger(PersistenceManagerFactoryHelper.class.getName());

	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static PersistenceManager getPM() {
		PersistenceManager pm = get().getPersistenceManager();
		pm.getFetchPlan().setMaxFetchDepth(-1);
		pm.getFetchPlan().setFetchSize(FetchPlan.FETCH_SIZE_GREEDY);
		pm.getFetchPlan().setDetachmentOptions(FetchPlan.DETACH_LOAD_FIELDS);
		log.fine("fetch groups: " + pm.getFetchPlan().getGroups());
		pm.setDetachAllOnCommit(true);
		return pm;
	}

}
