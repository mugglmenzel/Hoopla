package de.eorganization.hoopla.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.gwt.user.client.ui.FileUpload;

import de.eorganization.hoopla.client.services.HooplaService;
import de.eorganization.hoopla.server.services.HooplaServiceImpl;
import de.eorganization.hoopla.shared.model.ahp.configuration.Alternative;
import de.eorganization.hoopla.shared.model.ahp.configuration.Criterion;
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;
import de.eorganization.hoopla.shared.model.ahp.configuration.Goal;
import de.eorganization.hoopla.shared.model.ahp.values.CriterionImportance;
import de.eorganization.hoopla.shared.model.ahp.values.GoalImportance;

/**
 * @author mugglmenzel
 * @author svens0n
 * 
 *         Author: Michael Menzel (mugglmenzel), Sven Frauen (svens0n)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel $
 * 
 *         Revision: $Revision: 165 $
 * 
 *         Date: $Date: 2011-08-05 15:45:22 +0200 (Fri, 05 Aug 2011) $
 * 
 *         License:
 * 
 *         Copyright 2011 Forschungszentrum Informatik FZI / Karlsruhe Institute
 *         of Technology
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 * 
 *         SVN URL: $HeadURL:
 *         https://aotearoadecisions.googlecode.com/svn/trunk/
 *         src/main/java/de/fzi
 *         /aotearoa/de.eorganization.hoopla.shared.model/model/ahp/configuration/Alternative.java $
 * 
 */
