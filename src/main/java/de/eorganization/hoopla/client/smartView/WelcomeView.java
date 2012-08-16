package de.eorganization.hoopla.client.smartView;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
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
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;

public class WelcomeView extends AbstractView {

	private final static ListGrid decisionsList = new ListGrid();

	public WelcomeView() {
		super(false, false, false);
		getHeading().setContents("Welcome to Hoopla");
		getInstructions()
				.setContents(
						"Start a new Cloud decision or continue one of your existing Cloud decisions.");
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

		Label blankDecisionLabel = new Label(
				"<span style=\"font-size: 10pt; font-style: bold\">Start from the cratch and build your own Cloud Decision.</span>");
		blankDecisionLabel.setAutoHeight();
		blankDecisionLabel.setWrap(false);

		// Buttons
		IButton newBlankDecisionButton = new IButton(
				"<span style=\"font-size: 12pt; font-style: bold\">New Blank Decision</span>");
		newBlankDecisionButton.setWidth(400);
		newBlankDecisionButton.setHeight(50);
		newBlankDecisionButton.setWrap(false);
		newBlankDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Decision dec = new Decision();
				dec.setMember(Hoopla.loginInfo.getMember());
				Hoopla.hooplaService.storeDecision(dec,
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

		Label cloudInfrastructureDecisionLabel = new Label(
				"<span style=\"font-size: 10pt; font-style: bold\">Is Cloud the right platform for you? Make a Infrastructure Decision.</span>");
		cloudInfrastructureDecisionLabel.setAutoHeight();
		cloudInfrastructureDecisionLabel.setWrap(false);


		IButton newInfraDecisionButton = new IButton(
				"<span style=\"font-size: 12pt; font-style: bold\">New Cloud Infrastructure Decision</span>");
		newInfraDecisionButton.setWidth(400);
		newInfraDecisionButton.setHeight(50);
		newInfraDecisionButton.setWrap(false);
		newInfraDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Hoopla.hooplaService.getDecisionTemplate(1L,
						new AsyncCallback<DecisionTemplate>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(DecisionTemplate result) {
								Decision dec = result.getDecision();
								dec.setMember(Hoopla.loginInfo.getMember());
								Hoopla.hooplaService.storeDecision(dec,
										new AsyncCallback<Decision>() {

											@Override
											public void onFailure(
													Throwable caught) {

											}

											@Override
											public void onSuccess(
													Decision result) {
												Hoopla.updateDecision(result);
												Hoopla.tabs.selectTab(Hoopla.tabs
														.getSelectedTabNumber() + 1);
											}
										});
							}
						});

			}

		});
		
		Label cloudComputeServiceDecisionLabel = new Label(
				"<span style=\"font-size: 10pt; font-style: bold\">Which Cloud Compute Service is best for you? Compare Compute Services in a Decision.</span>");
		cloudComputeServiceDecisionLabel.setAutoHeight();
		cloudComputeServiceDecisionLabel.setWrap(false);


		IButton newComputeServiceDecisionButton = new IButton(
				"<span style=\"font-size: 12pt; font-style: bold\">New Cloud Compute Service Decision (soon)</span>");
		newComputeServiceDecisionButton.setWidth(400);
		newComputeServiceDecisionButton.setHeight(50);
		newComputeServiceDecisionButton.setWrap(false);
		newComputeServiceDecisionButton.setDisabled(true);
		
		
		
		IButton selectDecisionButton = new IButton("Select Decision");
		selectDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				goNext();
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
		contents.addMember(cloudInfrastructureDecisionLabel);
		contents.addMember(newInfraDecisionButton);
		contents.addMember(cloudComputeServiceDecisionLabel);
		contents.addMember(newComputeServiceDecisionButton);
		contents.addMember(blankDecisionLabel);
		contents.addMember(newBlankDecisionButton);
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
						"decision") != null) {
			Hoopla.updateDecision((Decision) decisionsList.getSelectedRecord()
					.getAttributeAsObject("decision"));
			Hoopla.tabs.selectTab(Hoopla.tabs.getSelectedTabNumber() + 1);
		} else
			SC.warn("Please select a decision first!");

	}

	@Override
	public void refresh() {
		updateDecisions();
	}

	@Override
	public void asTemplate() {
	}

}