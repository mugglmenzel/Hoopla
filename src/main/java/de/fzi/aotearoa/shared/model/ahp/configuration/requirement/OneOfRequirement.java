/**
 * 
 */
package de.fzi.aotearoa.shared.model.ahp.configuration.requirement;

import java.util.SortedSet;

import javax.jdo.annotations.PersistenceCapable;

import de.fzi.aotearoa.shared.model.ahp.configuration.attribute.IEAttribute;

/**
 * @author mugglmenzel
 * @param <T>
 *
 */
@PersistenceCapable
public class OneOfRequirement<T> extends Requirement<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -281977232271734055L;

	public OneOfRequirement(String name,
			IEAttribute attribute, SortedSet<IRequirementItem<T>> values) {
		super(name, RequirementType.ONEOUTOF, attribute, values);
	}

	@Override
	public boolean checkValue(T item) {
		return getValues().contains(item);
	}

}
