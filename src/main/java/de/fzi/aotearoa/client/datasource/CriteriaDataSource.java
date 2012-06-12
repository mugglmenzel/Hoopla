package de.fzi.aotearoa.client.datasource;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.widgets.tree.TreeNode;

import de.fzi.aotearoa.client.AotearoaSmart;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;

public class CriteriaDataSource {

	public TreeNode[] createCriterionTreeNodeArray() {

		List<Goal> goals = AotearoaSmart.decision.getGoals();

		List<TreeNode> criteriaTreeNodeList = new ArrayList<TreeNode>();

		for(Goal crit : goals) {
			criteriaTreeNodeList.add(new CriterionTreeNode(crit.getId(), crit.getName(),
					crit.getDescription(),
					crit.getGoalType() != null ? crit.getGoalType()
							.toString() : "", crit.getWeight(), crit.getGlobalWeight(), null, null));
			criteriaTreeNodeList.addAll(getChildrenTreeNodes(crit));
		}

		return criteriaTreeNodeList.toArray(new TreeNode[0]);
	}
	
	public TreeNode[] createCriterionTreeNodeArray(String goalName) {
		
		List<TreeNode> criteriaTreeNodeList = new ArrayList<TreeNode>();
		
		criteriaTreeNodeList.addAll(getChildrenTreeNodes(AotearoaSmart.decision.getGoal(goalName)));
		
		return criteriaTreeNodeList.toArray(new TreeNode[0]);
	}

	private List<CriterionTreeNode> getChildrenTreeNodes(Criterion crit) {
		List<CriterionTreeNode> criteriaTreeNodeList = new ArrayList<CriterionTreeNode>();
		
		for (Criterion criterion : crit.getChildren()) {
			criteriaTreeNodeList.add(new CriterionTreeNode(criterion.getId(), criterion.getName(),
					criterion.getDescription(),
					criterion.getType() != null && !criterion.hasChildren() ? criterion.getType()
							.getTypeName() : "", crit.getWeight(), crit.getGlobalWeight(), crit.getName(), crit.getId()));
			criteriaTreeNodeList.addAll(getChildrenTreeNodes(criterion));
		}
		return criteriaTreeNodeList;
	}
}
