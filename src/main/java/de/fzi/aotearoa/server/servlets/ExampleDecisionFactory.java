package de.fzi.aotearoa.server.servlets;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.CriterionType;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.DecisionTemplate;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;
import de.fzi.aotearoa.shared.model.ahp.configuration.GoalType;
import de.fzi.aotearoa.shared.model.ahp.values.CriterionImportance;


public class ExampleDecisionFactory {
	
	public static Decision createExampleDecision() {
		
		Decision  decision = new Decision();
		decision.setName("Cloud Computing Provider Selection");
		decision.setDescription("Decision Description");
		Goal goal = new Goal();
		goal.setName("Find the best Cloud Provider");
		goal.setDescription("Goal Description");
		goal.setWeight(1);
		Goal goal2 = new Goal();
		goal2.setName("goal2");
		goal2.setDescription("Goal2 description");
		Criterion c1 = new Criterion("Kosten");
		c1.setDescription("description");
		Criterion c2 = new Criterion("Usability");
		c2.setDescription("description");
		Criterion c3 = new Criterion("Reputation");
		c3.setDescription("description");
		Criterion c4 = new Criterion("Service Level Agreements");
		c4.setDescription("description");
		Criterion c5 = new Criterion("Architektur");
		c5.setDescription("description");
		
		//Kosten
		Criterion c11 = new Criterion("laufende Kosten");
		c11.setDescription("description");
		Criterion c12 = new Criterion("Abrechnungsmodell");
		c12.setDescription("description");
		Criterion c13 = new Criterion("Rabatte/Verguenstigungen");
		c13.setDescription("description");
		c1.addChild(c11);
		c1.addChild(c12);
		c1.addChild(c13);
		
		//Usability
		Criterion c21 = new Criterion("User Interface");
		c21.setDescription("description");
		Criterion c22 = new Criterion("Dokumentation");
		c22.setDescription("description");
		Criterion c23 = new Criterion("Online Support");
		c23.setDescription("description");
		c2.addChild(c21);
		c2.addChild(c22);
		c2.addChild(c23);
		
		//Reputation
		Criterion c31 = new Criterion("Gesamteindruck");
		c31.setDescription("description");
		Criterion c32 = new Criterion("Zertifizierung");
		c32.setDescription("description");
		c3.addChild(c31);
		c3.addChild(c32);
		
		//Service Level Agreements
		Criterion c41 = new Criterion("Verf�gbarkeit");
		c41.setDescription("description");
		Criterion c42 = new Criterion("Performanz");
		c42.setDescription("description");

		c4.addChild(c41);
		c4.addChild(c42);

		
		//Architektur
		Criterion c51 = new Criterion("Elastizitaet");
		c51.setDescription("description");
		Criterion c52 = new Criterion("Software Lock-In");
		c52.setDescription("description");
		Criterion c53 = new Criterion("Breite des Service Portfolios");
		c53.setDescription("description");
		c5.addChild(c51);
		c5.addChild(c52);
		c5.addChild(c53);
		
		
		//defining Alternatives
		Alternative a1 = new Alternative();
		a1.setName("Amazon Web Services");
		a1.setDescription("a1 descr");
		Alternative a2 = new Alternative();
		a2.setName("Rackspace UK");
		a2.setDescription("a2 descr");
		Alternative a3 = new Alternative();
		a3.setName("Terremark vCloud Express");
		a3.setDescription("a3 descr");
		
		
		goal.addChild(c1);
		goal.addChild(c2);
		goal.addChild(c3);
		goal.addChild(c4);
		goal.addChild(c5);
		
		decision.addGoal(goal);
		decision.addGoal(goal2);
		decision.addAlternative(a1);
		decision.addAlternative(a2);
		decision.addAlternative(a3);
		
		return decision;
	}
	
