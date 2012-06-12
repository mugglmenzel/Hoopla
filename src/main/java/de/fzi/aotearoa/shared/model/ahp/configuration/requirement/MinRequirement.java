/**
 * 
 */
package de.fzi.aotearoa.shared.model.ahp.configuration.requirement;

import de.fzi.aotearoa.shared.model.ahp.configuration.attribute.IEAttribute;

/**
 * @author mugglmenzel
 * 
 */
public class MinRequirement<T> extends Requirement<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7375902195281171750L;

	public MinRequirement(String name, IEAttribute attribute,
			IRequirementItem<T> value) {
		super(name, RequirementType.MINIMUM, attribute, value);
	}

	@Override
	public boolean checkValue(T item) {
		return getValue().compareTo(item) < 0
				|| getValue().compareTo(item) == 0;
	}

}
