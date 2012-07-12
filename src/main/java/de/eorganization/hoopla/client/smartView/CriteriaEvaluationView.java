package de.eorganization.hoopla.client.smartView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.smartView.canvas.GoalsAndCriteriaCanvas;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;
import de.eorganization.hoopla.shared.model.ahp.values.CriterionImportance;
import de.eorganization.hoopla.shared.model.ahp.values.GoalImportance;

public class CriteriaEvaluationView extends AbstractView {

	private static Logger log = Logger.getLogger(CriteriaEvaluationView.class.getName());
	
	private GoalsAndCriteriaCanvas goalsAndCriteria;

	public CriteriaEvaluationView() {
		super();

		getHeading().setContents("4. Criteria Evaluation");
		getInstructions().setContents("Set Importance of Criteria.");
		getPostit()
				.setContents(
						"The Hoopla supports defining a specific importance for all goals and criteria to reflect the complexity of a Cloud decision precisely. To support the user with the definition of the decision's importance preferences the Hoopla uses pairwise comparisons.");

		goalsAndCriteria = new GoalsAndCriteriaCanvas(false);
		goalsAndCriteria.changeLeftDistance(30);
		goalsAndCriteria.setCustomSize(500, 400);
		
		refresh();
		// Hoopla.decisionUpdatedSubject.addObserver(this);
	}

	private void generateContent() {

		Label goalsHeader = new Label("Goals:");
		goalsHeader.setAutoHeight();
		goalsHeader.setMargin(5);
		goalsHeader.setStyleName("formheader");

		List<List<Criterion>> criteriaByLevel = Hoopla.decision
				.getCriteriaByLevels();

		List<Canvas> goalSliders = new ArrayList<Canvas>();

		List<Goal> goals = Hoopla.decision.getGoals();
		
		List<GoalImportance> goalImportances = Hoopla.decision.getImportanceGoals();
		
		log.info("importance goals available: " + goalImportances);

		if (goals.size() > 1)
			for (int i = 0; i < goals.size() - 1; i++)
				for (int j = i + 1; j < goals.size(); j++) {

					GoalImportance imp = Hoopla.decision
							.getImportanceGoal(i, j);

					if (imp == null) {

						imp = new GoalImportance(i, j, 0D, "");
						Hoopla.decision.insertImportanceGoal(imp);

					}

					//Edit Wang
					final GoalImportance importance = imp;
					Slider hSlider = new Slider(/*goals.get(i).getName()
							+ " vs. " + goals.get(j).getName()*/ " ");
					hSlider.setPrompt(goals.get(i).getDescription()
							+ "\n<br/>--- vs. ---<br/>\n" + goals.get(j).getDescription());
					hSlider.setVertical(false);
					hSlider.setMinValue(-9);
					hSlider.setMaxValue(9);
					hSlider.setNumValues(19);
					hSlider.setValue(importance != null ? importance
							.getComparisonAToB().floatValue() : 0f);
					hSlider.setMinValueLabel(goals.get(i).getName());   //add
					hSlider.setMaxValueLabel(goals.get(j).getName());   //add
					hSlider.setWidth(500);
					hSlider.setHeight(50);
					hSlider.setLabelWidth(150);
					hSlider.setLeft(30);
					//hSlider.setShowRange(false);
					hSlider.setShowValue(false);

					hSlider.addValueChangedHandler(new ValueChangedHandler() {

						@Override
						public void onValueChanged(ValueChangedEvent event) {
							importance.setComparisonAToB(new Double(event
									.getValue()));
							
							Hoopla.decision.insertImportanceGoal(
									importance);
						}
					});

					TextArea comment = new TextArea();
					comment.setText(importance.getComment());
					
					comment.addValueChangeHandler(new ValueChangeHandler<String>() {

						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							importance.setComment(event.getValue());
							Hoopla.decision.insertImportanceGoal(
									importance);
						}
					});

					HLayout sliderLayout = new HLayout();
					sliderLayout.addMember(hSlider);
					sliderLayout.addMember(comment);
					goalSliders.add(sliderLayout);
				}

