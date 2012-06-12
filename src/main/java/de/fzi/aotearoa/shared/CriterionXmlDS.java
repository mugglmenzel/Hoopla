package de.fzi.aotearoa.shared;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class CriterionXmlDS extends DataSource {
	
	private static CriterionXmlDS instance = null;

	public static CriterionXmlDS getInstance() {
		if (instance == null) {
			instance = new CriterionXmlDS("criteriaDS");
		}
		return instance;
	}
	
	public CriterionXmlDS(String id) {
		
		setID(id);
		setTitleField("name");
		setRecordXPath("/List/criterion");
		DataSourceTextField nameField = new DataSourceTextField("name", "name", 128);
		
		DataSourceTextField typeField = new DataSourceTextField("type", "type", 64);  
		typeField.setValueMap("quantitative" , "qualitative" , "benchmarking", "");
		
		DataSourceIntegerField criterionIdField = new DataSourceIntegerField(
				"criterionId", "Criterion ID");
		criterionIdField.setPrimaryKey(true);
		criterionIdField.setRequired(true);
		
		DataSourceIntegerField isChildOfField = new DataSourceIntegerField(
				"isChildof", "Parent");
		isChildOfField.setRequired(true);
		isChildOfField.setForeignKey(id + ".criterionId");
		isChildOfField.setRootValue("1");
		
		DataSourceBooleanField isSelectedField = new DataSourceBooleanField(
				"isSelected", "");
		
		setFields(nameField, typeField, criterionIdField, isChildOfField, isSelectedField);

		setDataURL("ds/test_data/criteria.data.xml");
		setClientOnly(true);
	}

}
