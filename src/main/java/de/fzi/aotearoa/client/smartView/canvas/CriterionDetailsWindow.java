package de.fzi.aotearoa.client.smartView.canvas;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.client.validators.CriterionNameValidator;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.CriterionType;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;

public class CriterionDetailsWindow extends Window {

	DynamicForm form = new DynamicForm();
	SelectItem parentCriterionItem = new SelectItem();
	TextItem criterionNameItem = new TextItem();
	TextAreaItem criterionDescriptionItem = new TextAreaItem();
	TextItem criterionMetricItem = new TextItem();
	SelectItem criterionTypeItem = new SelectItem();
	TextItem criterionURLItem = new TextItem();
	IButton saveCriterionButton = new IButton("Save Criterion");
	CriteriaTreeGrid critTree;

	/**
	 * Constructor for adding a criterion below its goal
	 * 
	 * @param goalName
	 * @param critTree
	 */
	public CriterionDetailsWindow(String goalName, CriteriaTreeGrid critTree) {
		this.critTree = critTree;
		createWindowLayout(goalName);
		createSaveButtonHandler(null, null);
		parentCriterionItem.setValueMap(updateParentCriterionItem(goalName));
		parentCriterionItem.setValue(goalName);
	}

	/**
	 * Constructor for adding a criterion below another criterion
	 * 
	 * @param goalName
	 * @param currentCriterion
	 * @param critTree
	 */
	public CriterionDetailsWindow(String goalName, String currentCriterion,
			CriteriaTreeGrid critTree) {
		this.critTree = critTree;
		createWindowLayout(goalName);
		createSaveButtonHandler(null, null);
		parentCriterionItem.setValueMap(updateParentCriterionItem(goalName));
		parentCriterionItem.setValue(currentCriterion);
	}

	/**
	 * Constructor for updating a specific criterion
	 * 
	 * @param goalName
	 * @param criterionName
	 * @param parentCriterionName
	 * @param critTree
	 */
	public CriterionDetailsWindow(String goalName, String criterionName,
			String parentCriterionName, CriteriaTreeGrid critTree) {
		this.critTree = critTree;
		Criterion crit = AotearoaSmart.decision.getGoal(goalName).getCriterion(
				criterionName);
		Criterion parentCrit;
		if (!goalName.equals(parentCriterionName)) {
			parentCrit = AotearoaSmart.decision.getGoal(goalName).getCriterion(
					parentCriterionName);
		} else {
			parentCrit = AotearoaSmart.decision.getGoal(goalName);
		}
		createWindowLayout(goalName);
		createSaveButtonHandler(crit, parentCrit);
		criterionNameItem.setValue(crit.getName());
		criterionDescriptionItem.setValue(crit.getDescription());
		criterionTypeItem.setValue(crit.getType().toString().toLowerCase());
		parentCriterionItem.setValueMap(updateParentCriterionItemForEdit(
				goalName, criterionName));
		parentCriterionItem.setValue(parentCriterionName);
	}

