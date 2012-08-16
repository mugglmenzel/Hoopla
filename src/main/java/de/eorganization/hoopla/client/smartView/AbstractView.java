package de.eorganization.hoopla.client.smartView;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;

import de.eorganization.hoopla.client.Hoopla;
import de.eorganization.hoopla.client.observer.Observable;

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
 *         Revision: $Revision: 216 $
 * 
 *         Date: $Date: 2011-09-15 16:36:49 +0200 (Do, 15 Sep 2011) $
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
 *         src/main/java/de/fzi/aotearoa/client/smartView/AbstractView.java $
 * 
 */

public abstract class AbstractView implements IView {

	private Layout layout = new VLayout();

	private Label heading = new Label();

	private Label instructions = new Label();

	private Layout content = new VLayout();

	private Canvas navigation = new Canvas();

	private Label postit = new Label();

	private Label postitHeader = new Label();

	public AbstractView() {
		this(true, true, true);
	}

	public AbstractView(String backLabel, String nextLabel, String templateLabel) {
		this(true, true, true, backLabel, nextLabel, templateLabel, -1, -1, -1);
	}

	public AbstractView(boolean showBackButton, boolean shownextButton,
			boolean showTemplateButton) {
		this(showBackButton, shownextButton, showTemplateButton, "Go Back!",
				"Save and Next Step!", "Save as Template!", -1, -1, -1);
	}

	public AbstractView(int backNo, int nextNo) {
		this(true, true, true, "Go Back!", "Save and Next Step!",
				"Save as Template!", backNo, nextNo, nextNo);
	}

	public AbstractView(String backLabel, String nextLabel,
			String templateLabel, int backNo, int nextNo, int templateNo) {
		this(true, true, true, backLabel, nextLabel, templateLabel, backNo,
				nextNo, templateNo);
	}

