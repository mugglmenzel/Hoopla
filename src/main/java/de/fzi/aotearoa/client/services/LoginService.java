/**
 * 
 */
package de.fzi.aotearoa.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fzi.aotearoa.shared.LoginInfo;

/**
 * @author menzel
 * 
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	public LoginInfo login(String requestUri);
	
}
