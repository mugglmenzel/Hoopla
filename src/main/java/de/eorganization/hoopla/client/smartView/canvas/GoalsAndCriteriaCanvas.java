package de.eorganization.hoopla.client.smartView.canvas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.validators.GoalNameValidator;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;
import de.eorganization.hoopla.shared.model.ahp.configuration.GoalType;

public class GoalsAndCriteriaCanvas extends Canvas {

	private TabSet goalsTabSet = new TabSet();
	private final IButton addGoalButton = new IButton("Add Goal");
	private final IButton editGoalButton = new IButton("Edit Goal");
	private final IButton removeGoalButton = new IButton("Delete Goal");
	private final IButton addCriterionButton = new IButton("Add Criterion");
	private Map<String, CriteriaTreeGrid> critTreeMap = new HashMap<String, CriteriaTreeGrid>();
	private VLayout mainLayout = new VLayout();
	private boolean isEditable;
	private int customWidth = 0;
	private int customHeight = 0;

	public GoalsAndCriteriaCanvas(boolean isEditable) {

		this.isEditable = isEditable;

		goalsTabSet.setTabBarPosition(Side.TOP);
		// goalsTabSet.setEdgeMarginSize(0);
		// goalsTabSet.setEdgeOffset(0);
		// goalsTabSet.setBackgroundColor("red");

		mainLayout.setMembersMargin(10);
		if (isEditable) {
			mainLayout.addMember(createButtons());
		}
		mainLayout.addMember(goalsTabSet);

		addChild(mainLayout);
		refresh();
	}

