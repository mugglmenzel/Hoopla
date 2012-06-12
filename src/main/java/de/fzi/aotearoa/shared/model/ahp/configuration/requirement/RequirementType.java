package de.fzi.aotearoa.shared.model.ahp.configuration.requirement;

import java.io.Serializable;

public enum RequirementType implements Serializable {
	EQUALS("equalsRequirement"), MINIMUM("minRequirement"), MAXIMUM(
			"maxRequirement"), ONEOUTOF("oneOfRequirement");

	/**
	 * @uml.property  name="typeName"
	 */
	String typeName;

	private RequirementType(String type) {
		setTypeName(type);
	}

	/**
	 * @return
	 * @uml.property  name="typeName"
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param name
	 * @uml.property  name="typeName"
	 */
	public void setTypeName(String name) {
		this.typeName = name;
	}
}
