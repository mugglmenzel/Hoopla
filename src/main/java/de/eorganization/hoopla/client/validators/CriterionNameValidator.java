package de.eorganization.hoopla.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;

public class CriterionNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {

		String criterionName = (String) getFormItem().getValue();
		for (Goal g : Hoopla.decision.getGoals())
			for (Criterion crit : g.getAllDescendants()) {
				if (crit.getName().equals(criterionName))
					return false;
			}
		return true;

	}

}
