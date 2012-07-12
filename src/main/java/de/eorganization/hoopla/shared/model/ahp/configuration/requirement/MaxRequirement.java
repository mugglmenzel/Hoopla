/**
 * 
 */
package de.eorganization.hoopla.shared.model.ahp.configuration.requirement;

import de.eorganization.hoopla.shared.model.ahp.configuration.attribute.IEAttribute;

/**
 * @author mugglmenzel
 * 
 */
public class MaxRequirement<T> extends Requirement<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7185304119011925915L;

	public MaxRequirement(String name, 
			IEAttribute attribute, IRequirementItem<T> value) {
		super(name, RequirementType.MAXIMUM, attribute, value);
	}

	@Override
	public boolean checkValue(T item) {
		return getValue().compareTo(item) > 0 || getValue().compareTo(item) == 0;
	}

}
