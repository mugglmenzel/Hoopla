package de.fzi.aotearoa.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.user.client.ui.FileUpload;

import de.fzi.aotearoa.server.services.AotearoaServiceImpl;
import de.fzi.aotearoa.shared.model.ahp.configuration.Alternative;
import de.fzi.aotearoa.shared.model.ahp.configuration.CriterionType;
import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.DecisionTemplate;
import de.fzi.aotearoa.shared.model.ahp.configuration.Goal;
import de.fzi.aotearoa.shared.model.ahp.configuration.Criterion;
import de.fzi.aotearoa.shared.model.ahp.configuration.GoalType;
import de.fzi.aotearoa.shared.model.ahp.values.CriterionImportance;
import de.fzi.aotearoa.shared.model.ahp.values.GoalImportance;

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
 *         /aotearoa/shared/model/ahp/configuration/Alternative.java $
 * 
 */
public class TemplateImportServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(FileUpload.class
			.getName());

	private static final long serialVersionUID = 1L;

	private boolean isDecisionTemplate = false;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			DecisionTemplate newDecisionTemplate = new DecisionTemplate();

			ServletFileUpload upload = new ServletFileUpload();
			resp.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName());
				} else {
					log.info("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());
				}

				Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(stream);

				// doc.getDocumentElement().normalize();

				Element decisionElement = doc.getDocumentElement();
				String rootName = decisionElement.getNodeName();
				if (rootName.equals("decision")) {
					isDecisionTemplate = false;
				} else if (rootName.equals("decisionTemplate")) {
					isDecisionTemplate = true;
				} else {
					log.warning("This XML Document has a wrong RootElement: "
							+ rootName
							+ ". It should be <decision> or <decisionTemplate>.");
				}

				NodeList decisionNodes = decisionElement.getChildNodes();
				for (int i = 0; i < decisionNodes.getLength(); i++) {
					Node node = decisionNodes.item(i);

					if (node instanceof Element) {
						Element child = (Element) node;
						if (child.getNodeName().equals("name")
								&& !child.getTextContent().equals("")) {
							newDecisionTemplate.setName(child.getTextContent());
							log.info("Parsed decision name: "
									+ newDecisionTemplate.getName());
						}
						if (child.getNodeName().equals("description")
								&& !child.getTextContent().equals("")) {
							newDecisionTemplate.setDescription(child
									.getTextContent());
							log.info("Parsed decision description: "
									+ newDecisionTemplate.getDescription());
						}
						if (isDecisionTemplate
								&& child.getNodeName().equals("templateName")) {
							newDecisionTemplate.setTemplateName(child
									.getTextContent());
							log.info("Parsed decision TemplateName: "
									+ newDecisionTemplate.getTemplateName());
						}
						if (child.getNodeName().equals("alternatives")) {
							parseAlternatives(child.getChildNodes(),
									newDecisionTemplate);
						}
						if (child.getNodeName().equals("goals")) {
							parseGoals(child.getChildNodes(),
									newDecisionTemplate);
						}
						if (child.getNodeName().equals("importanceGoals")) {
							parseGoalImportances(child.getChildNodes(),
									newDecisionTemplate);
						}
					}
				}

				// NodeList decNameElementList =
				// doc.getElementsByTagName("name");
				// Element decNameElement = (Element)
				// decNameElementList.item(0);
				// System.out.println("Decision Name: " +
				// decNameElement.getNodeValue());
				// newDecision.setName(decNameElement.getNodeValue());
				//
				// NodeList decDescrElementList =
				// doc.getElementsByTagName("description");
				// Element decDescrElement = (Element)
				// decDescrElementList.item(0);
				// System.out.println("Decision Descrption: " +
				// decDescrElement.getNodeValue());
				// newDecision.setDescription(decDescrElement.getNodeValue());

				log.info("Fully parsed Decision: "
						+ newDecisionTemplate.toString());

				// import alternatives
				// NodeList altNodeList =
				// doc.getElementsByTagName("alternative");
				// System.out.println("Information of all alternatives:");
				//
				// for (int i = 0; i < altNodeList.getLength(); i++) {
				//
				// Node altNode = altNodeList.item(i);
				//
				// if (altNode.getNodeType() == Node.ELEMENT_NODE) {
				//
				// Element altElement = (Element) altNode;
				//
				// NodeList altNameElementList =
				// altElement.getElementsByTagName("name");
				// Element altNameElement = (Element)
				// altNameElementList.item(0);
				// NodeList altNameList = altNameElement.getChildNodes();
				// System.out.println("Alternative Name: " + ((Node)
				// altNameList.item(0)).getNodeValue());
				//
				// NodeList altDescrElementList =
				// altElement.getElementsByTagName("description");
				// Element altDescrElement = (Element)
				// altDescrElementList.item(0);
				// NodeList altDescrList = altDescrElement.getChildNodes();
				// System.out.println("Alternative Description: " + ((Node)
				// altDescrList.item(0)).getNodeValue());
				// }
				// }

				// int len;
				// byte[] buffer = new byte[8192];
				// while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
				// resp.getOutputStream().write(buffer, 0, len);
				// }

				new AotearoaServiceImpl()
						.storeDecisionTemplate(newDecisionTemplate);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		log.info("returning to referer " + req.getHeader("referer"));
		resp.sendRedirect(req.getHeader("referer") != null && !"".equals(req.getHeader("referer")) ? req.getHeader("referer") : "localhost:8088");
	}

	private void parseAlternatives(NodeList altNodes, Decision newDecision) {
		for (int i = 0; i < altNodes.getLength(); i++) {
			Node node = altNodes.item(i);

			if (node instanceof Element) {
				Alternative alt = new Alternative();
				Element child = (Element) node;

				NodeList name = child.getElementsByTagName("name");
				if (name.getLength() > 0) {
					alt.setName(name.item(0).getTextContent());
				}

				NodeList descr = child.getElementsByTagName("description");
				if (descr.getLength() > 0) {
					alt.setDescription(descr.item(0).getTextContent());
				}

				// NodeList indexResult =
				// child.getElementsByTagName("indexResult");
				// if (indexResult.getLength() > 0) {
				// try {
				// alt.setIndexResult(Float.valueOf(indexResult.item(0).getTextContent()));
				// } catch (Exception ex) {
				// ex.printStackTrace();
				// }
				// }

				log.info("Parsed alternative: " + alt.getName() + ", "
						+ alt.getDescription());
				newDecision.addAlternative(alt);
			}
		}
	}

	private void parseGoals(NodeList goalNodes, Decision newDecision) {
		for (int i = 0; i < goalNodes.getLength(); i++) {
			Node node = goalNodes.item(i);

			if (node instanceof Element) {
				Goal newGoal = new Goal();

				Element child = (Element) node;

				NodeList goalTagNodes = child.getChildNodes();
				for (int x = 0; x < goalTagNodes.getLength(); x++) {
					if (goalTagNodes.item(x) instanceof Element) {
						Element goalTagElement = (Element) goalTagNodes.item(x);

						if (goalTagElement.getNodeName().equals("name")) {
							newGoal.setName(goalTagElement.getTextContent());
						}

						if (goalTagElement.getNodeName().equals("description")) {
							newGoal.setDescription(goalTagElement
									.getTextContent());
						}

						if (goalTagElement.getNodeName().equals("goalType")) {
							newGoal.setGoalType(GoalType.valueOf(goalTagElement
									.getTextContent()));
						}

						if (goalTagElement.getNodeName().equals("children")) {
							newGoal.setChildren(parseCriteria(goalTagElement
									.getChildNodes()));
						}

						if (goalTagElement.getNodeName().equals(
								"importanceChildren")) {
							if (goalTagElement.hasChildNodes()) {
								parseImportanceChildren(
										goalTagElement.getChildNodes(), newGoal);
							}
						}
					}
				}
				// NodeList name = child.getElementsByTagName("goalName");
				// if (name.getLength() > 0) {
				// newGoal.setName(name.item(0).getTextContent());
				// }
				//
				// NodeList description =
				// child.getElementsByTagName("goalDescription");
				// if (description.getLength() > 0) {
				// newGoal.setDescription(description.item(0).getTextContent());
				// }
				//
				// NodeList goalType = child.getElementsByTagName("goalType");
				// if (goalType.getLength() > 0 ) {
				// if (goalType.item(0).getTextContent().equals("POSITIVE")) {
				// newGoal.setGoalType(GoalType.POSITIVE);
				// } else if
				// (goalType.item(0).getTextContent().equals("NEGATIVE")) {
				// newGoal.setGoalType(GoalType.NEGATIVE);
				// }
				// }
				//
				// NodeList criteria = child.getElementsByTagName("criteria");
				// if (criteria.getLength() > 0) {
				// newGoal.setChildren(parseCriteria(criteria.item(0).getChildNodes()));
				// }

				newDecision.addGoal(newGoal);
				log.info("Parsed gaol: " + newGoal.getName() + ", "
						+ newGoal.getDescription() + ", "
						+ newGoal.getGoalType());
			}
		}
	}

	private List<Criterion> parseCriteria(NodeList criterionNodes) {
		List<Criterion> criteriaList = new ArrayList<Criterion>();

		for (int i = 0; i < criterionNodes.getLength(); i++) {
			Node criterionNode = criterionNodes.item(i);
			if (criterionNode instanceof Element) {
				criteriaList.add(createCriterion((Element) criterionNode));
			}
		}

		return criteriaList;
	}

	private Criterion createCriterion(Element criterionElement) {
		NodeList criterionTags = criterionElement.getChildNodes();
		Criterion newCriterion = new Criterion();

		for (int i = 0; i < criterionTags.getLength(); i++) {
			Node criterionTagNode = criterionTags.item(i);
			if (criterionTagNode instanceof Element) {
				Element criterionTagElement = (Element) criterionTagNode;

				if (criterionTagElement.getNodeName().equals("name")) {
					newCriterion.setName(criterionTagElement.getTextContent());
				}

				if (criterionTagElement.getNodeName().equals("description")) {
					newCriterion.setDescription(criterionTagElement
							.getTextContent());
				}

				if (criterionTagElement.getNodeName().equals("type")) {
					if (criterionTagElement.getTextContent().equals(
							"quantitative")) {
						newCriterion.setType(CriterionType.QUANTITATIVE);
					} else if (criterionTagElement.getTextContent().equals(
							"qualitative")) {
						newCriterion.setType(CriterionType.QUALITATIVE);
					} else if (criterionTagElement.getTextContent().equals(
							"benchmark")) {
						newCriterion.setType(CriterionType.BENCHMARK);
					}
				}

				if (criterionTagElement.getNodeName().equals("weight")
						&& !criterionTagElement.getTextContent().equals("")) {
					try {
						newCriterion.setWeight(Double
								.valueOf(criterionTagElement.getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				if (criterionTagElement.getNodeName().equals("children")) {
					if (criterionTagElement.hasChildNodes()) {
						NodeList criterionChildNodes = criterionTagElement
								.getChildNodes();
						for (int x = 0; x < criterionChildNodes.getLength(); x++) {
							Node criterionChildNode = criterionChildNodes
									.item(x);
							if (criterionChildNode instanceof Element) {
								newCriterion
										.addChild(createCriterion((Element) criterionChildNode));
							}
						}
					}
				}

				if (criterionTagElement.getNodeName().equals(
						"importanceChildren")) {
					if (criterionTagElement.hasChildNodes()) {
						parseImportanceChildren(
								criterionTagElement.getChildNodes(),
								newCriterion);
					}
				}
			}
		}
		log.info("Parsed Criterion: " + newCriterion.toString());
		return newCriterion;
	}

	private void parseImportanceChildren(NodeList importanceChildrenNodes,
			Criterion newCriterion) {
		for (int i = 0; i < importanceChildrenNodes.getLength(); i++) {
			Node node = importanceChildrenNodes.item(i);

			CriterionImportance criterionImportance = new CriterionImportance();

			if (node instanceof Element) {
				Element child = (Element) node;

				NodeList critA = child.getElementsByTagName("critA");
				if (critA.getLength() > 0) {
					try {
						criterionImportance.setCritA(Integer.valueOf(critA
								.item(0).getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList critB = child.getElementsByTagName("critB");
				if (critB.getLength() > 0) {
					try {
						criterionImportance.setCritB(Integer.valueOf(critB
								.item(0).getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList comparisonAToB = child
						.getElementsByTagName("comparisonAToB");
				if (comparisonAToB.getLength() > 0) {
					try {
						criterionImportance.setComparisonAToB(Double
								.valueOf(comparisonAToB.item(0)
										.getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList comment = child.getElementsByTagName("comment");
				if (comment.getLength() > 0) {
					criterionImportance.setComment(comment.item(0)
							.getTextContent());
				}

				newCriterion.insertImportanceChild(criterionImportance);
				log.info("Parsed criterionImportance: "
						+ criterionImportance.getCritA() + ", "
						+ criterionImportance.getCritB() + ", "
						+ criterionImportance.getComparisonAToB() + ", "
						+ criterionImportance.getComment());
			}
		}
	}

	private void parseGoalImportances(NodeList goalImportanceNodes,
			Decision newDecision) {
		for (int i = 0; i < goalImportanceNodes.getLength(); i++) {
			Node node = goalImportanceNodes.item(i);

			GoalImportance goalImportance = new GoalImportance();

			if (node instanceof Element) {
				Element child = (Element) node;

				NodeList critA = child.getElementsByTagName("critA");
				if (critA.getLength() > 0) {
					try {
						goalImportance.setCritA(Integer.valueOf(critA.item(0)
								.getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList critB = child.getElementsByTagName("critB");
				if (critB.getLength() > 0) {
					try {
						goalImportance.setCritB(Integer.valueOf(critB.item(0)
								.getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList comparisonAToB = child
						.getElementsByTagName("comparisonAToB");
				if (comparisonAToB.getLength() > 0) {
					try {
						goalImportance.setComparisonAToB(Double
								.valueOf(comparisonAToB.item(0)
										.getTextContent()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				NodeList comment = child.getElementsByTagName("comment");
				if (comment.getLength() > 0) {
					goalImportance.setComment(comment.item(0).getTextContent());
				}
				newDecision.insertImportanceGoal(goalImportance);
				log.info("Parsed goalImportance: " + goalImportance.getCritA()
						+ ", " + goalImportance.getCritB() + ", "
						+ goalImportance.getComparisonAToB() + ", "
						+ goalImportance.getComment());
			}
		}
	}

}
