package de.eorganization.hoopla.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.eorganization.hoopla.client.services.LoginService;
import de.eorganization.hoopla.server.jdo.PersistenceManagerFactoryHelper;
import de.eorganization.hoopla.shared.model.LoginInfo;
import de.eorganization.hoopla.shared.model.Member;
import de.eorganization.hoopla.shared.model.UserRole;

/**
 * 
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel $
 * 
 *         Revision: $Revision: 170 $
 * 
 *         Date: $Date: 2011-08-05 16:48:05 +0200 (Fr, 05 Aug 2011) $
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
 *         src/main/java/de/fzi/aotearoa/server/services/LoginServiceImpl.java $
 * 
 */

public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1447714256662875710L;

	private Logger log = Logger.getLogger(LoginServiceImpl.class.getName());

	private UserService userService = UserServiceFactory.getUserService();

	public LoginInfo login(String requestUri) {
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		Member mbr = null;
		if (userService.isUserLoggedIn() && user != null) {
			mbr = getMember(user.getEmail());
			if (mbr == null)
				mbr = storeMember(new Member(user.getEmail(),
						user.getNickname(),
						userService.isUserAdmin() ? UserRole.ADMIN
								: UserRole.USER));
		}
		if (mbr != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setMember(mbr);
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	public Member getMember(String email) {
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		Member memb = null;
		try {
			memb = pm.getObjectById(Member.class,
					KeyFactory.createKey(Member.class.getSimpleName(), email));
			memb = pm.detachCopy(memb);
		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
		return memb;
	}

	public Member storeMember(Member member) {
		if(member == null) return null;
		PersistenceManager pm = PersistenceManagerFactoryHelper.getPM();
		try {
			log.info("storing member " + member + " with id " + member.getEmail());
			return pm.detachCopy(pm.makePersistent(member));
		} catch (Exception e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		}
	}

}
