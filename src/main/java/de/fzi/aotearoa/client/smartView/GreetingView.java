package de.fzi.aotearoa.client.smartView;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.types.VerticalAlignment;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.client.smartView.canvas.XMLTemplateUploadWindow;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.DecisionTemplate;

public class GreetingView extends AbstractView {

	private final static ListGrid decisionsList = new ListGrid();

	private final static ListGrid templateList = new ListGrid();

	public GreetingView() {
		super(false, false, false);
		getHeading().setContents("Welcome to Aotearoa");
		getInstructions().setContents(
				"Select an existing decision or a template.");
		getPostit()
				.setContents(
						"Aotearoa offers you several templates to do an  Cloud Computing Provider evaluation. Select a decision, a template  or  create your own decision by clicking 'New Cloud Decision'  ...");

		getNavigation().setVisible(false);

		//ListGrids
		decisionsList.setWidth(500);
		decisionsList.setHeight(300);
		decisionsList.setShowAllRecords(true);
		decisionsList.setSelectionType(SelectionStyle.SINGLE);
		ListGridField DecisionNameField = new ListGridField("name", "Name");
		decisionsList.setFields(DecisionNameField);
		decisionsList.setCanResizeFields(true);

		templateList.setWidth(500);
		templateList.setHeight(300);
		templateList.setShowAllRecords(true);
		templateList.setSelectionType(SelectionStyle.SINGLE);
		ListGridField TemplateNameField = new ListGridField("name", "Name");
		templateList.setFields(TemplateNameField);
		templateList.setCanResizeFields(true);
		
		// Buttons
		IButton newDecisionButton = new IButton("New Decision");
		// newDecision.setAutoFit(true);
		// newDecision.setLeft(30);
		//newDecisionButton.setWidth(150);
		//newDecisionButton.setHeight(50);
		newDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				AotearoaSmart.decision = new Decision();
				AotearoaSmart.tabs.selectTab(AotearoaSmart.tabs
						.getSelectedTabNumber() + 1);

			}

		});

		IButton uploadButton = new IButton("Upload Template");
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new XMLTemplateUploadWindow().show();
			}

		});

		IButton selectDecisionButton = new IButton("Select Decision");
		selectDecisionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AotearoaSmart.aotearoaService.getDecision(
						(Decision) decisionsList.getSelectedRecord()
								.getAttributeAsObject("decision"),
						new AsyncCallback<Decision>() {
							public void onFailure(Throwable error) {
							}

							public void onSuccess(Decision result) {
								if (result != null) {
									AotearoaSmart.updateDecision(result);
									nextTab(-1);
								}
							}
						});
			}
		});
		
		IButton selectTemplateButton = new IButton("Select Template");
		selectTemplateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AotearoaSmart.aotearoaService.getDecisionTemplate(
						(DecisionTemplate) templateList.getSelectedRecord()
								.getAttributeAsObject("template"),
						new AsyncCallback<DecisionTemplate>() {
							public void onFailure(Throwable error) {
							}

							public void onSuccess(DecisionTemplate result) {
								if (result != null) {
									AotearoaSmart.updateDecision(result
											.getDecision());

									nextTab(-1);
								}
							}
						});
			}
		});

		IButton exportTemplateButton = new IButton("Export Template");
		exportTemplateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(GWT.getModuleBaseURL()
						+ "export?id="
						+ templateList.getSelectedRecord()
								.getAttributeAsString("id"), "_blank", "");
			}
		});
		
		// ButtonLayout
		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setMembersMargin(15);
		buttonsLayout.addMember(selectTemplateButton);
		buttonsLayout.addMember(uploadButton);
		buttonsLayout.addMember(exportTemplateButton);
		
		// Labels
		Label decisionLabel = new Label();
		decisionLabel.setContents("<span style='font-size:1.5em;" +
				"text-decoration:underline;'>Existing Decisions:</span>");
		decisionLabel.setWrap(false);
		decisionLabel.setHeight(30);
		decisionLabel.setWidth(500);
		decisionLabel.setValign(VerticalAlignment.BOTTOM);
		
		Label templateLabel = new Label();
		templateLabel.setContents("<span style='font-size:1.6em;" +
				"text-decoration:underline;'>Existing Templates:</span>");
		templateLabel.setWrap(false);
		templateLabel.setHeight(30);
		templateLabel.setWidth(500);
		templateLabel.setValign(VerticalAlignment.BOTTOM);
		
		// Content Layout
		VLayout contents = new VLayout();
		contents.addMember(newDecisionButton);
		contents.addMember(decisionLabel);
		contents.addMember(selectDecisionButton);
		contents.addMember(decisionsList);
		contents.addMember(templateLabel);
		contents.addMember(buttonsLayout);
		contents.addMember(templateList);
		contents.setMembersMargin(10);
		contents.setLeft(30);
		
		Canvas canvas = new Canvas();
		canvas.addChild(contents);

		getContent().addMember(canvas);

		updateDecisions();
	}

	// TODO: statt Decision DecisionTemplates waehlen.
	private void updateDecisions() {

		AotearoaSmart.aotearoaService
				.getDecisions(new AsyncCallback<List<Decision>>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(List<Decision> result) {
						ListGridRecord[] res = new ListGridRecord[result.size()];
						int i = 0;

						for (Decision dc : result) {
							res[i] = new ListGridRecord();
							res[i].setAttribute("name", dc.getName());
							res[i].setAttribute("id", dc.getKeyId());
							res[i].setAttribute("decision", dc);
							i++;
							decisionsList.setData(res);

						}
					}
				});

		AotearoaSmart.aotearoaService
				.getDecisionTemplates(new AsyncCallback<List<DecisionTemplate>>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(List<DecisionTemplate> result) {
						ListGridRecord[] res = new ListGridRecord[result.size()];
						int i = 0;

						for (DecisionTemplate dc : result) {
							res[i] = new ListGridRecord();
							res[i].setAttribute("name", dc.getTemplateName());
							res[i].setAttribute("id", dc.getKeyId());
							res[i].setAttribute("template", dc);
							i++;
							templateList.setData(res);

						}
					}
				});

	}

	@Override
	public void goBack() {
	}

	List<Decision> dec = new ArrayList<Decision>();

	@Override
	public void goNext() {

		if (templateList.getSelectedRecord() != null)
			AotearoaSmart.aotearoaService.getDecision(
					(Decision) ((DecisionTemplate) templateList
							.getSelectedRecord().getAttributeAsObject(
									"template")).getDecision(),
					new AsyncCallback<Decision>() {
						public void onFailure(Throwable error) {
						}

						public void onSuccess(Decision result) {
							if (result != null)
								AotearoaSmart.updateDecision(result);
							else
								AotearoaSmart.updateDecision(new Decision());
							nextTab(-1);
						}
					});

		else if (decisionsList.getSelectedRecord() != null)
			AotearoaSmart.aotearoaService.getDecision((Decision) decisionsList
					.getSelectedRecord().getAttributeAsObject("decision"),
					new AsyncCallback<Decision>() {
						public void onFailure(Throwable error) {
						}

						public void onSuccess(Decision result) {
							if (result != null)
								AotearoaSmart.updateDecision(result);
							else
								AotearoaSmart.updateDecision(new Decision());
							nextTab(-1);
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