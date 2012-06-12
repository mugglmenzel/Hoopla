package de.fzi.aotearoa.shared.model.ahp.configuration.requirement;

import java.io.Serializable;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.attribute.IEAttribute;

/**
 * 
 * @author menzel Description to be inserted here.
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel@gmail.com $
 * 
 *         Revision: $Revision: 242 $
 * 
 *         Date: $Date: 2011-09-25 17:48:24 +0200 (So, 25 Sep 2011) $
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
 *         /aotearoa/shared/model/ahp/configuration/Criterion.java $
 * 
 */

@PersistenceCapable
public abstract class Requirement<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4265980830540891007L;

	/**
	 * @uml.property name="name"
	 */
	@Persistent
	private String name;

	@Persistent(defaultFetchGroup = "true")
	private Decision decision;
	
	/**
	 * @uml.property name="reqType"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private RequirementType reqType;

	/**
	 * @uml.property name="attributeName"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private IEAttribute attributeName;

	/**
	 * @uml.property name="value"
	 * @uml.associationEnd
	 */
	private IRequirementItem<T> value;

	/**
	 * @uml.property name="values"
	 */
	private Set<IRequirementItem<T>> values;

	public Requirement(String name, RequirementType reqType,
			IEAttribute attribute, IRequirementItem<T> value) {
		super();
		this.name = name;
		this.reqType = reqType;
		this.attributeName = attribute;
		this.value = value;
	}

	public Requirement(String name, RequirementType reqType,
			IEAttribute attribute, Set<IRequirementItem<T>> values) {
		super();
		this.name = name;
		this.reqType = reqType;
		this.attributeName = attribute;
		this.setValues(values);
	}

	public IRequirementItem<T> getValue() {
		return value;
	}

	public void setValue(IRequirementItem<T> value) {
		this.value = value;
	}

	/**
	 * @return
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property name="reqType"
	 */
	public RequirementType getReqType() {
		return reqType;
	}

	/**
	 * @param reqType
	 * @uml.property name="reqType"
	 */
	public void setReqType(RequirementType reqType) {
		this.reqType = reqType;
	}

	/**
	 * @return the attribute
	 * @uml.property name="attributeName"
	 */
	public IEAttribute getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attribute
	 *            the attribute to set
	 * @uml.property name="attributeName"
	 */
	public void setAttributeName(IEAttribute attribute) {
		this.attributeName = attribute;
	}

	/**
	 * @return the values
	 */
	public Set<IRequirementItem<T>> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(Set<IRequirementItem<T>> values) {
		this.values = values;
	}

	public abstract boolean checkValue(T item);

}