	private Window createGoalDetailsWindow(final String goalName) {

		final Window goalWindow = new Window();
		goalWindow.setWidth(450);
		goalWindow.setHeight(300);
		goalWindow.setTitle("Goal Details");
		goalWindow.setShowMinimizeButton(false);
		goalWindow.setIsModal(true);
		goalWindow.setShowModalMask(true);
		goalWindow.centerInPage();
		goalWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				goalWindow.destroy();
			}
		});

		final DynamicForm form = new DynamicForm();

		final TextItem goalNameItem = new TextItem();
		goalNameItem.setTitle("Name");
		goalNameItem.setRequired(true);
		goalNameItem.setWidth(300);
		GoalNameValidator nameValidator = new GoalNameValidator();
		nameValidator.setErrorMessage("Goal name already exists.");
		goalNameItem.setValidators(nameValidator);
		goalNameItem.setSelectOnFocus(true);

		final TextAreaItem goalDescriptionItem = new TextAreaItem();
		goalDescriptionItem.setTitle("Description");
		goalDescriptionItem.setRequired(false);
		goalDescriptionItem.setWidth(300);
		goalDescriptionItem.setHeight(150);
		goalDescriptionItem.setTitleVAlign(VerticalAlignment.TOP);

		final SelectItem goalTypeItem = new SelectItem();
		goalTypeItem.setTitle("Type");
		goalTypeItem.setRequired(true);
		GoalType[] goalTypes = GoalType.values();
		String[] goalTypeNames = new String[goalTypes.length];
		for (int i = 0; i < goalTypes.length; i++) {
			goalTypeNames[i] = goalTypes[i].toString();
		}
		goalTypeItem.setValueMap(goalTypeNames);
		goalTypeItem.setValue(goalTypeNames[0]);
		form.setFields(goalNameItem, goalDescriptionItem, goalTypeItem);
		form.setAutoFocus(true);

		HLayout buttons = new HLayout();
		buttons.setMembersMargin(15);
		buttons.setAlign(Alignment.CENTER);

		IButton saveGoalButton = new IButton("Save Goal");

		if (goalName != null) {
			final Goal goal = Hoopla.decision.getGoal(goalName);
			goalNameItem.setValue(goal.getName());
			goalDescriptionItem.setValue(goal.getDescription());
			goalTypeItem.setValue(goal.getGoalType().toString());

			saveGoalButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (goalName.equals(goalNameItem.getValueAsString())) {
						goalNameItem.setValidators();
					}
					if (form.validate()) {
						goal.setName(goalNameItem.getValueAsString());
						goal.setDescription(goalDescriptionItem
								.getValueAsString());
						goal.setGoalType(GoalType.valueOf(goalTypeItem
								.getDisplayValue().toUpperCase()));
						goalWindow.destroy();
						refresh();
					}
					if (goalName.equals(goalNameItem.getValueAsString())) {
						GoalNameValidator nameValidator = new GoalNameValidator();
						nameValidator
								.setErrorMessage("Goal name already exists.");
						goalNameItem.setValidators(nameValidator);
					}
				}
			});

		} else {
			saveGoalButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()) {
						Goal goal = new Goal();
						goal.setName(goalNameItem.getValueAsString());
						goal.setDescription(goalDescriptionItem
								.getValueAsString());
						goal.setGoalType(GoalType.valueOf(goalTypeItem
								.getDisplayValue().toUpperCase()));
						Hoopla.decision.addGoal(goal);
						goalWindow.destroy();
						refresh();
					}
				}
			});
		}

		IButton cancelButton = new IButton("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goalWindow.destroy();
			}
		});

		buttons.addMember(saveGoalButton);
		buttons.addMember(cancelButton);

		VLayout windowLayout = new VLayout();
		windowLayout.setMargin(10);
		windowLayout.setMembersMargin(15);
		windowLayout.addMember(form);
		windowLayout.addMember(buttons);

		goalWindow.addItem(windowLayout);

		return goalWindow;
	}

	public void refresh() {
		refreshGoalsTabSet();
		refreshSize();
		handleButtons();
	}

	private void refreshSize() {
		if (customWidth > 0 || customHeight > 0) {
			setCustomSize(customWidth, customHeight);
			setAutoWidth();
			setAutoHeight();
		} else {
			goalsTabSet.setWidth100();
			//goalsTabSet.setWidth(500);
			goalsTabSet.setHeight100();
		}

		markForRedraw();
		redraw();
	}

	private void refreshGoalsTabSet() {

		critTreeMap.clear();
		for (Tab tab : goalsTabSet.getTabs()) {
			goalsTabSet.removeTab(tab);
		}
		List<Goal> allGoals = Hoopla.decision.getGoals();
		if (allGoals.size() > 0) {
			for (Goal goal : allGoals) {
				Tab tab = new Tab(goal.getName());
				// ID darf dann nicht 1 sein in JS
				// tab.setID(goal.getName());
				VLayout tabLayout = new VLayout();
				// tabLayout.addMember(createCriterionButtons());
				CriteriaTreeGrid tree = new CriteriaTreeGrid(goal, isEditable);
				// tree.setBorder("1px solid grey");
				critTreeMap.put(goal.getName(), tree);
				tabLayout.addMember(tree);
				tree.refreshCriteriaTree();
				tab.setPane(tabLayout);
				this.goalsTabSet.addTab(tab);
			}
		}
	}

	private void handleButtons() {
		if (Hoopla.decision.getGoals().size() > 0) {
			editGoalButton.enable();
			removeGoalButton.enable();
			addCriterionButton.enable();
		} else {
			editGoalButton.disable();
			removeGoalButton.disable();
			addCriterionButton.disable();
		}
	}

	private HLayout createButtons() {

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setMembersMargin(15);

		addGoalButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createGoalDetailsWindow(null).show();
			}
		});

		editGoalButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createGoalDetailsWindow(goalsTabSet.getSelectedTab().getTitle())
						.show();
			}
		});

		removeGoalButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final String goalName = goalsTabSet.getSelectedTab().getTitle();
				SC.confirm("Do you really want to delete " + goalName
						+ " and all its children criteria?",
						new BooleanCallback() {
							public void execute(Boolean value) {
								if (value != null && value) {
									Hoopla.decision.deleteGoal(goalName);
									refresh();
									handleButtons();
								}
							}
						});
			}
		});

		addCriterionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String goalName = goalsTabSet.getSelectedTab().getTitle();
				new CriterionDetailsWindow(goalName, critTreeMap.get(goalName))
						.show();
				// createCriterionDetailsWindow(null, null).show();
			}
		});

		handleButtons();

		buttonsLayout.addMember(addGoalButton);
		buttonsLayout.addMember(editGoalButton);
		buttonsLayout.addMember(removeGoalButton);
		buttonsLayout.addMember(addCriterionButton);

		return buttonsLayout;
	}

	public void changeLeftDistance(int left) {
		mainLayout.setLeft(left);
	}

	public void setCustomSize(int width, int height) {
		this.customWidth = width;
		this.customHeight = height;
		goalsTabSet.setWidth(width);
		goalsTabSet.setHeight(height);
		setAutoWidth();
		setAutoHeight();
	}

}
