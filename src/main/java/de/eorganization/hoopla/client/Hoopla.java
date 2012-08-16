package de.eorganization.hoopla.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

import de.eorganization.hoopla.client.gui.TopLayout;
import de.eorganization.hoopla.client.gui.canvas.LoginWindow;
import de.eorganization.hoopla.client.gui.canvas.RegisterWindow;
import de.eorganization.hoopla.client.observer.events.DecisionUpdatedEvent;
import de.eorganization.hoopla.client.services.HooplaService;
import de.eorganization.hoopla.client.services.HooplaServiceAsync;
import de.eorganization.hoopla.client.services.LoginService;
import de.eorganization.hoopla.client.services.LoginServiceAsync;
import de.eorganization.hoopla.client.smartView.AdminView;
import de.eorganization.hoopla.client.smartView.AlternativesEvaluationView;
import de.eorganization.hoopla.client.smartView.AlternativesView;
import de.eorganization.hoopla.client.smartView.CriteriaEvaluationView;
import de.eorganization.hoopla.client.smartView.GoalsAndCriteriaView;
import de.eorganization.hoopla.client.smartView.IView;
import de.eorganization.hoopla.client.smartView.NewDecisionView;
import de.eorganization.hoopla.client.smartView.NoLoginGreetingView;
import de.eorganization.hoopla.client.smartView.RequirementsView;
import de.eorganization.hoopla.client.smartView.ResultView;
import de.eorganization.hoopla.client.smartView.WelcomeView;
import de.eorganization.hoopla.client.subjects.DecisionUpdatedSubject;
import de.eorganization.hoopla.shared.model.LoginInfo;
import de.eorganization.hoopla.shared.model.Member;
import de.eorganization.hoopla.shared.model.UserRole;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;

/**
 * 
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel $
 * 
 *         Revision: $Revision: 273 $
 * 
 *         Date: $Date: 2012-05-02 15:13:24 +0200 (Mi, 02 Mai 2012) $
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
 *         src/main/java/de/fzi/aotearoa/client/Hoopla.java $
 * 
 */

public class Hoopla implements EntryPoint {

	public static TabSet tabs = new TabSet();

	public static List<IView> views = new ArrayList<IView>();

	public static HooplaServiceAsync hooplaService = GWT
			.create(HooplaService.class);

	public static LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public static de.eorganization.hoopla.shared.model.LoginInfo loginInfo = new LoginInfo();

	public static Decision decision = new Decision();

	public static DecisionUpdatedSubject decisionUpdatedSubject = new DecisionUpdatedSubject();