	private void createWindowLayout(String goalName) {

		setWidth(450);
		setHeight(320);
		setTitle("Criterion Details");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				destroy();
			}
		});

		parentCriterionItem.setTitle("Parent");
		parentCriterionItem.setRequired(true);
		parentCriterionItem.setWidth(300);
		parentCriterionItem.setAddUnknownValues(false);
		parentCriterionItem.setSelectOnFocus(false);

		criterionNameItem.setTitle("Name");
		criterionNameItem.setRequired(true);
		criterionNameItem.setWidth(300);
		CriterionNameValidator nameValidator = new CriterionNameValidator();
		nameValidator.setErrorMessage("Criterion name already exists.");
		criterionNameItem.setValidators(nameValidator);
		criterionNameItem.setSelectOnFocus(true);

		criterionMetricItem.setTitle("Metric");
		criterionMetricItem.setRequired(false);
		criterionMetricItem.setWidth(300);

		criterionDescriptionItem.setTitle("Description");
		criterionDescriptionItem.setRequired(false);
		criterionDescriptionItem.setWidth(300);
		criterionDescriptionItem.setHeight(150);
		criterionDescriptionItem.setTitleVAlign(VerticalAlignment.TOP);

		criterionTypeItem.setTitle("Type");
		criterionTypeItem.setRequired(true);
		CriterionType[] criterionTypes = CriterionType.values();
		String[] criterionTypeNames = new String[criterionTypes.length];
		for (int i = 0; i < criterionTypes.length; i++) {
			criterionTypeNames[i] = criterionTypes[i].toString();
		}
		criterionTypeItem.setValueMap(criterionTypeNames);
		criterionTypeItem.setValue(criterionTypeNames[0]);
		criterionTypeItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if (criterionTypeItem.getValueAsString().equals(
						CriterionType.QUALITATIVE.getTypeName()))
					criterionMetricItem.hide();
				else
					criterionMetricItem.show();
			}
		});

		criterionURLItem.setTitle("URL");
		criterionURLItem.setRequired(false);
		criterionURLItem.setWidth(300);

		form.setFields(parentCriterionItem, criterionNameItem,
				criterionMetricItem, criterionDescriptionItem,
				criterionTypeItem, criterionURLItem);
		form.focusInItem(criterionNameItem);
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

		buttons.addMember(saveCriterionButton);
		buttons.addMember(cancelButton);

		VLayout windowLayout = new VLayout();
		windowLayout.setMargin(10);
		windowLayout.setMembersMargin(15);
		windowLayout.addMember(form);
		windowLayout.addMember(buttons);

		addItem(windowLayout);
	}

	private void createSaveButtonHandler(final Criterion updatedCrit,
			final Criterion parentCrit) {

		if (updatedCrit == null) {
			saveCriterionButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()) {
						Criterion crit = new Criterion();
						crit.setName(criterionNameItem.getValueAsString());
						crit.setDescription(criterionDescriptionItem
								.getValueAsString());
						crit.setMetric(criterionMetricItem.getValueAsString());
						crit.setType(CriterionType.valueOf(criterionTypeItem
								.getDisplayValue().toUpperCase()));
						crit.setUrl(criterionURLItem.getValueAsString());
						Criterion parentCriterion = AotearoaSmart.decision
								.getCriterion(parentCriterionItem
										.getValueAsString());
						parentCriterion.addChild(crit);
						destroy();
						critTree.refreshCriteriaTree();
					}
				}
			});
		} else {
			saveCriterionButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (updatedCrit.getName().equals(
							criterionNameItem.getValueAsString())) {
						criterionNameItem.setValidators();
					}
					if (form.validate()) {
						updatedCrit.setName(criterionNameItem
								.getValueAsString());
						updatedCrit.setDescription(criterionDescriptionItem
								.getValueAsString());
						updatedCrit.setMetric(criterionMetricItem
								.getValueAsString());
						updatedCrit.setType(CriterionType
								.valueOf(criterionTypeItem.getDisplayValue()
										.toUpperCase()));
						updatedCrit.setUrl(criterionURLItem.getValueAsString());
						if (!parentCrit.getName().equals(
								parentCriterionItem.getValueAsString())) {
							AotearoaSmart.decision.getCriterion(
									parentCriterionItem.getValueAsString())
									.addChild(updatedCrit);
							parentCrit.deleteCriterion(updatedCrit.getName());
						}
						destroy();
						critTree.refreshCriteriaTree();
					}
					if (updatedCrit.getName().equals(
							criterionNameItem.getValueAsString())) {
						CriterionNameValidator nameValidator = new CriterionNameValidator();
						nameValidator
								.setErrorMessage("Goal name already exists.");
						criterionNameItem.setValidators(nameValidator);
					}
				}

			});
		}
	}

	public LinkedHashMap<String, String> updateParentCriterionItem(
			String goalName) {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(goalName, "<b>" + goalName + "</b>");
		if (AotearoaSmart.decision.getGoals().size() > 0) {
			Goal goal = AotearoaSmart.decision.getGoal(goalName);
			for (List<Criterion> list : goal.getCriteriaByLevels()) {
				for (Criterion criterion : list) {
					valueMap.put(criterion.getName(), criterion.getName());
				}
			}
		}
		return valueMap;
	}

	public LinkedHashMap<String, String> updateParentCriterionItemForEdit(
			String goalName, String critName) {

		Goal currentGoal = AotearoaSmart.decision.getGoal(goalName);
		Criterion updatedCriterion = currentGoal.getCriterion(critName);

		Map<String, String> notParents = new HashMap<String, String>();
		notParents.put(updatedCriterion.getName(), updatedCriterion.getName());
		if (updatedCriterion.hasChildren()) {
			for (Criterion crit : updatedCriterion.getAllDescendants()) {
				notParents.put(crit.getName(), crit.getName());
			}
		}

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(goalName, "<b>" + goalName + "</b>");
		for (List<Criterion> list : currentGoal.getCriteriaByLevels()) {
			for (Criterion criterion : list) {
				if (!notParents.containsKey(criterion.getName())) {
					valueMap.put(criterion.getName(), criterion.getName());
				}
			}
		}
		return valueMap;
	}

}
