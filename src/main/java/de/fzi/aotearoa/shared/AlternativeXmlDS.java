package de.fzi.aotearoa.shared;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.*;

public class AlternativeXmlDS extends DataSource {

    private static AlternativeXmlDS instance = null;

    public static AlternativeXmlDS getInstance() {
        if (instance == null) {
            instance = new AlternativeXmlDS("supplyItemDS");
        }
        return instance;
    }

    public AlternativeXmlDS(String id) {

        setID(id);
        setRecordXPath("/List/alternative");
        DataSourceIntegerField pkField = new DataSourceIntegerField("itemID");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField itemNameField = new DataSourceTextField("name", "Alternative", 128, true);
        DataSourceTextField skuField = new DataSourceTextField("value", "weight", 10, true);

        DataSourceTextField goal1Field = new DataSourceTextField("goal1", "Goal", 10, true);
        DataSourceTextField valueg1Field = new DataSourceTextField("valueg1", "weight", 10, true);
        DataSourceTextField goal2Field = new DataSourceTextField("goal2", "Goal", 10, true);
        DataSourceTextField valueg2Field = new DataSourceTextField("valueg2", "weight", 10, true);
        DataSourceTextField goal3Field = new DataSourceTextField("goal3", "Goal", 10, true);
        DataSourceTextField valueg3Field = new DataSourceTextField("valueg3", "weight", 10, true);

        setFields(pkField, itemNameField, skuField, goal1Field, valueg1Field, goal2Field, valueg2Field, goal3Field, valueg3Field);

        setDataURL("ds/test_data/alternatives.data.xml");
        setClientOnly(true);        
    }
}

