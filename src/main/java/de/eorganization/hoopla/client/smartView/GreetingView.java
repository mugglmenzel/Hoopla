package de.eorganization.hoopla.client.smartView;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

public class GreetingView extends AbstractView {

	private final static ListGrid decisionsList = new ListGrid();

	public GreetingView() {
		super(false, false, false);
		getHeading().setContents("Welcome to the Hoopla");
		getInstructions().setContents(
				"Select an existing decision or a template.");
		getPostit()
				.setContents(
						"The Hoopla offers you several templates to do an  Cloud Computing Provider evaluation. Select a decision, a template  or  create your own decision by clicking 'New Cloud Decision'  ...");

		getNavigation().setVisible(false);

		// ListGrids
		decisionsList.setWidth(500);
		decisionsList.setHeight(300);
		decisionsList.setShowAllRecords(true);
		decisionsList.setSelectionType(SelectionStyle.SINGLE);
		ListGridField DecisionNameField = new ListGridField("name", "Name");
		decisionsList.setFields(DecisionNameField);
		decisionsList.setCanResizeFields(true);
		// Buttons
		IButton newDecisionButton = new IButton("New Decision");
		// newDecision.setAutoFit(true);
		// newDecision.setLeft(30);
		// newDecisionButton.setWidth(150);
		// newDecisionButton.setHeight(50);
		newDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Hoopla.hooplaService.storeDecision(new Decision(),
						new AsyncCallback<Decision>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Decision result) {
								Hoopla.updateDecision(result);
								Hoopla.tabs.selectTab(Hoopla.tabs
										.getSelectedTabNumber() + 1);
							}
						});

			}

		});
		IButton selectDecisionButton = new IButton("Select Decision");
		selectDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Hoopla.updateDecision((Decision) decisionsList
						.getSelectedRecord().getAttributeAsObject("decision"));
			}
		});

		// Labels
		Label decisionLabel = new Label();
		decisionLabel.setContents("<span style='font-size:1.5em;"
				+ "text-decoration:underline;'>Existing Decisions:</span>");
		decisionLabel.setWrap(false);
		decisionLabel.setHeight(30);
		decisionLabel.setWidth(500);
		decisionLabel.setValign(VerticalAlignment.BOTTOM);

		// Content Layout
		VLayout contents = new VLayout();
		contents.addMember(newDecisionButton);
		contents.addMember(decisionLabel);
		contents.addMember(selectDecisionButton);
		contents.addMember(decisionsList);
		// contents.addMember(templateLabel);
		// contents.addMember(buttonsLayout);
		// contents.addMember(templateList);
		contents.setMembersMargin(10);
		contents.setLeft(30);

		Canvas canvas = new Canvas();
		canvas.addChild(contents);

		getContent().addMember(canvas);

		updateDecisions();
	}

	private void updateDecisions() {

		Hoopla.hooplaService.getDecisions(Hoopla.loginInfo.getMember().getEmail(),
				new AsyncCallback<List<Decision>>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(List<Decision> result) {
						ListGridRecord[] res = new ListGridRecord[result.size()];
						int i = 0;

						for (Decision dc : result) {
							res[i] = new ListGridRecord();
							res[i].setAttribute("name", dc.getName());
							res[i].setAttribute("id", dc.getId());
							res[i].setAttribute("decision", dc);
							i++;
							decisionsList.setData(res);

						}
					}
				});

	}

	@Override
	public void goBack() {
	}

	@Override
	public void goNext() {

		if (decisionsList.getSelectedRecord() != null
				&& decisionsList.getSelectedRecord().getAttributeAsObject(
						"decision") != null)
			Hoopla.updateDecision((Decision) decisionsList.getSelectedRecord()
					.getAttributeAsObject("decision"));
		else
			Hoopla.hooplaService.storeDecision(new Decision(),
					new AsyncCallback<Decision>() {

						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(Decision result) {
							Hoopla.updateDecision(result);
						}
					});

	}

	@Override
	public void refresh() {
		updateDecisions();
	}

	@Override
	public void asTemplate() {
	}

}