/**
 * 
 */
package de.eorganization.hoopla.client.subjects;



import de.eorganization.hoopla.client.observer.Observable;
import de.eorganization.hoopla.client.observer.events.DecisionUpdatedEvent;

/**
 * @author menzel
 *
 */
public class DecisionUpdatedSubject extends Observable {

	public void raiseUpdatedEvent(DecisionUpdatedEvent event) {
		setChanged();
		notifyObservers(event);
	}
			
}