		Label criteriaHeader = new Label("Criteria:");
		criteriaHeader.setAutoHeight();
		criteriaHeader.setMargin(5);
		criteriaHeader.setStyleName("formheader");

		List<Canvas> critsSliders = new ArrayList<Canvas>();
		List<List<Criterion>> leafsLists = new ArrayList<List<Criterion>>(
				criteriaByLevel);
		for (List<Criterion> leafs : leafsLists) {
			List<Criterion> crits = new ArrayList<Criterion>(leafs);
			crits.removeAll(Hoopla.decision.getGoals());
			if (crits.size() > 1)
				for (int i = 0; i < crits.size() - 1; i++)
					for (int j = i + 1; j < crits.size(); j++) {

						final List<Criterion> finalCrits = crits;
						final int finalI = i;

						CriterionImportance imp = Hoopla.decision
								.getParentCriterion(crits.get(i).getName())
								.getImportanceChild(i, j);
						if (imp == null) {
							imp = new CriterionImportance(i, j, 0D, "");
							Hoopla.decision
									.getParentCriterion(crits.get(i).getName())
									.getImportanceChildren().add(imp);
						}

						//Edit Wang
						final CriterionImportance importance = imp;
						Slider hSlider = new Slider(/*crits.get(i)
								.getName() + " vs. " + crits.get(j).getName()*/ " ");
						hSlider.setPrompt(crits.get(i).getDescription()
								+ "\n<br/>--- vs. ---<br/>\n" + crits.get(j).getDescription());
						hSlider.setVertical(false);
						hSlider.setMinValue(-9);
						hSlider.setMaxValue(9);
						hSlider.setNumValues(19);
						hSlider.setValue(imp != null ? imp.getComparisonAToB()
								.floatValue() : 0f);
						hSlider.setMinValueLabel(crits.get(i).getName());   //add
						hSlider.setMaxValueLabel(crits.get(j).getName());   //add
						hSlider.setWidth(500);
						hSlider.setHeight(50);
						hSlider.setLabelWidth(150);
						hSlider.setLeft(30);
						//hSlider.setShowRange(false);
						hSlider.setShowValue(false);

						hSlider.addValueChangedHandler(new ValueChangedHandler() {

							@Override
							public void onValueChanged(ValueChangedEvent event) {
								importance.setComparisonAToB(new Double(event
										.getValue()));
								Hoopla.decision
										.getParentCriterion(
												finalCrits.get(finalI)
														.getName())
										.insertImportanceChild(importance);
							}
						});

						TextArea comment = new TextArea();
						comment.setText(importance.getComment());
						comment.addValueChangeHandler(new ValueChangeHandler<String>() {

							@Override
							public void onValueChange(
									ValueChangeEvent<String> event) {
								importance.setComment(event.getValue());
								Hoopla.decision
										.getParentCriterion(
												finalCrits.get(finalI)
														.getName())
										.insertImportanceChild(importance);
							}
						});

						HLayout sliderLayout = new HLayout();
						sliderLayout.addMember(hSlider);
						sliderLayout.addMember(comment);
						critsSliders.add(sliderLayout);
					}
		}

		// Canvas criteria = new Canvas();
		// criteria.setHeight(treeGrid.getHeight());
		// criteria.addChild(treeGrid);

		getContent().addMember(goalsAndCriteria);
		getContent().addMember(goalsHeader);
		for (Canvas hSliderCanvas : goalSliders)
			getContent().addMember(hSliderCanvas);
		getContent().addMember(criteriaHeader);
		for (Canvas hSliderCanvas : critsSliders)
			getContent().addMember(hSliderCanvas);

	}

	@Override
	public void goNext() {
		Hoopla.hooplaService.storeDecision(Hoopla.decision,
				new AsyncCallback<Decision>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(Decision result) {
						Hoopla.updateDecision(result);
					}
				});
	}

	@Override
	public void goBack() {

	}

	@Override
	public void refresh() {
		getContent().removeMembers(getContent().getMembers());
		generateContent();

		goalsAndCriteria.refresh();
	}

	@Override
	public void asTemplate() {
		// TODO Auto-generated method stub

	}

}
