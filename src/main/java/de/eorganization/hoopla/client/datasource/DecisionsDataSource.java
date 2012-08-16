package de.eorganization.hoopla.client.datasource;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

public class DecisionsDataSource {
	
	public ListGridRecord[] createListGridRecords() {
		
		Hoopla.hooplaService.getDecisions(Hoopla.loginInfo.getMember().getEmail(), new AsyncCallback<List<Decision>>() {
			
			public void onSuccess(List<Decision> result) {
				
			}
			
			public void onFailure(Throwable caught) {
				
			}
		});
		
		return null;
		
	}
}