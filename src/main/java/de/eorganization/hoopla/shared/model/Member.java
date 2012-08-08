package de.eorganization.hoopla.shared.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(detachable = "true")
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String email;

	@Persistent
	private String nickname;
	
	@Persistent
	private UserRole role;

	

	/**
	 * 
	 */
	public Member() {
		super();
	}


	/**
	 * 
	 */
	public Member(String email, UserRole role) {
		super();
		this.email = email;
		this.role = role;
	}

	
	/**
	 * @param email
	 * @param nickname
	 * @param role
	 */
	public Member( String email, String nickname, UserRole role) {
		super();
		this.email = email;
		this.nickname = nickname;
		this.role = role;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the role
	 */
	public UserRole getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(UserRole role) {
		this.role = role;
	}


	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}


	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Member [" + (email != null ? "email=" + email + ", " : "")
				+ (nickname != null ? "nickname=" + nickname + ", " : "")
				+ (role != null ? "role=" + role : "") + "]";
	}

	
	
	
}
