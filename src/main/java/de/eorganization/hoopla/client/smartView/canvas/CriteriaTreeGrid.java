package de.eorganization.hoopla.client.smartView.canvas;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.datasource.CriteriaDataSource;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;

public class CriteriaTreeGrid extends TreeGrid {

	private HLayout rollOverCanvas;
	private ListGridRecord rollOverRecord;
	private Goal currentGoal;

	public CriteriaTreeGrid(Goal goal, boolean isEditable) {
		super();
		currentGoal = goal;
		setShowRollOverCanvas(isEditable);
		setWidth100();
		setHeight100();

		//setSelectionAppearance(SelectionAppearance.CHECKBOX);
		//setShowSelectedStyle(false);
		//setShowPartialSelection(true);
		//setCascadeSelection(true);
		setShowConnectors(true);

		TreeGridField field1 = new TreeGridField("Name", "Name");
		TreeGridField field2 = new TreeGridField("Type", "Type");
		TreeGridField[] fields = new TreeGridField[] { field1, field2 };

		setFields(fields);

		final Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("Name");
		tree.setIdField("Name");
		tree.setParentIdField("ParentName");
		tree.setRootValue(currentGoal.getName());
		tree.setShowRoot(false);

		addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				tree.openAll();
			}
		});

		setData(tree);
		selectAllRecords();
		
//		if (isEditable) {
//			addDragAndDrop();
//		}

		draw();
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {

		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(50);
			rollOverCanvas.setHeight(22);

			ImgButton editImg = new ImgButton();
			editImg.setShowDown(false);
			editImg.setShowRollOver(false);
			editImg.setLayoutAlign(Alignment.CENTER);
			editImg.setSrc("/comment_edit.png");
			editImg.setPrompt("Edit Criterion");
			editImg.setHeight(16);
			editImg.setWidth(16);
			editImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					String criterionName = rollOverRecord.getAttribute("Name");
					String parentName = rollOverRecord
							.getAttribute("ParentName");
					new CriterionDetailsWindow(currentGoal.getName(),
							criterionName, parentName, CriteriaTreeGrid.this).show();
				}

			});

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc("/delete.png");
			deleteImg.setPrompt("Delete Criterion");
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					final String currentCritName = rollOverRecord
							.getAttribute("Name");
					SC.confirm("Do you really want to delete "
							+ currentCritName
							+ " and all its children criteria?",
							new BooleanCallback() {
								public void execute(Boolean value) {
									if (value != null && value) {
										currentGoal
												.deleteCriterion(currentCritName);
										CriteriaTreeGrid.this
												.refreshCriteriaTree();
									}
								}
							});
				}
			});

			ImgButton addChildImg = new ImgButton();
			addChildImg.setShowDown(false);
			addChildImg.setShowRollOver(false);
			addChildImg.setLayoutAlign(Alignment.CENTER);
			addChildImg.setSrc("/add.png");
			addChildImg.setPrompt("Add Child Criterion");
			addChildImg.setHeight(16);
			addChildImg.setWidth(16);
			addChildImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					String currentCritName = rollOverRecord
							.getAttribute("Name");
					new CriterionDetailsWindow(currentGoal.getName(),
							currentCritName, CriteriaTreeGrid.this).show();
				}
			});

			rollOverCanvas.addMember(addChildImg);
			rollOverCanvas.addMember(editImg);
			rollOverCanvas.addMember(deleteImg);
		}
		return rollOverCanvas;
	}
	
	private void addDragAndDrop() {

		 addFolderDropHandler(new FolderDropHandler() {
		 
		@Override
		public void onFolderDrop(FolderDropEvent event) {
			System.out.println("dropped to folder " + event);
			 
//			 for (TreeNode node : event.getNodes()) {
//			 Hoopla.decision.getCriterion( ((CriterionTreeNode)
//			 event.getFolder()) .getAttribute("Name")).addChild(
//			 Hoopla.decision .getCriterion(((CriterionTreeNode) node).getAttribute("Name"))); } 
			 refreshCriteriaTree();
			
		} });
		 
		 
		 addDropHandler(new DropHandler() {
		  
		 @Override 
		 public void onDrop(DropEvent event) {
		 System.out.println("dropped " + event);
		 refreshCriteriaTree();
		 } });
		 
		 //setCanRemoveRecords(false);
		 setCanSelectAll(true);
		 setCanSelectText(true); 
		 setCanDropOnLeaves(true);
		 setCanReorderRecords(true);
		 setCanAcceptDroppedRecords(true);
		
	}

	public void refreshCriteriaTree() {
		if (!Hoopla.decision.getGoals().isEmpty()) {
			final Tree tree = new Tree();
			tree.setModelType(TreeModelType.PARENT);
			tree.setNameProperty("Name");
			tree.setIdField("Name");
			tree.setParentIdField("ParentName");
			tree.setRootValue(currentGoal.getName());
			tree.setShowRoot(false);
			tree.setData(new CriteriaDataSource()
					.createCriterionTreeNodeArray(currentGoal.getName()));
			setData(tree);
			selectAllRecords();
			tree.openAll();
		}
	}

}
