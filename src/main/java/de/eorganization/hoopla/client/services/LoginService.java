/**
 * 
 */
package de.eorganization.hoopla.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.eorganization.hoopla.shared.model.LoginInfo;
import de.eorganization.hoopla.shared.model.Member;

/**
 * @author menzel
 * 
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	public LoginInfo login(String requestUri);
	
	public Member getMember(String id);
	
	public Member storeMember(Member member);
	
}
