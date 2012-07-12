package de.eorganization.hoopla.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.smartView.canvas.AlternativeDetailsWindow;
import de.eorganization.hoopla.client.smartView.canvas.AlternativesListGrid;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;

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
 *         Revision: $Revision: 216 $
 * 
 *         Date: $Date: 2011-09-15 16:36:49 +0200 (Do, 15 Sep 2011) $
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
 *         src/main/java/de/fzi/aotearoa/client/smartView/AlternativesView.java
 *         $
 * 
 */

public class AlternativesView extends AbstractView {

	final AlternativesListGrid alternativesListGrid;

	public AlternativesView() {
		super();

		getHeading().setContents("2. Alternatives");
		getInstructions()
				.setContents(
						"Define all alternative options available to your Cloud decision.");
		getPostit()
				.setContents(
						"The list on the left shows alternative solutions to implement your project. Every solution can be a Cloud or a non-Cloud option. Add all possible options to the list to let the Hoopla help you with finding out which is the preferred option according to your goals and preferences.");

		VLayout viewLayout = new VLayout();
		viewLayout.setMembersMargin(10);

		alternativesListGrid = new AlternativesListGrid();

		IButton addAlternative = new IButton("Add Alternative");
		addAlternative.setLeft(30);
		addAlternative.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new AlternativeDetailsWindow(alternativesListGrid).show();
			}
		});

		viewLayout.addMember(addAlternative);
		viewLayout.addMember(alternativesListGrid);

		Canvas canvas = new Canvas();
		canvas.addChild(viewLayout);
		viewLayout.setLeft(30);

		getContent().addMember(canvas);
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

	public void refresh() {
		alternativesListGrid.refreshAlternativesListGrid();
	}

	@Override
	public void goBack() {

	}

	@Override
	public void asTemplate() {
		Hoopla.hooplaService.storeDecisionTemplate(new DecisionTemplate(
				Hoopla.decision), new AsyncCallback<DecisionTemplate>() {

			@Override
			public void onSuccess(DecisionTemplate result) {
				System.out.println("Template: " + result);
			}

			@Override
			public void onFailure(Throwable arg0) {
				arg0.printStackTrace();
			}
		});
	}

}
