/**
 * 
 */
package de.eorganization.hoopla.shared.model.ahp.configuration.requirement;

/**
 * @author mugglmenzel
 * @param <T>
 * 
 */
public interface IRequirementItem<T> extends Comparable<T> {

	public Comparable<T> getValue();

}
