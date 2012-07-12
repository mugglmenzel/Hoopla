package de.eorganization.hoopla.client.datasource;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.widgets.tree.TreeNode;

import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;
import de.eorganization.hoopla.shared.model.ahp.values.EvaluationResult;

public class ResultDataSource {

	public TreeNode[] createResultTreeNodeArray(EvaluationResult result) {
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

		for (Alternative alt : result.getResultMultiplicativeIndexMap().keySet()) {
			treeNodeList
					.add(new ResultTreeNode(alt.getId(), alt.getName(), alt
							.getDescription(), null, result.getResultMultiplicativeIndexMap().get(
							alt)));
			for (Goal crit : result.getDecision().getGoals()) {
				treeNodeList.add(new CriterionTreeNode(alt.getId()
						+ crit.getId(), crit.getName(), crit.getDescription(),
						crit.getGoalType().toString(), crit.getWeight(), crit.getGlobalWeight(), alt.getName(),
						alt.getId()));
				treeNodeList.addAll(getChildrenTreeNodes(crit, alt));
			}
		}

		return treeNodeList.toArray(new TreeNode[0]);
	}

	private List<CriterionTreeNode> getChildrenTreeNodes(Criterion crit,
			Alternative alt) {
		List<CriterionTreeNode> criteriaTreeNodeList = new ArrayList<CriterionTreeNode>();

		for (Criterion criterion : crit.getChildren()) {
			criteriaTreeNodeList.add(new CriterionTreeNode(alt.getId()
					+ criterion.getId(), criterion.getName(), criterion
					.getDescription(), criterion.getType() != null ? criterion
					.getType().getTypeName() : "", criterion.getWeight(), criterion.getGlobalWeight(), crit
					.getName(), alt.getId() + crit.getId()));
			criteriaTreeNodeList.addAll(getChildrenTreeNodes(criterion, alt));
		}
		return criteriaTreeNodeList;
	}

}