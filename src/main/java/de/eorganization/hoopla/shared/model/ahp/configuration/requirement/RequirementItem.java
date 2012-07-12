/**
 * 
 */
package de.eorganization.hoopla.shared.model.ahp.configuration.requirement;

/**
 * @author mugglmenzel
 *
 */
public class RequirementItem<T> implements IRequirementItem<T> {

	
	/**
	 * @uml.property  name="value"
	 */
	private Comparable<T> value;
	
	/**
	 * 
	 */
	public RequirementItem(Comparable<T> value) {
		super();
		this.value = value;
	}

	@Override
	public int compareTo(T o) {
		return getValue().compareTo(o);
	}

	@Override
	public Comparable<T> getValue() {
		return value;
	}

}
