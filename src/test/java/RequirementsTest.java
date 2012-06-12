import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.attribute.ECloudDecisionAttribute;
import de.fzi.aotearoa.shared.model.ahp.configuration.requirement.MaxRequirement;
import de.fzi.aotearoa.shared.model.ahp.configuration.requirement.MinRequirement;
import de.fzi.aotearoa.shared.model.ahp.configuration.requirement.Requirement;
import de.fzi.aotearoa.shared.model.ahp.configuration.requirement.RequirementItem;

public class RequirementsTest {

	@Test
	public void ReqTest() {
		System.out.println("Testing requirements");
		Decision d = new Decision("Xin's Decision");
		Requirement<Double> minReq = new MinRequirement<Double>(
				"minimal Performance Value",
				ECloudDecisionAttribute.PERFORMANCE,
				new RequirementItem<Double>(200D));
		d.getRequirements().add(minReq);
		Requirement<Double> maxReq = new MaxRequirement<Double>(
				"max Pref Value", ECloudDecisionAttribute.COSTPERHOUR, new RequirementItem<Double>(200D));

		System.out.println("requirements of " + d.getName() + ": "
				+ d.getRequirements());
		assertEquals(maxReq, d.getRequirements().get(0));
	}

}
