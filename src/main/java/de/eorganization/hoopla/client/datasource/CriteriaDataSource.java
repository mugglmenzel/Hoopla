package de.eorganization.hoopla.client.datasource;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.widgets.tree.TreeNode;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;

public class CriteriaDataSource {

	public TreeNode[] createCriterionTreeNodeArray() {

		List<Goal> goals = Hoopla.decision.getGoals();

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
		
		criteriaTreeNodeList.addAll(getChildrenTreeNodes(Hoopla.decision.getGoal(goalName)));
		
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
