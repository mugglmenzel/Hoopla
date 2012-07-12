package de.eorganization.hoopla.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

public class RequirementsView extends AbstractView {
	public RequirementsView(){
		super();
		final TreeGrid treeGrid = new TreeGrid();
		treeGrid.setLoadDataOnDemand(false);
		treeGrid.setWidth(500);
		treeGrid.setHeight(400);
		treeGrid.setLeft(30);
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
		Hoopla.hooplaService.storeDecision(Hoopla.decision,
				new AsyncCallback<Decision>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(Decision result) {
						Hoopla.updateDecision(result);
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
