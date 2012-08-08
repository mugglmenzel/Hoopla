package de.eorganization.hoopla.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

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

	public static de.eorganization.hoopla.shared.model.LoginInfo user = new LoginInfo();

	public static Decision decision = new Decision();

	public static DecisionUpdatedSubject decisionUpdatedSubject = new DecisionUpdatedSubject();

	public void onModuleLoad() {
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						user = result;

						createMasterLayout();
					}
				});

	}

	private void createMasterLayout() {

		Layout masterLayout = new VLayout();
		masterLayout.setWidth100();
		masterLayout.setHeight100();

		// Canvas topBlock = new Canvas();
		// topBlock.setHeight(5);

		final Layout top = new HLayout();
		top.setWidth100();
		top.setBackgroundImage("/clouds.png");
		top.setBackgroundPosition("bottom");
		top.setBackgroundRepeat(BkgndRepeat.REPEAT_X);
		top.setAlign(Alignment.LEFT);

		Anchor hooplaLogo = new Anchor(new SafeHtmlBuilder().appendHtmlConstant(Canvas.imgHTML("/hoopla_logo.png", 353, 150)).toSafeHtml(), GWT.getHostPageBaseURL(), "_top");
		//hooplaLogo.setLayoutAlign(VerticalAlignment.TOP);
		/*hooplaLogo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL());
			}
		});*/
		//hooplaLogo.setHoverStyle("link");
		//hooplaLogo.addStyleName("link");
		HLayout login = new HLayout();
		login.setAlign(Alignment.RIGHT);
		login.setMembersMargin(15);

		top.addMember(hooplaLogo);
		top.addMember(login);

		if (!user.isLoggedIn()) {
			login.addMember(new Anchor(new SafeHtmlBuilder()
					.appendHtmlConstant(
							"<span style=\"font-size: 20pt\">login</span>")
					.toSafeHtml(), user.getLoginUrl()));
		} else {
			login.addMember(new Label("<span style=\"font-size: 20pt\">"
					+ user.getMember().getNickname() + " </span> "));
			login.addMember(new Anchor(new SafeHtmlBuilder()
					.appendHtmlConstant(
							"<span style=\"font-size: 20pt\">logout</span>")
					.toSafeHtml(), user.getLogoutUrl()));
		}

		tabs.setWidth100();
		tabs.setHeight100();
		tabs.setBackgroundColor("white");

		if (!user.isLoggedIn()) {
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
		if (user.getMember() != null
				&& UserRole.ADMIN.equals(user.getMember().getRole())) {
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

		masterLayout.addMember(top);
		masterLayout.addMember(tabs);

		masterLayout.draw();

	}

	public static void updateDecision(Decision d) {
		decision = d;
		// evaluation.update(decision);
		decisionUpdatedSubject.raiseUpdatedEvent(new DecisionUpdatedEvent(
				decision));
	}
}
