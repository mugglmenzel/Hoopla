package de.fzi.aotearoa.client.datasource;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;

public class DecisionsDataSource {
	
	public ListGridRecord[] createListGridRecords() {
		
		AotearoaSmart.aotearoaService.getDecisions(new AsyncCallback<List<Decision>>() {
			
			public void onSuccess(List<Decision> result) {
				
			}
			
			public void onFailure(Throwable caught) {
				
			}
		});
		
		return null;
		
	}
}