public class TemplateExportServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(FileUpload.class
			.getName());

	private static final long serialVersionUID = 1L;

	private DecisionTemplate decisionTemplate = null;

	Document doc = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HooplaService aos = new HooplaServiceImpl();
		decisionTemplate = aos.getDecisionTemplate(new Long(req
				.getParameter("id")));
		log.fine("loaded decision template " + decisionTemplate + " from id "
				+ req.getParameter("id"));

		if (decisionTemplate == null) {
			decisionTemplate = ExampleDecisionFactory
					.createExampleDecisionTemplate();
		}

		log.info("Creating a new XML Template for decision: "
				+ decisionTemplate.getTemplateName());

		DocumentBuilderFactory docBFac;
		DocumentBuilder docBuild;
		try {
			docBFac = DocumentBuilderFactory.newInstance();
			docBuild = docBFac.newDocumentBuilder();
			doc = docBuild.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Element root = doc.createElement("decisionTemplate");

			Element name = doc.createElement("name");
			name.appendChild(decisionTemplate.getName() != null ? doc.createTextNode(decisionTemplate.getName()) : doc.createTextNode(""));
			root.appendChild(name);

			Element templateDescr = doc.createElement("description");
			templateDescr.appendChild(decisionTemplate.getDescription() != null ? doc.createTextNode(decisionTemplate
					.getDescription()) : doc.createTextNode(""));
			root.appendChild(templateDescr);

			Element templateName = doc.createElement("templateName");
			templateName.appendChild(decisionTemplate.getTemplateName() != null ? doc.createTextNode(decisionTemplate
					.getTemplateName()) : doc.createTextNode(""));
			root.appendChild(templateName);

			root.appendChild(createAlternativesElement());

			root.appendChild(createGoalsElement());

			root.appendChild(createImportanceGoalsElement());

			doc.appendChild(root);

			log.info(xmlToString(doc));

			resp.setContentType("application/xml");

			PrintWriter out = resp.getWriter();
			out.println(xmlToString(doc));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Element createAlternativesElement() {
		Element alternatives = doc.createElement("alternatives");

		for (Alternative alt : decisionTemplate.getAlternatives()) {
			Element alternative = doc.createElement("alternative");

			Element name = doc.createElement("name");
			name.appendChild(alt.getName() != null ? doc.createTextNode(alt.getName()) : doc.createTextNode(""));
			alternative.appendChild(name);

			Element decsription = doc.createElement("description");
			decsription.appendChild(alt.getDescription() != null ? doc.createTextNode(alt.getDescription()) : doc.createTextNode(""));
			alternative.appendChild(decsription);

			alternatives.appendChild(alternative);
		}

		return alternatives;
	}

	private Element createGoalsElement() {
		Element goals = doc.createElement("goals");

		for (Goal goal : decisionTemplate.getGoals()) {
			Element goalElement = doc.createElement("goal");

			Element name = doc.createElement("name");
			name.appendChild(goal.getName() != null ? doc.createTextNode(goal.getName()) : doc.createTextNode(""));
			goalElement.appendChild(name);

			Element decsription = doc.createElement("description");
			decsription.appendChild(goal.getDescription() != null ? doc.createTextNode(goal.getDescription()) : doc.createTextNode(""));
			goalElement.appendChild(decsription);

			Element goalType = doc.createElement("goalType");
			goalType.appendChild(goal.getGoalType() != null ? doc.createTextNode(goal.getGoalType().name()) : doc.createTextNode("POSITIVE"));
			goalElement.appendChild(goalType);

			goalElement.appendChild(createChildrenElement(goal.getChildren()));

			goals.appendChild(goalElement);
		}

		return goals;
	}

	private Element createChildrenElement(List<Criterion> criteria) {
		Element children = doc.createElement("children");

		for (Criterion crit : criteria) {
			Element criterion = doc.createElement("criterion");

			Element name = doc.createElement("name");
			name.appendChild(crit.getName() != null ? doc.createTextNode(crit.getName()) : doc.createTextNode(""));
			criterion.appendChild(name);

			Element decsription = doc.createElement("description");
			decsription.appendChild(crit.getDescription() != null ? doc.createTextNode(crit.getDescription()) : doc.createTextNode(""));
			criterion.appendChild(decsription);

			Element type = doc.createElement("type");
			type.appendChild(crit.getType() != null ? doc.createTextNode(crit.getType().getTypeName()) : doc.createTextNode("quantitative"));
			criterion.appendChild(type);

			Element weight = doc.createElement("weight");
			//save as every criterion has a default value of 1D as weight
			Double number = crit.getWeight();
			weight.appendChild(doc.createTextNode(number.toString()));

			criterion.appendChild(weight);

			if (crit.hasChildren()) {
				criterion
						.appendChild(createChildrenElement(crit.getChildren()));
			} else {
				criterion.appendChild(doc.createElement("children"));
			}

			if (!crit.getImportanceChildren().isEmpty()) {
				Element importanceChildren = doc
						.createElement("importanceChildren");

				for (CriterionImportance critImportance : crit
						.getImportanceChildren()) {
					Element criterionImportance = doc
							.createElement("criterionImportance");

					Element critA = doc.createElement("critA");
					Integer critANumber = critImportance.getCritA();
					critA.appendChild(doc.createTextNode(critANumber.toString()));
					criterionImportance.appendChild(critA);

					Element critB = doc.createElement("critB");
					Integer critBNumber = critImportance.getCritB();
					critB.appendChild(doc.createTextNode(critBNumber.toString()));
					criterionImportance.appendChild(critB);

					Element comparisonAToB = doc
							.createElement("comparisonAToB");
					Double comparisonAToBNumber = critImportance.getComparisonAToB() != null ? critImportance.getComparisonAToB() : 0D;
					comparisonAToB.appendChild(doc
							.createTextNode(comparisonAToBNumber.toString()));
					criterionImportance.appendChild(comparisonAToB);

					Element comment = doc.createElement("comment");
					comment.appendChild(critImportance.getComment() != null ? doc.createTextNode(critImportance
							.getComment()) : doc.createTextNode(""));
					criterionImportance.appendChild(comment);

					importanceChildren.appendChild(criterionImportance);
				}
				criterion.appendChild(importanceChildren);
			} else {
				criterion.appendChild(doc.createElement("importanceChildren"));
			}

			children.appendChild(criterion);
		}
		return children;
	}

	private Element createImportanceGoalsElement() {
		Element importanceGoals = doc.createElement("importanceGoals");

		for (GoalImportance goalImp : decisionTemplate.getImportanceGoals()) {
			Element goalImportance = doc.createElement("goalImportance");

			Element critA = doc.createElement("critA");
			Integer critANumber = goalImp.getCritA();
			critA.appendChild(doc.createTextNode(critANumber.toString()));
			goalImportance.appendChild(critA);

			Element critB = doc.createElement("critB");
			Integer critBNumber = goalImp.getCritB();
			critB.appendChild(doc.createTextNode(critBNumber.toString()));
			goalImportance.appendChild(critB);

			Element comparisonAToB = doc.createElement("comparisonAToB");
			Double comparisonAToBNumber = goalImp.getComparisonAToB() != null ? goalImp.getComparisonAToB() : 0D;
			comparisonAToB.appendChild(doc.createTextNode(comparisonAToBNumber
					.toString()));
			goalImportance.appendChild(comparisonAToB);

			Element comment = doc.createElement("comment");
			comment.appendChild(goalImp.getComment() != null ? doc.createTextNode(goalImp.getComment()) : doc.createTextNode(""));
			goalImportance.appendChild(comment);

			importanceGoals.appendChild(goalImportance);
		}

		return importanceGoals;
	}

	private String xmlToString(Node node) {
		try {
			Source source = new DOMSource(node);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

}
