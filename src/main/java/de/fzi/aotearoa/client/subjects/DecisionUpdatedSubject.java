/**
 * 
 */
package de.fzi.aotearoa.client.subjects;



import de.fzi.aotearoa.client.observer.Observable;
import de.fzi.aotearoa.client.observer.events.DecisionUpdatedEvent;

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
