package de.fzi.aotearoa.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;

public class GoalNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {
		
		String goalName = (String) getFormItem().getValue();
		for (Goal g : AotearoaSmart.decision.getGoals())
			if (g.getName().equals(goalName)) {
				return false;
			}
		return true;
	}

}
