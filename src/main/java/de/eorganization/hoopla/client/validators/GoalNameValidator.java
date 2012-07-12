package de.eorganization.hoopla.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;

public class GoalNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {
		
		String goalName = (String) getFormItem().getValue();
		for (Goal g : Hoopla.decision.getGoals())
			if (g.getName().equals(goalName)) {
				return false;
			}
		return true;
	}

}