	public static DecisionTemplate createExampleDecisionTemplate() {
		
		DecisionTemplate  decisionTemplate = new DecisionTemplate();
		decisionTemplate.setName("Cloud Computing Provider Selection");
		decisionTemplate.setDescription("Decision Description");
		decisionTemplate.setTemplateName("Template");
		Goal goal = new Goal();
		goal.setName("Find the best Cloud Provider");
		goal.setDescription("Goal Description");
		goal.setGoalType(GoalType.NEGATIVE);
		Goal goal2 = new Goal();
		goal2.setName("goal2");
		goal2.setGoalType(GoalType.POSITIVE);
		goal2.setDescription("Goal2 description");
		Criterion c1 = new Criterion("Kosten");
		c1.setDescription("description");
		c1.setType(CriterionType.BENCHMARK);
		Criterion c2 = new Criterion("Usability");
		c2.setDescription("description");
		c2.setType(CriterionType.QUALITATIVE);
		Criterion c3 = new Criterion("Reputation");
		c3.setDescription("description");
		c3.setType(CriterionType.QUANTITATIVE);
		Criterion c4 = new Criterion("Service Level Agreements");
		c4.setDescription("description");
		c4.setType(CriterionType.QUALITATIVE);
		Criterion c5 = new Criterion("Architektur");
		c5.setDescription("description");
		c5.setType(CriterionType.QUALITATIVE);
		
		/*
		CriterionImportance ci0 = new CriterionImportance();
		ci0.setCritA(0);
		ci0.setCritB(1);
		ci0.setComparisonAToB(1.0);
		ci0.setComment("comment0");
		
		CriterionImportance ci1 = new CriterionImportance();
		ci1.setCritA(0);
		ci1.setCritB(2);
		ci1.setComparisonAToB(1.0);
		ci1.setComment("comment1");
		
		CriterionImportance ci2 = new CriterionImportance();
		ci2.setCritA(0);
		ci2.setCritB(3);
		ci2.setComparisonAToB(1.0);
		ci2.setComment("comment2");
		
		CriterionImportance ci3 = new CriterionImportance();
		ci3.setCritA(0);
		ci3.setCritB(4);
		ci3.setComparisonAToB(1.0);
		ci3.setComment("comment3");
		
		CriterionImportance ci4 = new CriterionImportance();
		ci4.setCritA(1);
		ci4.setCritB(2);
		ci4.setComparisonAToB(1.0);
		ci4.setComment("comment4");
		
		CriterionImportance ci5 = new CriterionImportance();
		ci5.setCritA(1);
		ci5.setCritB(3);
		ci5.setComparisonAToB(1.0);
		ci5.setComment("comment5");
		
		CriterionImportance ci6 = new CriterionImportance();
		ci6.setCritA(1);
		ci6.setCritB(4);
		ci6.setComparisonAToB(1.0);
		ci6.setComment("comment6");
		
		CriterionImportance ci7 = new CriterionImportance();
		ci7.setCritA(2);
		ci7.setCritB(3);
		ci7.setComparisonAToB(1.0);
		ci7.setComment("comment7");
		
		CriterionImportance ci8 = new CriterionImportance();
		ci8.setCritA(2);
		ci8.setCritB(3);
		ci8.setComparisonAToB(1.0);
		ci8.setComment("comment8");
		
		CriterionImportance ci9 = new CriterionImportance();
		ci9.setCritA(3);
		ci9.setCritB(4);
		ci9.setComparisonAToB(1.0);
		ci9.setComment("comment9");
		*/
		
		//Kosten
		Criterion c11 = new Criterion("laufende Kosten");
		c11.setDescription("description");
		c11.setType(CriterionType.QUALITATIVE);
		Criterion c12 = new Criterion("Abrechnungsmodell");
		c12.setDescription("description");
		c12.setType(CriterionType.QUALITATIVE);
		Criterion c13 = new Criterion("Rabatte/Verguenstigungen");
		c13.setDescription("description");
		c13.setType(CriterionType.QUALITATIVE);
		c1.addChild(c11);
		c1.addChild(c12);
		c1.addChild(c13);
		
		//Usability
		Criterion c21 = new Criterion("User Interface");
		c21.setDescription("description");
		c21.setType(CriterionType.QUALITATIVE);
		Criterion c22 = new Criterion("Dokumentation");
		c22.setDescription("description");
		c22.setType(CriterionType.QUALITATIVE);
		Criterion c23 = new Criterion("Online Support");
		c23.setDescription("description");
		c23.setType(CriterionType.QUALITATIVE);
		c2.addChild(c21);
		c2.addChild(c22);
		c2.addChild(c23);
		
		//Reputation
		Criterion c31 = new Criterion("Gesamteindruck");
		c31.setDescription("description");
		c31.setType(CriterionType.QUALITATIVE);
		Criterion c32 = new Criterion("Zertifizierung");
		c32.setDescription("description");
		c32.setType(CriterionType.QUALITATIVE);
		c3.addChild(c31);
		c3.addChild(c32);
		
		//Service Level Agreements
		Criterion c41 = new Criterion("Verf�gbarkeit");
		c41.setDescription("description");
		c41.setType(CriterionType.QUALITATIVE);
		Criterion c42 = new Criterion("Performanz");
		c42.setDescription("description");
		c42.setType(CriterionType.QUALITATIVE);

		c4.addChild(c41);
		c4.addChild(c42);

		
		//Architektur
		Criterion c51 = new Criterion("Elastizitaet");
		c51.setDescription("description");
		c51.setType(CriterionType.QUALITATIVE);
		Criterion c52 = new Criterion("Software Lock-In");
		c52.setDescription("description");
		c52.setType(CriterionType.QUALITATIVE);
		Criterion c53 = new Criterion("Breite des Service Portfolios");
		c53.setDescription("description");
		c53.setType(CriterionType.QUALITATIVE);
		c5.addChild(c51);
		c5.addChild(c52);
		c5.addChild(c53);
		
		
		//defining Alternatives
		Alternative a1 = new Alternative();
		a1.setName("Amazon Web Services");
		a1.setDescription("a1 descr");
		Alternative a2 = new Alternative();
		a2.setName("Rackspace UK");
		a2.setDescription("a2 descr");
		Alternative a3 = new Alternative();
		a3.setName("Terremark vCloud Express");
		a3.setDescription("a3 descr");
		
		
		goal.addChild(c1);
		goal.addChild(c2);
		goal.addChild(c3);
		goal.addChild(c4);
		goal.addChild(c5);
		/*
		goal.getImportanceChildren().add(ci0);
		goal.getImportanceChildren().add(ci1);
		goal.getImportanceChildren().add(ci2);
		goal.getImportanceChildren().add(ci3);
		goal.getImportanceChildren().add(ci4);
		goal.getImportanceChildren().add(ci5);
		goal.getImportanceChildren().add(ci6);
		goal.getImportanceChildren().add(ci7);
		goal.getImportanceChildren().add(ci8);
		goal.getImportanceChildren().add(ci9);
		*/
		decisionTemplate.addGoal(goal);
		decisionTemplate.addGoal(goal2);
		decisionTemplate.addAlternative(a1);
		decisionTemplate.addAlternative(a2);
		decisionTemplate.addAlternative(a3);
		
		return decisionTemplate;
	}

}
