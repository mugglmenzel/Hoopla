package de.fzi.aotearoa.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.RequirementsXmlDS;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;

public class RequirementsView extends AbstractView {
	public RequirementsView(){
		super();
		DataSource requirementsDS = RequirementsXmlDS.getInstance();
		final TreeGrid treeGrid = new TreeGrid();
		treeGrid.setLoadDataOnDemand(false);
		treeGrid.setWidth(500);
		treeGrid.setHeight(400);
		treeGrid.setLeft(30);
		treeGrid.setDataSource(requirementsDS);
		treeGrid.setAutoFetchData(true);
		treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		treeGrid.setShowSelectedStyle(false);
		treeGrid.setShowPartialSelection(true);
		treeGrid.setCascadeSelection(true);
		treeGrid.setSelectionProperty("isSelected");
		
		TreeGridField field = new TreeGridField("name", "Name");
		//TreeGridField type = new TreeGridField("type", "Type");
		//type.setCanEdit(true);
		treeGrid.setFields(field);

		treeGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
				treeGrid.getData().openAll();
			}
		});
		
		Canvas requirements = new Canvas();
		requirements.setHeight(treeGrid.getHeight());
		requirements.addChild(treeGrid);

		
		getHeading().setContents("Requirements");
		getInstructions().setContents("Define your Requirements.");
		getPostit().setContents("Define in this step your requirements to the alternatives. The alternatives which do not fullfil your requirements are filtered out.");

		getContent().addMember(requirements);
	}

	@Override
	public void goBack() {
		
	}

	@Override
	public void goNext() {
		AotearoaSmart.aotearoaService.storeDecision(AotearoaSmart.decision,
				new AsyncCallback<Decision>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(Decision result) {
						AotearoaSmart.updateDecision(result);
						System.out.println("id: " + result.getId());
						System.out.println("print: " + result);
					}
				});
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void asTemplate() {
		// TODO Auto-generated method stub
		
	}

}
