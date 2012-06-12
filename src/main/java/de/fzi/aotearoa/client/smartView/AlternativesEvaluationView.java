package de.fzi.aotearoa.client.smartView;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.client.smartView.canvas.GoalsAndCriteriaCanvas;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.CriterionType;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeImportance;
import de.fzi.aotearoa.shared.model.ahp.values.AlternativeValue;

public class AlternativesEvaluationView extends AbstractView {

	private GoalsAndCriteriaCanvas goalsAndCriteria;

	public AlternativesEvaluationView() {
		super();

		getHeading().setContents("5. Alternatives Evaluation");
		getInstructions().setContents(
				"Assess and evaluate your Alternatives regarding Criteria.");
		getPostit()
				.setContents(
						"Before Aotearoa can compute a final result you have to assess and evaluate all criteria you specified earlier. Some criteria are measurable and a value has to be inserted in the form on the left. Some of the measurable criteria must be benchmarked first by clicking on the blue arrow. If you specified qualitative criteria you can fill in your preferences by using the sliders at the bottom of the form.");

		refresh();
		// AotearoaSmart.decisionUpdatedSubject.addObserver(this);
	}

	private void generateContent() {

		goalsAndCriteria = new GoalsAndCriteriaCanvas(false);
		goalsAndCriteria.changeLeftDistance(30);
		goalsAndCriteria.setCustomSize(500, 400);
		goalsAndCriteria.refresh();
		getContent().addMember(goalsAndCriteria);

		Label quantitativeHeader = new Label("Quantitative:");
		quantitativeHeader.setAutoHeight();
		quantitativeHeader.setMargin(5);
		quantitativeHeader.setStyleName("formheader");
		getContent().addMember(quantitativeHeader);

		List<Criterion> crits = AotearoaSmart.decision
				.getLeafCriteria(CriterionType.QUANTITATIVE);

		if (!crits.isEmpty())
			for (int i = 0; i < AotearoaSmart.decision.getAlternatives().size(); i++) {
				Alternative alt = AotearoaSmart.decision.getAlternatives().get(
						i);

				Label quantitativeAlternativeHeader = new Label(alt.getName());
				quantitativeAlternativeHeader.setPrompt(alt.getDescription());
				quantitativeAlternativeHeader.setAutoHeight();
				quantitativeAlternativeHeader.setMargin(5);
				quantitativeAlternativeHeader.setStyleName("formheader2");
				getContent().addMember(quantitativeAlternativeHeader);

				DynamicForm quantitativeForm = new DynamicForm();

				List<FormItem> formFields = new ArrayList<FormItem>();

				for (Criterion crit : crits) {

					final Criterion finalCrit = crit;

					/*
					 * FormItemIcon costCalculatorIcon = new FormItemIcon();
					 * costCalculatorIcon.setSrc("/cost_calculator.png");
					 * costCalculatorIcon
					 * .setPrompt("Cost Calculator started.");
					 * costCalculatorIcon.setWidth(100);
					 * costCalculatorIcon.setHeight(16);
					 */

					final AlternativeValue av = crit.getValueAlternative(i) != null ? crit
							.getValueAlternative(i) : new AlternativeValue(i,
							crit, 0D, "");

					final SpinnerItem alternativeValue = new SpinnerItem();
					alternativeValue.setTitle(crit.getName());
					alternativeValue.setRequired(true);
					alternativeValue.setPrompt(crit.getDescription());
					alternativeValue.setHint(crit.getMetric());
					
					Double value = av != null ? av.getValue() : null;
					alternativeValue.setDefaultValue(value != null ? value
							.floatValue() : 0f);
					alternativeValue.setStep(0.1f);
					alternativeValue.setWidth(400);
					// investmentCostsAlternative1Value.setIcons(costCalculatorIcon);

					final TextAreaItem alternativeValueDescr = new TextAreaItem();
					alternativeValueDescr.setTitle("Comment for "
							+ crit.getName());
					alternativeValueDescr.setDefaultValue(av != null ? av
							.getDescription() : "");
					alternativeValueDescr.setWidth(400);
					alternativeValueDescr.setHeight(50);

					alternativeValue.addChangedHandler(new ChangedHandler() {

						@Override
						public void onChanged(ChangedEvent event) {
							if (event.getValue() != null
									&& AotearoaSmart.decision
											.getCriterion(finalCrit.getName()) != null) {
								av.setValue(new Double(event.getValue()
										.toString()).doubleValue());
								AotearoaSmart.decision.getCriterion(
										finalCrit.getName())
										.insertValueAlternative(av);
							}
						}
					});

					alternativeValueDescr
							.addChangedHandler(new ChangedHandler() {

								@Override
								public void onChanged(ChangedEvent event) {
									if (event.getValue() != null
											&& AotearoaSmart.decision
													.getCriterion(finalCrit
															.getName()) != null) {
										av.setDescription(event.getValue() != null ? event
												.getValue().toString() : "");
										AotearoaSmart.decision.getCriterion(
												finalCrit.getName())
												.insertValueAlternative(av);
									}
								}
							});
					
					
					
					formFields.add(alternativeValue);
					formFields.add(alternativeValueDescr);
				}

				quantitativeForm.setFields(formFields
						.toArray(new FormItem[crits.size()]));
				quantitativeForm.setWidth(500);
				quantitativeForm.setLeft(30);

				Canvas quantitativeFormCanvas = new Canvas();
				quantitativeFormCanvas.addChild(quantitativeForm);

				getContent().addMember(quantitativeFormCanvas);

			}

		Label benchmarkingHeader = new Label("Benchmarking:");
		benchmarkingHeader.setAutoHeight();
		benchmarkingHeader.setMargin(5);
		benchmarkingHeader.setStyleName("formheader");
		getContent().addMember(benchmarkingHeader);
		crits = AotearoaSmart.decision.getLeafCriteria(CriterionType.BENCHMARK);

		if (!crits.isEmpty())
			for (int i = 0; i < AotearoaSmart.decision.getAlternatives().size(); i++) {
				Alternative alt = AotearoaSmart.decision.getAlternatives().get(
						i);

				Label benchmarkingAlternativeHeader = new Label(alt.getName());
				benchmarkingAlternativeHeader.setPrompt(alt.getDescription());
				benchmarkingAlternativeHeader.setAutoHeight();
				benchmarkingAlternativeHeader.setMargin(5);
				benchmarkingAlternativeHeader.setStyleName("formheader2");
				getContent().addMember(benchmarkingAlternativeHeader);

				DynamicForm benchmarkingForm = new DynamicForm();

				List<FormItem> formFields = new ArrayList<FormItem>();

				for (Criterion crit : crits) {

					final Criterion finalCrit = crit;

					/*
					 * FormItemIcon fii = new FormItemIcon();
					 * fii.setSrc("/benchmarking.png");
					 * fii.setPrompt("Benchmarking triggered.");
					 * fii.setWidth(100); fii.setHeight(16);
					 */

					final AlternativeValue av = crit.getValueAlternative(i) != null ? crit
							.getValueAlternative(i) : new AlternativeValue(i,
							crit, 0D, "");

					final SpinnerItem alternativeValue = new SpinnerItem();
					alternativeValue.setTitle(crit.getName());
					alternativeValue.setRequired(true);
					alternativeValue.setPrompt(crit.getDescription());
					alternativeValue.setHint(crit.getMetric());
					
					Double value = av != null ? av.getValue() : null;
					alternativeValue.setDefaultValue(value != null ? value
							.floatValue() : 0f);
					alternativeValue.setWidth(400);
					// alternativeValue.setIcons(fii);

					final TextAreaItem alternativeValueDescr = new TextAreaItem();
					alternativeValueDescr.setTitle("Comment for "
							+ crit.getName());
					alternativeValueDescr.setDefaultValue(av != null ? av
							.getDescription() : "");
					alternativeValueDescr.setWidth(400);
					alternativeValueDescr.setHeight(50);

					alternativeValue.addChangedHandler(new ChangedHandler() {

						@Override
						public void onChanged(ChangedEvent event) {
							if (event.getValue() != null
									&& AotearoaSmart.decision
											.getCriterion(finalCrit.getName()) != null) {
								av.setValue(new Double(event.getValue()
										.toString()).doubleValue());
								AotearoaSmart.decision.getCriterion(
										finalCrit.getName())
										.insertValueAlternative(av);
							}
						}
					});

					alternativeValueDescr
							.addChangedHandler(new ChangedHandler() {

								@Override
								public void onChanged(ChangedEvent event) {
									if (event.getValue() != null
											&& AotearoaSmart.decision
													.getCriterion(finalCrit
															.getName()) != null) {
										av.setDescription(event.getValue() != null ? event
												.getValue().toString() : "");
										AotearoaSmart.decision.getCriterion(
												finalCrit.getName())
												.insertValueAlternative(av);
									}
								}
							});

					formFields.add(alternativeValue);
					formFields.add(alternativeValueDescr);

				}

				benchmarkingForm.setFields(formFields
						.toArray(new FormItem[crits.size()]));
				benchmarkingForm.setWidth(500);
				benchmarkingForm.setLeft(30);

				Canvas benchmarkingFormCanvas = new Canvas();
				benchmarkingFormCanvas.addChild(benchmarkingForm);
				getContent().addMember(benchmarkingFormCanvas);

			}

		Label qualitativeHeader = new Label("Qualitative:");
		qualitativeHeader.setAutoHeight();
		qualitativeHeader.setMargin(5);
		qualitativeHeader.setStyleName("formheader");
		getContent().addMember(qualitativeHeader);

		crits = AotearoaSmart.decision
				.getLeafCriteria(CriterionType.QUALITATIVE);

		for (Criterion crit : crits) {
			Label qualitativeAlternativeHeader = new Label(crit.getName());
			qualitativeAlternativeHeader.setPrompt(crit.getDescription());
			qualitativeAlternativeHeader.setAutoHeight();
			qualitativeAlternativeHeader.setMargin(5);
			qualitativeAlternativeHeader.setStyleName("formheader2");
			getContent().addMember(qualitativeAlternativeHeader);

			List<Alternative> alts = AotearoaSmart.decision.getAlternatives();

			DynamicForm qualitativeForm = new DynamicForm();

			List<FormItem> formFields = new ArrayList<FormItem>();

			for (int i = 0; i < alts.size() - 1; i++) {
				for (int j = i + 1; j < alts.size(); j++) {
					final Criterion critFinal = crit;
					final SliderItem hSlider = new SliderItem();

					final AlternativeImportance ai = crit
							.getImportanceAlternative(i, j) != null ? crit
							.getImportanceAlternative(i, j)
							: new AlternativeImportance(i, j, crit, 0D, "");

					hSlider.setTitle(alts.get(i) + " vs. " + alts.get(j));
					hSlider.setPrompt(alts.get(i).getDescription() + "\n<br/>--- vs. ---<br/>\n" + alts.get(j).getDescription());
					//hSlider.setHint(alts.get(i).getDescription() + "\n<br/>--- vs. ---<br/>\n" + alts.get(j).getDescription());
					
					hSlider.setVertical(false);
					hSlider.setMinValue(-9);
					hSlider.setMaxValue(9);
					hSlider.setNumValues(19);
					Double importance = ai != null ? ai.getComparisonAToB()
							: null;
					hSlider.setValue(importance != null ? importance
							.floatValue() : 0f);
					hSlider.setWidth(500);
					hSlider.setHeight(50);
					hSlider.setLeft(30);

					final TextAreaItem alternativeImportanceDescr = new TextAreaItem();
					alternativeImportanceDescr.setTitle("Comment");
					alternativeImportanceDescr.setDefaultValue(ai != null ? ai
							.getDescription() : "");
					alternativeImportanceDescr.setHeight(50);
					alternativeImportanceDescr.setWidth(400);

					hSlider.addChangedHandler(new ChangedHandler() {

						@Override
						public void onChanged(ChangedEvent event) {

							if (event.getValue() != null && critFinal != null) {
								ai.setComparisonAToB(new Double(event
										.getValue().toString()).doubleValue());
								AotearoaSmart.decision.getCriterion(
										critFinal.getName())
										.insertImportanceAlternative(ai);
							}
						}
					});

					alternativeImportanceDescr
							.addChangedHandler(new ChangedHandler() {

								@Override
								public void onChanged(ChangedEvent event) {
									if (event.getValue() != null
											&& critFinal != null) {
										ai.setDescription(event.getValue() != null ? event
												.getValue().toString() : "");
										AotearoaSmart.decision
												.getCriterion(
														critFinal.getName())
												.insertImportanceAlternative(ai);
									}
								}
							});

					formFields.add(hSlider);
					formFields.add(alternativeImportanceDescr);

				}
			}
			qualitativeForm.setFields(formFields.toArray(new FormItem[crits
					.size()]));
			qualitativeForm.setWidth(500);
			qualitativeForm.setLeft(30);

			Canvas qualitativeFormCanvas = new Canvas();
			qualitativeFormCanvas.addChild(qualitativeForm);

			getContent().addMember(qualitativeFormCanvas);
		}

	}

	@Override
	public void goNext() {
		AotearoaSmart.aotearoaService.storeDecision(AotearoaSmart.decision,
				new AsyncCallback<Decision>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(Decision result) {
						AotearoaSmart.updateDecision(result);
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

	}

}
