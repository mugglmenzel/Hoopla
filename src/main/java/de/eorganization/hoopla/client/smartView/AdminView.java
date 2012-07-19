package de.eorganization.hoopla.client.smartView;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.smartView.canvas.XMLTemplateUploadWindow;
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;

public class AdminView extends AbstractView {

	private final static ListGrid templateList = new ListGrid();

	public AdminView() {
		super(false, false, false);
		getHeading().setContents("Admin");
		getInstructions().setContents(
				"Tweak Hoopla.");
		getPostit()
				.setContents(
						"You are THE Admin, you know what to do!");

		getNavigation().setVisible(false);

		VLayout content = new VLayout();
		
		IButton uploadButton = new IButton("Upload Cloud Infrastructure Decision Template");
		uploadButton.setAutoFit(true);
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new XMLTemplateUploadWindow(1).show();
			}

		});
		
		Label templateInfoLabel = new Label("<span style=\"font-size: 10pt; font-style: bold\">Templates:<br/>1 = Cloud Infrastructure Template<br/>2 = Cloud Provider Template ");
		
		templateList.setWidth(500);
		templateList.setHeight(300);
		templateList.setShowAllRecords(true);
		templateList.setSelectionType(SelectionStyle.SINGLE);
		ListGridField templateIdField = new ListGridField("id", "ID");
		ListGridField templateKeyField = new ListGridField("key", "Key");
		ListGridField templateNameField = new ListGridField("name", "Name");
		templateList.setFields(templateIdField, templateKeyField, templateNameField);
		templateList.setCanResizeFields(true);


		content.addMember(uploadButton);
		content.addMember(templateInfoLabel);
		content.addMember(templateList);
		getContent().addMember(content);

		updateDecisionTemplates();
	}

	private void updateDecisionTemplates() {

		Hoopla.hooplaService.getDecisionTemplates(new AsyncCallback<List<DecisionTemplate>>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(List<DecisionTemplate> result) {
				ListGridRecord[] res = new ListGridRecord[result.size()];
				int i = 0;

				for (DecisionTemplate dc : result) {
					res[i] = new ListGridRecord();
					res[i].setAttribute("name", dc.getName());
					res[i].setAttribute("id", dc.getId());
					res[i].setAttribute("key", dc.getKeyId());
					res[i].setAttribute("decision", dc);
					i++;
					templateList.setData(res);

				}
			}
		});

	}

	@Override
	public void goBack() {
	}

	@Override
	public void goNext() {
	}

	@Override
	public void refresh() {
		updateDecisionTemplates();
	}

	@Override
	public void asTemplate() {
	}

}