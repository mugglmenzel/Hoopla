package de.eorganization.hoopla.client.smartView.canvas;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class XMLTemplateUploadWindow extends Window {

	private int substituteTemplateId = 1;
	private DynamicForm uploadForm;
	private UploadItem fileItem;

	public XMLTemplateUploadWindow(int substituteTemplateId) {
		this.substituteTemplateId = substituteTemplateId;
		createWindowLayout();
	}

	private void createWindowLayout() {

		setWidth(400);
		setHeight(120);
		setTitle("Upload XML Decision Template");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				destroy();
			}
		});

		uploadForm = new DynamicForm();
		uploadForm.setAction(GWT.getModuleBaseURL() + "import");
		uploadForm.setMethod(FormMethod.POST);
		uploadForm.setEncoding(Encoding.MULTIPART);

		HiddenItem templateIdItem = new HiddenItem("substituteTemplateId");
		templateIdItem.setValue(substituteTemplateId);
		fileItem = new UploadItem();
		fileItem.setTitle("Template");
		fileItem.setWidth(300);
		uploadForm.setItems(fileItem, templateIdItem);

		IButton uploadButton = new IButton("Upload");
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				Object obj = fileItem.getDisplayValue();
				if (obj != null) {
					uploadForm.submitForm();
				} else
					SC.say("Please select a XML file.");
			}
		});

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout buttons = new HLayout();
		buttons.setMembersMargin(15);
		buttons.setAlign(Alignment.CENTER);

		buttons.addMember(uploadButton);
		buttons.addMember(cancelButton);

		VLayout windowLayout = new VLayout();
		windowLayout.setMargin(10);
		windowLayout.setMembersMargin(15);
		windowLayout.addMember(uploadForm);
		windowLayout.addMember(buttons);

		addItem(windowLayout);
	}

}
