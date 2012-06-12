package de.fzi.aotearoa.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.client.datasource.ResultAlternativesDataSource;
import de.fzi.aotearoa.client.datasource.ResultCriteriaWeightsDataSource;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.values.EvaluationResult;

public class ResultView extends AbstractView {

	private TreeGrid weightsGrid = new TreeGrid();

	private ListGrid resultsGrid = new ListGrid();

	// TODO: Word Export als Beratungsprotokoll
	public ResultView() {
		super(true, true, false, "Go Back!", "Make New Decision!", "", -1, 0, 0);

		resultsGrid.setWidth(700);

		resultsGrid.setCanExpandRecords(true);
		resultsGrid.setExpansionMode(ExpansionMode.DETAILS);
		resultsGrid.setLeft(30);

		ListGridField nameResultField = new ListGridField("Name", "Name");
		ListGridField multiValueResultField = new ListGridField("MultiplicativeIndexValue", "Index Value (Multiplicative)");
		ListGridField addiValueResultField = new ListGridField("AdditiveIndexValue", "Index Value (Additive)");
		ListGridField posiValueResultField = new ListGridField("PositiveGoalsValue", "Sum-over-Goals Value (Positive)");
		ListGridField negaValueResultField = new ListGridField("NegativeGoalsValue", "Sum-over-Goals Value (Negative)");
		resultsGrid.setFields(nameResultField, multiValueResultField, addiValueResultField, posiValueResultField, negaValueResultField);
		resultsGrid.setSortField("MultiplicativeIndexValue");
		resultsGrid.setSortDirection(SortDirection.DESCENDING);

		weightsGrid.setWidth(700);
		//weightsGrid.setHeight100();

		weightsGrid.setShowSelectedStyle(false);
		weightsGrid.setShowConnectors(false);

		// resultsGrid.setCanExpandRecords(true);
		// resultsGrid.setExpansionMode(ExpansionMode.DETAILS);
		weightsGrid.setLeft(30);

		ListGridField nameField = new ListGridField("Name", "Name");
		ListGridField localWeightField = new ListGridField("LocalWeight", "Local Weight");
		ListGridField globalWeightField = new ListGridField("GlobalWeight", "Global Weight");
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

		//weightsGrid.draw();

		Label indicesLabel = new Label("Evaluation Results:");
		indicesLabel.setAutoHeight();
		indicesLabel.setAutoHeight();
		indicesLabel.setStyleName("formheader");
		
		Label weightsLabel = new Label("Criteria Weights:");
		weightsLabel.setAutoHeight();
		weightsLabel.setAutoHeight();
		weightsLabel.setStyleName("formheader");
		
		VLayout result = new VLayout();
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
						"The list on the left shows the results calculated by Aotearoa according to the goals, criteria and evaluation you provided. The order of the options reflects the preference suggested by Aotearoa.");

		getContent().addMember(result);

		// AotearoaSmart.decisionUpdatedSubject.addObserver(this);
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
	 * @see de.fzi.aotearoa.client.smartView.AbstractView#update()
	 */
	@Override
	public void refresh() {

		AotearoaSmart.aotearoaService.evaluate(AotearoaSmart.decision, null,
				15, new AsyncCallback<EvaluationResult>() {

					@Override
					public void onSuccess(EvaluationResult result) {
						AotearoaSmart.updateDecision(result.getDecision());
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

						AotearoaSmart.aotearoaService.storeDecision(
								AotearoaSmart.decision,
								new AsyncCallback<Decision>() {
									public void onFailure(Throwable error) {
									}

									public void onSuccess(Decision result) {
										AotearoaSmart.updateDecision(result);

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
