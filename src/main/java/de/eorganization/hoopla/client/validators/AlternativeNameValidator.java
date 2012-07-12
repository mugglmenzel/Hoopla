package de.eorganization.hoopla.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;

public class AlternativeNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {

		String alternativeName = (String) getFormItem().getValue();

		boolean alternativeExists = false;
		if (Hoopla.decision != null && Hoopla.decision.getAlternatives() != null)
			for (Alternative alt : Hoopla.decision.getAlternatives()) {
				if (alt.getName().equals(alternativeName)) {
					alternativeExists = true;
					break;
				}
			}

		if (alternativeExists) {
			return false;
		} else {
			return true;
		}
	}

}
