package de.eorganization.hoopla.client.smartView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

public class NewDecisionView extends AbstractView {

	DynamicForm form = new DynamicForm();
	TextItem decisionNameItem = new TextItem();
	TextAreaItem decisionDescrItem = new TextAreaItem();
	// add Wang
	TextAreaItem decisionCommItem = new TextAreaItem();

	/**
	 * 
	 */
	public NewDecisionView() {
		super(true, true, false);

		getHeading().setContents("1. New Cloud Decision");
		getInstructions().setContents("Create a new Cloud decision.");
		getPostit()
				.setContents(
						"In the Hoopla you can create a new Cloud decision by using the form on the left. Simply choose a proper name to describe your Cloud project. You can also add a description to explain the details of your project and decision.");

		decisionNameItem.setTitle("Name");
		decisionNameItem.setRequired(true);
		decisionNameItem.setWidth(400);
		decisionNameItem.setSelectOnFocus(true);

		decisionDescrItem.setTitle("Description");
		decisionDescrItem.setTitleVAlign(VerticalAlignment.TOP);
		decisionDescrItem.setRequired(false);
		decisionDescrItem.setHeight(300);
		decisionDescrItem.setWidth(400);

		// add Wang
		decisionCommItem.setTitle("Comment");
		decisionCommItem.setTitleVAlign(VerticalAlignment.TOP);
		decisionCommItem.setRequired(false);
		decisionCommItem.setHeight(150);
		decisionCommItem.setWidth(400);

		// edit Wang
		form.setFields(new FormItem[] { decisionNameItem, decisionDescrItem,
				decisionCommItem });
		form.setWidth(500);
		form.setLeft(30);
		form.setAutoFocus(true);

		getContent().addMember(form);

		refresh();
	}

	@Override
	public void goNext() {
		if (form.validate()) {
			Hoopla.decision.setName(decisionNameItem.getValueAsString());
			Hoopla.decision.setDescription(decisionDescrItem
					.getValueAsString());
			// add Wang
			Hoopla.decision.setComment(decisionCommItem
					.getValueAsString());

			/*
			 * if (Hoopla.decision.getGoals().isEmpty()) { Goal goal =
			 * new Goal(); goal.setName("Root Goal");
			 * Hoopla.decision.addGoal(goal); }
			 */
			//SC.say("storing decision...");
			Hoopla.hooplaService.storeDecision(Hoopla.decision,
					new AsyncCallback<Decision>() {
						public void onFailure(Throwable error) {
						}

						public void onSuccess(Decision result) {
							Hoopla.updateDecision(result);
						}
					});
		}
	}

	@Override
	public void goBack() {
		// nothing happens here
	}

	@Override
	public void refresh() {
		decisionNameItem.setDefaultValue("Project Webserver - ?");
		decisionNameItem
				.setValue(Hoopla.decision != null
						&& Hoopla.decision.getName() != null ? Hoopla.decision
						.getName() : "Project Webserver - ?");
		decisionDescrItem.setDefaultValue("In this project...");
		decisionDescrItem
				.setValue(Hoopla.decision != null
						&& Hoopla.decision.getDescription() != null ? Hoopla.decision
						.getDescription() : "In this project...");
		// add Wang
		decisionCommItem.setDefaultValue("No comment yet");
		decisionCommItem
				.setValue(Hoopla.decision != null
						&& Hoopla.decision.getComment() != null ? Hoopla.decision
						.getComment() : "No comment yet");
	}

	@Override
	public void asTemplate() {
	}

}