	/**
	 * 
	 */
	public AbstractView(boolean showBackButton, boolean showNextButton,
			boolean showTemplateButton, String backLabel, String nextLabel,
			String templateLabel, int backNo, int nextNo, int templateNo) {
		super();
		setLayout(new VLayout());
		getLayout().setWidth100();
		getLayout().setHeight100();

		HLayout tabContentLayout = new HLayout();
		tabContentLayout.setWidth100();
		tabContentLayout.setAlign(VerticalAlignment.TOP);

		VLayout contentLayout = new VLayout();
		contentLayout.setWidth(600);
		contentLayout.setMaxWidth(600);
		contentLayout.setAutoHeight();
		VLayout postitLayout = new VLayout();
		postitLayout.setWidth(400);
		postitLayout.setHeight100();
		postitLayout.setSnapTo("TL");
		postitLayout.setAlign(Alignment.LEFT);

		tabContentLayout.addMember(contentLayout);
		tabContentLayout.addMember(postitLayout);

		getHeading().setStyleName("heading");
		getHeading().setHeight(30);
		getHeading().setMargin(10);
		getInstructions().setStyleName("instructions");
		getInstructions().setHeight(15);
		getInstructions().setMargin(10);
		createNavigation(showBackButton, showNextButton, showTemplateButton,
				backLabel, nextLabel, templateLabel, backNo, nextNo, templateNo);
		getNavigation().setMargin(10);

		VLayout help = new VLayout();
		help.setBackgroundImage("/images/post-it.png");
		help.setBackgroundRepeat(BkgndRepeat.NO_REPEAT);
		help.setWidth(349);
		help.setHeight(389);
		help.setLayoutAlign(VerticalAlignment.TOP);

		getPostitHeader().setValign(VerticalAlignment.TOP);
		getPostitHeader().setAutoHeight();
		getPostitHeader().setMaxWidth(300);
		getPostitHeader().setStyleName("postitHeader");
		getPostitHeader().setContents("Help");
		getPostitHeader().setIcon("/images/help.png");

		getPostit().setValign(VerticalAlignment.TOP);
		getPostit().setAutoHeight();
		getPostit().setMaxWidth(300);
		getPostit().setStyleName("postit");

		help.addMember(getPostitHeader());
		help.addMember(getPostit());

		contentLayout.addMember(getHeading());
		contentLayout.addMember(getInstructions());
		contentLayout.addMember(getContent());
		contentLayout.addMember(getNavigation());

		postitLayout.addMember(help);

		getLayout().addMember(tabContentLayout);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getLayout()
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * @param layout
	 *            the layout to set
	 */
	private void setLayout(Layout layout) {
		this.layout = layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.eorganization.hoopla.client.smartView.IView#setHeading(com.smartgwt.client
	 * .widgets.Label)
	 */
	public void setHeading(Label heading) {
		this.heading = heading;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getHeading()
	 */
	public Label getHeading() {
		return heading;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.eorganization.hoopla.client.smartView.IView#setInstructions(com.smartgwt.client
	 * .widgets.Label)
	 */
	public void setInstructions(Label instructions) {
		this.instructions = instructions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getInstructions()
	 */
	public Label getInstructions() {
		return instructions;
	}

	/**
	 * @param navigation
	 *            the navigation to set
	 */
	private void setNavigation(Canvas navigation) {
		this.navigation = navigation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getNavigation()
	 */
	public Canvas getNavigation() {
		return navigation;
	}

	private void createNavigation(boolean showBackButton,
			boolean showNextButton, boolean showTemplateButton,
			String backLabel, String nextLabel, String templateLabel,
			final int backNo, final int nextNo, final int templateNo) {

		IButton back = new IButton();
		back.setTitle(backLabel);
		back.setLayoutAlign(Alignment.LEFT);
		back.setDisabled(!showBackButton);
		back.setAutoFit(true);
		if (showBackButton) {
			back.setIcon("/images/arrow_left.png");
		}
		back.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				goBack();
				previousTab(backNo);
			}
		});

		IButton templateAndNext = new IButton();
		templateAndNext.setTitle(templateLabel);
		templateAndNext.setLeft(300);
		templateAndNext.setLayoutAlign(Alignment.RIGHT);
		templateAndNext.setDisabled(!showTemplateButton);
		templateAndNext.setAutoFit(true);
		templateAndNext.setIcon("/images/arrow_right.png");
		templateAndNext.setIconOrientation("right");
		templateAndNext.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				asTemplate();
				nextTab(templateNo);
			}

		});

		IButton saveAndNext = new IButton();
		saveAndNext.setTitle(nextLabel);
		saveAndNext.setLeft(500);
		saveAndNext.setLayoutAlign(Alignment.RIGHT);
		saveAndNext.setDisabled(!showNextButton);
		saveAndNext.setAutoFit(true);
		saveAndNext.setIcon("/images/arrow_right.png");
		saveAndNext.setIconOrientation("right");
		saveAndNext.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				goNext();
				nextTab(nextNo);
			}
		});

		Canvas navigation = new Canvas();
		navigation.setWidth(500);
		navigation.setHeight(saveAndNext.getHeight());
		navigation.addChild(back);
		navigation.addChild(templateAndNext);
		navigation.addChild(saveAndNext);

		setNavigation(navigation);
	}

	/**
	 * @param content
	 *            the content to set
	 */
	private void setContent(Layout content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getContent()
	 */
	public Layout getContent() {
		return content;
	}

	/**
	 * @param postit
	 *            the postit to set
	 */
	private void setPostit(Label postit) {
		this.postit = postit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getPostit()
	 */
	public Label getPostit() {
		return postit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.eorganization.hoopla.client.smartView.IView#setPostitHeader(com.smartgwt.client
	 * .widgets.Label)
	 */
	public void setPostitHeader(Label postitHeader) {
		this.postitHeader = postitHeader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#getPostitHeader()
	 */
	public Label getPostitHeader() {
		return postitHeader;
	}

	public void nextTab(final int nextNo) {
		Hoopla.tabs.selectTab(nextNo != -1 ? nextNo : Hoopla.tabs
				.getSelectedTabNumber() + 1);
		Hoopla.tabs.getSelectedTab().fireEvent(
				new TabSelectedEvent(null));
	}

	public void previousTab(final int prevNo) {
		Hoopla.tabs.selectTab(prevNo != -1 ? prevNo : Hoopla.tabs
				.getSelectedTabNumber() - 1);
		Hoopla.tabs.getSelectedTab().fireEvent(
				new TabSelectedEvent(null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#goNext()
	 */
	public abstract void goNext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#asTemplateAndGoNext()
	 */
	public abstract void asTemplate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#goBack()
	 */
	public abstract void goBack();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eorganization.hoopla.client.smartView.IView#update()
	 */
	public abstract void refresh();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		refresh();
	}

}
