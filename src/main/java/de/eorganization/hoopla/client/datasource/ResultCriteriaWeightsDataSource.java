package de.eorganization.hoopla.client.datasource;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.widgets.tree.TreeNode;

import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;
import de.eorganization.hoopla.shared.model.ahp.values.EvaluationResult;

public class ResultCriteriaWeightsDataSource {

	public TreeNode[] createResultTreeNodeArray(EvaluationResult result) {
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

		for (Goal crit : result.getDecision().getGoals()) {
			treeNodeList.add(new CriterionTreeNode(crit.getId(),
					crit.getName(), crit.getDescription(), crit.getGoalType()
							.toString(), crit.getWeight(), crit.getGlobalWeight(), null, null));
			treeNodeList.addAll(getChildrenTreeNodes(crit));
		}

		return treeNodeList.toArray(new TreeNode[0]);
	}

	private List<CriterionTreeNode> getChildrenTreeNodes(Criterion crit) {
		List<CriterionTreeNode> criteriaTreeNodeList = new ArrayList<CriterionTreeNode>();

		for (Criterion criterion : crit.getChildren()) {
			criteriaTreeNodeList.add(new CriterionTreeNode(criterion.getId(),
					criterion.getName(), criterion.getDescription(), criterion
							.getType() != null ? criterion.getType()
							.getTypeName() : "", criterion.getWeight(), criterion.getGlobalWeight(),
					crit.getName(), crit.getId()));
			criteriaTreeNodeList.addAll(getChildrenTreeNodes(criterion));
		}
		return criteriaTreeNodeList;
	}

}