package de.eorganization.hoopla.client.smartView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.datasource.ResultAlternativesDataSource;
import de.eorganization.hoopla.client.datasource.ResultCriteriaWeightsDataSource;
import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;
import de.eorganization.hoopla.shared.model.ahp.values.EvaluationResult;

public class ResultView extends AbstractView {

	private TreeGrid weightsGrid = new TreeGrid();

	private ListGrid resultsGrid = new ListGrid();

	private Label bestLabel = new Label("");

	// TODO: Word Export als Beratungsprotokoll
	public ResultView() {
		super(true, true, false, "Go Back!", "Make New Decision!", "", -1, 0, 0);

		HLayout bestLayout = new HLayout();
		Img bestImg = new Img("/checkmark.png", 50, 50);

		VLayout bestLabelLayout = new VLayout();
		Label bestIntroLabel = new Label(
				"<span style=\"font-size: 22pt\">The best option according to your preferences is </span>");
		bestIntroLabel.setWrap(false);
		bestLabel.setWrap(false);
		bestLabel.setAutoWidth();
		bestLabelLayout.addMember(bestIntroLabel);
		bestLabelLayout.addMember(bestLabel);

		bestLayout.addMember(bestImg);
		bestLayout.addMember(bestLabelLayout);

		resultsGrid.setWidth(700);

		resultsGrid.setCanExpandRecords(true);
		resultsGrid.setExpansionMode(ExpansionMode.DETAILS);
		resultsGrid.setLeft(30);

		ListGridField nameResultField = new ListGridField("Name", "Name");
		ListGridField multiValueResultField = new ListGridField(
				"MultiplicativeIndexValue", "Index Value (Multiplicative)");
		ListGridField addiValueResultField = new ListGridField(
				"AdditiveIndexValue", "Index Value (Additive)");
		ListGridField posiValueResultField = new ListGridField(
				"PositiveGoalsValue", "Sum-over-Goals Value (Positive)");
		ListGridField negaValueResultField = new ListGridField(
				"NegativeGoalsValue", "Sum-over-Goals Value (Negative)");
		resultsGrid.setFields(nameResultField, multiValueResultField,
				addiValueResultField, posiValueResultField,
				negaValueResultField);
		resultsGrid.setSortField("MultiplicativeIndexValue");
		resultsGrid.setSortDirection(SortDirection.DESCENDING);

		weightsGrid.setWidth(700);
		// weightsGrid.setHeight100();

		weightsGrid.setShowSelectedStyle(false);
		weightsGrid.setShowConnectors(false);

		// resultsGrid.setCanExpandRecords(true);
		// resultsGrid.setExpansionMode(ExpansionMode.DETAILS);
		weightsGrid.setLeft(30);

		ListGridField nameField = new ListGridField("Name", "Name");
		ListGridField localWeightField = new ListGridField("LocalWeight",
				"Local Weight");
		ListGridField globalWeightField = new ListGridField("GlobalWeight",
				"Global Weight");
		weightsGrid.setFields(nameField, localWeightField, globalWeightField);

		final Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("Name");
		tree.setIdField("Id");
		tree.setParentIdField("ParentId");
		tree.setShowRoot(false);

		weightsGrid.addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				tree.openAll();
			}
		});

		weightsGrid.setData(tree);
		weightsGrid.selectAllRecords();

		// if (isEditable) {
		// addDragAndDrop();
		// }

		// weightsGrid.draw();

		Label indicesLabel = new Label("Evaluation Results:");
		indicesLabel.setAutoHeight();
		indicesLabel.setAutoHeight();
		indicesLabel.setStyleName("formheader");

		Label weightsLabel = new Label("Criteria Weights:");
		weightsLabel.setAutoHeight();
		weightsLabel.setAutoHeight();
		weightsLabel.setStyleName("formheader");

		VLayout result = new VLayout();
		result.addMember(bestLayout);
		result.addMember(indicesLabel);
		result.addMember(resultsGrid);
		result.addMember(weightsLabel);
		result.addMember(weightsGrid);
		result.setLeft(30);
		result.setMembersMargin(10);

		getHeading().setContents("6. Results");
		getInstructions().setContents(
				"Values for your Decision have been calculated.");
		getPostit()
				.setContents(
						"The list on the left shows the results calculated by the Hoopla according to the goals, criteria and evaluation you provided. The order of the options reflects the preference suggested by the Hoopla.");

		getContent().addMember(result);

		// Hoopla.decisionUpdatedSubject.addObserver(this);
	}

	@Override
	public void goNext() {

	}

	@Override
	public void goBack() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.AbstractView#update()
	 */
	@Override
	public void refresh() {

		Hoopla.hooplaService.evaluate(Hoopla.decision, null, 15,
				new AsyncCallback<EvaluationResult>() {

					@Override
					public void onSuccess(EvaluationResult result) {
						Hoopla.updateDecision(result.getDecision());

						bestLabel
								.setContents("<span style=\"font-size: 24pt; font-style: bold\">"
										+ Collections
												.max(new ArrayList<Entry<Alternative, Double>>(
														result.getResultMultiplicativeIndexMap()
																.entrySet()),
														new Comparator<Entry<Alternative, Double>>() {

															@Override
															public int compare(
																	Entry<Alternative, Double> o1,
																	Entry<Alternative, Double> o2) {
																return o1
																		.getValue()
																		.compareTo(
																				o2.getValue());
															}
														}).getKey().getName()
										+ "</span>");

						resultsGrid.setData(new ResultAlternativesDataSource()
								.createListGridRecords(result));

						final Tree tree = new Tree();
						tree.setModelType(TreeModelType.PARENT);
						tree.setNameProperty("Name");
						tree.setIdField("Id");
						tree.setParentIdField("ParentId");
						tree.setRootValue("root");
						tree.setShowRoot(false);
						tree.setData(new ResultCriteriaWeightsDataSource()
								.createResultTreeNodeArray(result));
						weightsGrid.setData(tree);
						weightsGrid.selectAllRecords();
						tree.openAll();

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
					public void onFailure(Throwable arg0) {
					}
				});

	}

	@Override
	public void asTemplate() {
		// TODO Auto-generated method stub

	}

}
