package de.fzi.aotearoa.client.datasource;

import com.smartgwt.client.widgets.tree.TreeNode;

public class CriterionTreeNode extends TreeNode {
	
	public CriterionTreeNode(String id, String name, String description, String type, double localWeight, double globalWeight, String parentName, String parentId) {  
        setID(id);
		setName(name);  
        setDescription(description);  
        setType(type);
        setLocalWeight(localWeight);
        setGlobalWeight(globalWeight);
        setParentName(parentName);
        setParentId(parentId);
    }  

	public void setID(String value) {  
        setAttribute("Id", value);  
    } 
	
    public void setName(String value) {  
        setAttribute("Name", value);  
    }  

    public void setDescription(String value) {  
        setAttribute("Description", value);  
    }  

    public void setType(String value) {  
        setAttribute("Type", value);  
    }
    
    public void setLocalWeight(double value) {  
        setAttribute("LocalWeight", value);  
    }
    
    public void setGlobalWeight(double value) {  
        setAttribute("GlobalWeight", value);  
    }
    
    public void setParentName(String value) {
    	if (value == null) {
    		setAttribute("ParentName", "root");
    	} else {
    		setAttribute("ParentName", value);
    	}
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
		return "- Id: " + getAttribute("Id") + ", Name: " + getAttribute("Name") + ", Type: " + getAttribute("Type") + ", GlobalWeight: " + getAttribute("GlobalWeight") + ", ParentId: " + getAttribute("ParentId") + " -";
	}
    
    
}
