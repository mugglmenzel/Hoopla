/**
 * 
 */
package de.eorganization.hoopla.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.eorganization.hoopla.shared.LoginInfo;

/**
 * @author menzel
 * 
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	public LoginInfo login(String requestUri);
	
}
