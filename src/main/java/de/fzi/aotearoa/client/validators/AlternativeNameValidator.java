package de.fzi.aotearoa.client.validators;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;

public class AlternativeNameValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {
		
		String alternativeName = (String) getFormItem().getValue();
		
		boolean alternativeExists = false;
		
		for (Alternative alt : AotearoaSmart.decision.getAlternatives()) {
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
