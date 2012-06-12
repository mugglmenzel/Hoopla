package de.fzi.aotearoa.client.datasource;

import java.util.List;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;

public class AlternativesDataSource {
	
	public ListGridRecord[] createListGridRecords() {
		
		List<Alternative> alternatives = AotearoaSmart.decision.getAlternatives();
			
		ListGridRecord[] result = new ListGridRecord[alternatives.size()];
		
		int i = 0;
		
		for (Alternative alt : alternatives) {
			result[i] = new ListGridRecord();
			result[i].setAttribute("name", alt.getName());
			result[i].setAttribute("id", alt.getId());
			//result[i].setAttribute("value", alt.getIndexResult());
			i++;
		}
		
		return result;
	}
}