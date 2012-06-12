package de.fzi.aotearoa.shared;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class RequirementsXmlDS extends DataSource {
	private static RequirementsXmlDS instance = null;

	public static RequirementsXmlDS getInstance(){
	if (instance == null) {
		instance = new RequirementsXmlDS("requirementsDS");
	}
	return instance;
}

	
	public RequirementsXmlDS(String id){

		setID(id);
		setTitleField("name");
		setRecordXPath("/List/requirements");
		DataSourceTextField nameField = new DataSourceTextField("name", "name", 128);
		
		DataSourceTextField typeField = new DataSourceTextField("type", "type", 64);  
		typeField.setValueMap("quantitative" , "qualitative" , "benchmarking", "");
		
		DataSourceIntegerField requirementsIdField = new DataSourceIntegerField(
				"requirementId", "Requirements ID");
		requirementsIdField.setPrimaryKey(true);
		requirementsIdField.setRequired(true);
		
		DataSourceIntegerField isChildOfField = new DataSourceIntegerField(
				"isChildof", "Parent");
		isChildOfField.setRequired(true);
		isChildOfField.setForeignKey(id + ".requirementId");
		isChildOfField.setRootValue("1");
		
		DataSourceBooleanField isSelectedField = new DataSourceBooleanField(
				"isSelected", "");
		
		setFields(nameField, typeField, requirementsIdField, isChildOfField, isSelectedField);

		setDataURL("ds/test_data/requirements.data.xml");
		setClientOnly(true);
	}
}
