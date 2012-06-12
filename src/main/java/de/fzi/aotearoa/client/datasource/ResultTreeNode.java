package de.fzi.aotearoa.client.datasource;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ResultTreeNode extends TreeNode {
	
	public ResultTreeNode(String id, String name, String description, Double weight, Double value) {  
        setId(id);
		setName(name);
        setDescription(description);
        setWeight(weight);
        setValue(value);
        setParentId(null);
    }  

	public void setId(String value) {  
        setAttribute("Id", value);  
    }  

    public void setName(String value) {  
        setAttribute("Name", value);  
    }  

    public void setDescription(String value) {  
        setAttribute("Description", value);  
    }  
    
    public void setWeight(Double value) {  
        setAttribute("Weight", value);  
    }

    public void setValue(Double value) {  
        setAttribute("Value", value);  
    }

    
    public void setParentId(String value) {
    	if (value == null) {
    		setAttribute("ParentId", "root");
    	} else {
    		setAttribute("ParentId", value);
    	}
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "- Id: " + getAttribute("Id") + ", Name: " + getAttribute("Name") + ", ParentId: " + getAttribute("ParentId") + " -";
	}
    
    
}
