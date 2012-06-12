package de.fzi.aotearoa.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;

public class CriterionNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {

		String criterionName = (String) getFormItem().getValue();
		for (Goal g : AotearoaSmart.decision.getGoals())
			for (Criterion crit : g.getAllDescendants()) {
				if (crit.getName().equals(criterionName))
					return false;
			}
		return true;

	}

}
