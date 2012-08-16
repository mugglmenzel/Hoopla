package de.eorganization.hoopla.client.smartView.canvas;

import java.util.List;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.datasource.AlternativesDataSource;
import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;

public class AlternativesListGrid extends ListGrid {
	
	private HLayout rollOverCanvas;
	private ListGridRecord rollOverRecord;
	
	public AlternativesListGrid() {
		
		setShowRollOverCanvas(true);
		setWidth(500);
		setHeight(300);
		setShowAllRecords(true);
		// alternativesList.setAutoFetchData(true);
		setSelectionType(SelectionStyle.SINGLE);

		ListGridField alternativeNameField = new ListGridField("name", "Name");
		setFields(alternativeNameField);
		setCanResizeFields(true);
		
		refreshAlternativesListGrid();
	}
	
	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(50);
			rollOverCanvas.setHeight(22);

			ImgButton editImg = new ImgButton();
			editImg.setShowDown(false);
			editImg.setShowRollOver(false);
			editImg.setLayoutAlign(Alignment.CENTER);
			editImg.setSrc("/images/comment_edit.png");
			editImg.setPrompt("Edit Alternative");
			editImg.setHeight(16);
			editImg.setWidth(16);
			editImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					String name = rollOverRecord.getAttribute("name");
					new AlternativeDetailsWindow(name, AlternativesListGrid.this).show();
				}
			});

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc("/images/delete.png");
			deleteImg.setPrompt("Delete Alternative");
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					// this operation is not save because name
					// is not unique, id would be better but is not set
					// yet
					final String name = rollOverRecord.getAttribute("name");
					SC.confirm("Do you really want to delete "
							+ name + "?",
							new BooleanCallback() {
								public void execute(Boolean value) {
									if (value != null && value) {
										List<Alternative> alternatives = Hoopla.decision
												.getAlternatives();
										Alternative deleteAlt = null;
										for (Alternative alt : alternatives) {
											if (alt.getName().equals(name)) {
												deleteAlt = alt;
											}
										}
										if (deleteAlt != null) {
											alternatives.remove(deleteAlt);
										}
										refreshAlternativesListGrid();
									}
								}
							});
				}
			});

			rollOverCanvas.addMember(editImg);
			rollOverCanvas.addMember(deleteImg);
		}
		return rollOverCanvas;

	}
	
	public void refreshAlternativesListGrid() {
		setData(new AlternativesDataSource().createListGridRecords());
	}

}
