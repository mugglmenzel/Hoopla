package de.fzi.aotearoa.client.observer.events;

import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;

public class DecisionUpdatedEvent {
	
	private Decision decision;

	/**
	 * @param decision
	 */
	public DecisionUpdatedEvent(Decision decision) {
		super();
		this.decision = decision;
	}



	/**
	 * @return the decision
	 */
	public Decision getDecision() {
		return decision;
	}
}