	public void onModuleLoad() {
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						DOM.setStyleAttribute(RootPanel.get("loading")
								.getElement(), "display", "none");

						loginInfo = result;

						if (!loginInfo.isLoggedIn()) {
							createWelcomeLayout();
							if (getMember() != null) {
								new RegisterWindow(getMember()).show();
							}
						} else
							createMasterLayout();

					}
				});

	}

	private void createWelcomeLayout() {
		final Layout masterLayout = new VLayout();

		masterLayout.addMember(new TopLayout(loginInfo));

		VLayout welcomeInfo = new VLayout(10);
		welcomeInfo.setDefaultLayoutAlign(Alignment.CENTER);
		welcomeInfo.setAlign(VerticalAlignment.TOP);
		welcomeInfo.setHeight100();
		welcomeInfo.setWidth100();
		welcomeInfo.setBackgroundColor("#ffffff");

		Label welcomeLabel = new Label(
				"<h1 style=\"font-size: 40pt\">Welcome!</h1><p style=\"font-size: 20pt\"><span style=\"font-weight: bolder;\">Hoopla</span> <span style=\"font-style: italic; font-weight: bolder;\">n.</span> is  ... .</p><p style=\"font-size: 20pt\">Feel free to play Hoopla and <a href=\"http://myownthemepark.com/prices-table/\">contact us for a premium account</a>. Simply sign in with a social loginInfo account (Facebook, Twitter, Google+).</p>");
		welcomeLabel.setWidth(600);
		welcomeLabel.setStyleName("welcome");

		Label loginAnchor = new Label(
				"<span style=\"font-size: 35pt; cursor: pointer; text-decoration: underline;\">Login</span>");
		loginAnchor.setAutoFit(true);
		loginAnchor.setWrap(false);
		loginAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new LoginWindow(loginInfo.getLoginUrl()).show();
			}
		});

		welcomeInfo.addMember(welcomeLabel);
		welcomeInfo.addMember(loginAnchor);

		masterLayout.addMember(welcomeInfo);

		masterLayout.setWidth100();
		masterLayout.setHeight100();
		masterLayout.setMaxHeight(700);
		masterLayout.draw();

	}

	private void createMasterLayout() {

		Layout masterLayout = new VLayout();
		masterLayout.setWidth100();
		masterLayout.setHeight100();

		tabs.setWidth100();
		tabs.setHeight100();
		tabs.setBackgroundColor("white");

		if (!loginInfo.isLoggedIn()) {
			Tab greeting = new Tab("Welcome to the Hoopla");
			final NoLoginGreetingView greetingsView = new NoLoginGreetingView();
			greeting.setPane(greetingsView.getLayout());
			greeting.addTabSelectedHandler(new TabSelectedHandler() {

				@Override
				public void onTabSelected(TabSelectedEvent event) {
					greetingsView.refresh();
				}
			});
			tabs.addTab(greeting);

			views.add(greetingsView);
		} else {

			Tab greeting = new Tab("Welcome to the Hoopla");
			/*
			 * final GreetingView greetingsView = new GreetingView();
			 * greeting.setPane(greetingsView.getLayout());
			 * greeting.addTabSelectedHandler(new TabSelectedHandler() { public
			 * void onTabSelected(TabSelectedEvent event) {
			 * greetingsView.refresh(); } }); tabs.addTab(greeting);
			 * views.add(greetingsView);
			 */
			final WelcomeView welcomeView = new WelcomeView();
			greeting.setPane(welcomeView.getLayout());
			greeting.addTabSelectedHandler(new TabSelectedHandler() {
				public void onTabSelected(TabSelectedEvent event) {
					welcomeView.refresh();
				}
			});
			tabs.addTab(greeting);
			views.add(welcomeView);

			Tab newDecision = new Tab("1. New Cloud Decision");
			final NewDecisionView decisionView = new NewDecisionView();
			newDecision.setPane(decisionView.getLayout());
			newDecision.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					decisionView.refresh();
				}
			});
			tabs.addTab(newDecision);
			views.add(decisionView);

			Tab alternatives = new Tab("2. Alternatives");
			final AlternativesView altView = new AlternativesView();
			alternatives.setPane(altView.getLayout());
			alternatives.addTabSelectedHandler(new TabSelectedHandler() {
				public void onTabSelected(TabSelectedEvent event) {
					altView.refresh();
				}
			});
			tabs.addTab(alternatives);
			views.add(altView);

			Tab goalsCriteria = new Tab("3. Goals & Criteria");
			final GoalsAndCriteriaView goalsCriteriaView = new GoalsAndCriteriaView();
			goalsCriteria.setPane(goalsCriteriaView.getLayout());
			goalsCriteria.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					goalsCriteriaView.refresh();
				}
			});
			tabs.addTab(goalsCriteria);
			views.add(goalsCriteriaView);

			final Tab criteriaEval = new Tab("4. Goals/Criteria Importance");
			final CriteriaEvaluationView critEvalView = new CriteriaEvaluationView();
			criteriaEval.setPane(critEvalView.getLayout());
			criteriaEval.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					critEvalView.refresh();
				}
			});
			tabs.addTab(criteriaEval);
			views.add(critEvalView);

			final Tab requirements = new Tab("5. Requirements");
			final RequirementsView reqView = new RequirementsView();
			requirements.setPane(reqView.getLayout());
			requirements.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					critEvalView.refresh();
				}
			});
			tabs.addTab(requirements);
			views.add(reqView);

			final Tab alternativesEval = new Tab("6. Alternatives Evaluation");
			final AlternativesEvaluationView altEvalView = new AlternativesEvaluationView();
			alternativesEval.setPane(altEvalView.getLayout());
			alternativesEval.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					altEvalView.refresh();
				}
			});
			tabs.addTab(alternativesEval);
			views.add(altEvalView);

			Tab results = new Tab("7. Results");
			final ResultView resultsView = new ResultView();
			results.setPane(resultsView.getLayout());
			results.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					resultsView.refresh();
				}
			});
			tabs.addTab(results);
			views.add(resultsView);
		}
		if (loginInfo.getMember() != null
				&& UserRole.ADMIN.equals(loginInfo.getMember().getRole())) {
			Tab admin = new Tab("admin");
			final AdminView adminView = new AdminView();
			admin.setPane(adminView.getLayout());
			admin.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					adminView.refresh();
				}
			});
			tabs.addTab(admin);
			views.add(adminView);
		}

		masterLayout.addMember(new TopLayout(loginInfo));
		masterLayout.addMember(tabs);

		masterLayout.draw();

	}

	public static void updateDecision(Decision d) {
		decision = d;
		// evaluation.update(decision);
		decisionUpdatedSubject.raiseUpdatedEvent(new DecisionUpdatedEvent(
				decision));
	}

	private Member getMember() {
		return loginInfo != null ? loginInfo.getMember() : null;
	}
}
