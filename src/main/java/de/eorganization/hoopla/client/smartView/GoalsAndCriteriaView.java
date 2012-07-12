package de.eorganization.hoopla.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.smartView.canvas.GoalsAndCriteriaCanvas;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

/**
 * 
 * @author mugglmenzel
 * @author svens0n
 * 
 *         Author: Michael Menzel (mugglmenzel), Sven Frauen (svens0n)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel $
 * 
 *         Revision: $Revision: 200 $
 * 
 *         Date: $Date: 2011-09-01 16:35:31 +0200 (Do, 01 Sep 2011) $
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
 *         /aotearoa/client/smartView/GoalsAndCriteriaView.java $
 * 
 */

public class GoalsAndCriteriaView extends AbstractView {

	GoalsAndCriteriaCanvas goalsAndCriteriaCanvas;

	public GoalsAndCriteriaView() {
		super();

		getHeading().setContents("3. Goals and Criteria");
		getInstructions().setContents("Define your Goals and Criteria.");
		getPostit()
				.setContents(
						"In order to customize your decision along the project's requirements and goals the input form on the left allows you to represent goals and related criteria in a hierarchy. Initially, a recommended criteria hierarchy is suggested to ease the customization process. The number of hierarchy levels and criteria is not limited, but every further criterion increases the complexity of your Cloud decision. Moreover, you can choose what type of criteria you are considering. Quantitative and benchmarking types are measurable, critera of the qualitative type must be evaluated by your subjective perception.");

		goalsAndCriteriaCanvas = new GoalsAndCriteriaCanvas(true);
		goalsAndCriteriaCanvas.changeLeftDistance(30);
		goalsAndCriteriaCanvas.setCustomSize(600, 500);
		getContent().addMember(goalsAndCriteriaCanvas);
		refresh();
		//Hoopla.decisionUpdatedSubject.addObserver(this);
	}

	@Override
	public void goNext() {
		Hoopla.hooplaService.storeDecision(Hoopla.decision,
				new AsyncCallback<Decision>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(Decision result) {
						Hoopla.updateDecision(result);
					}
				});
	}

	@Override
	public void goBack() {

	}

	public void refresh() {
		goalsAndCriteriaCanvas.refresh();

	}

	@Override
	public void asTemplate() {
		// TODO Auto-generated method stub

	}

}
