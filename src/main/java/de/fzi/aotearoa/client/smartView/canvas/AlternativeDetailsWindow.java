package de.fzi.aotearoa.client.smartView.canvas;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.client.validators.AlternativeNameValidator;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;

public class AlternativeDetailsWindow extends Window {
	
	DynamicForm form = new DynamicForm();
	IButton saveAltButton = new IButton("Save Alternative");
	TextItem altNameItem = new TextItem();
	TextAreaItem altDescriptionItem = new TextAreaItem();
	AlternativesListGrid alternativesList;
	
	/**
	 * Constructor for creating a new alternative.
	 * @param alternativesList
	 */
	public AlternativeDetailsWindow(AlternativesListGrid alternativesList) {
		this.alternativesList = alternativesList;
		createWindowLayout();
		createSaveButtonHandler(null);
	}
	/**
	 * Constructor for updating an existing alternative.
	 * @param alternativeName
	 * @param alternativesList
	 */
	public AlternativeDetailsWindow(String alternativeName, AlternativesListGrid alternativesList) {
		this.alternativesList = alternativesList;
		createWindowLayout();
		Alternative updatedAlt = AotearoaSmart.decision.getAlternative(alternativeName);
		createSaveButtonHandler(updatedAlt);
		altNameItem.setValue(updatedAlt.getName());
		altDescriptionItem.setValue(updatedAlt.getDescription());
	}
	
	private void createWindowLayout() {
		
		setWidth(450);
		setHeight(270);
		setTitle("Alternative Details");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				destroy();
			}
		});

		altNameItem.setTitle("Name");
		altNameItem.setRequired(true);
		altNameItem.setWidth(300);
		AlternativeNameValidator nameValidator = new AlternativeNameValidator();
		nameValidator.setErrorMessage("Alternative name already exists.");
		altNameItem.setValidators(nameValidator);
		altNameItem.setSelectOnFocus(true);

		altDescriptionItem.setTitle("Description");
		altDescriptionItem.setRequired(false);
		altDescriptionItem.setWidth(300);
		altDescriptionItem.setHeight(150);
		altDescriptionItem.setTitleVAlign(VerticalAlignment.TOP);
		
		form.setFields(altNameItem, altDescriptionItem);
		form.setAutoFocus(true);
		
		HLayout buttons = new HLayout();
		buttons.setMembersMargin(15);
		buttons.setAlign(Alignment.CENTER);

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		buttons.addMember(saveAltButton);
		buttons.addMember(cancelButton);

		VLayout windowLayout = new VLayout();
		windowLayout.setMargin(10);
		windowLayout.setMembersMargin(15);
		windowLayout.addMember(form);
		windowLayout.addMember(buttons);

		addItem(windowLayout);
		
	}
	
	private void createSaveButtonHandler(final Alternative updatedAlt) {
		
		if (updatedAlt == null) {
			saveAltButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()) {
						Alternative alt = new Alternative();
						alt.setName(altNameItem.getValueAsString());
						alt.setDescription(altDescriptionItem.getValueAsString());
						AotearoaSmart.decision.addAlternative(alt);
						destroy();
						alternativesList.refreshAlternativesListGrid();
					}
				}
			});
		} else {	
			saveAltButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (updatedAlt.getName().equals(
							altNameItem.getValueAsString())) {
						altNameItem.setValidators();
					}
					if (form.validate()) {
						updatedAlt.setName(altNameItem
								.getValueAsString());
						updatedAlt.setDescription(altDescriptionItem
								.getValueAsString());
						destroy();
						alternativesList.refreshAlternativesListGrid();
					}
					if (updatedAlt.getName().equals(
							altNameItem.getValueAsString())) {
						AlternativeNameValidator nameValidator = new AlternativeNameValidator();
						nameValidator
								.setErrorMessage("Alternative name already exists.");
						altNameItem.setValidators(nameValidator);
					}
				}

			});
		}
		
	}

}
