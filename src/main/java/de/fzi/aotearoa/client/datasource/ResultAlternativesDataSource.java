/**
 * 
 */
package de.fzi.aotearoa.client.datasource;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.values.EvaluationResult;

/**
 * @author mugglmenzel
 * 
 */
public class ResultAlternativesDataSource {

	public ListGridRecord[] createListGridRecords(EvaluationResult result) {

		ListGridRecord[] results = new ListGridRecord[result.getDecision().getAlternatives()
				.size()];

		int i = 0;

		for (Alternative alt : result.getResultMultiplicativeIndexMap().keySet()) {
			results[i] = new ListGridRecord();
			results[i].setAttribute("Name", alt.getName());
			// result[i].setAttribute("id", alt.getId());
			results[i].setAttribute("MultiplicativeIndexValue", result.getResultMultiplicativeIndexMap().get(alt));
			results[i].setAttribute("AdditiveIndexValue", result.getResultAdditiveIndexMap().get(alt));
			results[i].setAttribute("PositiveGoalsValue", result.getResultPositiveGoalsMap().get(alt));
			results[i].setAttribute("NegativeGoalsValue", result.getResultNegativeGoalsMap().get(alt));
			i++;
		}

		return results;

	}

}
