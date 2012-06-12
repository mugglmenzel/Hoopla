package de.fzi.aotearoa.shared.model.ahp.configuration.attribute;

/**
 * @author  mugglmenzel
 */
public interface IEAttribute {

	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	public abstract void setName(String name);

	/**
	 * @return  the name
	 * @uml.property  name="name"
	 */
	public abstract String getName();

}