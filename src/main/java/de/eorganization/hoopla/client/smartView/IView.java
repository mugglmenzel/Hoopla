package de.eorganization.hoopla.client.smartView;



import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.Layout;

import de.eorganization.hoopla.client.observer.Observer;

public interface IView extends Observer {

	/**
	 * @return the layout
	 */
	public Layout getLayout();

	/**
	 * @param heading
	 *            the heading to set
	 */
	public void setHeading(Label heading);

	/**
	 * @return the heading
	 */
	public Label getHeading();

	/**
	 * @param instructions
	 *            the instructions to set
	 */
	public void setInstructions(Label instructions);

	/**
	 * @return the instructions
	 */
	public Label getInstructions();

	/**
	 * @return the navigation
	 */
	public Canvas getNavigation();

	/**
	 * @return the content
	 */
	public Layout getContent();

	/**
	 * @return the postit
	 */
	public Label getPostit();

	/**
	 * @param postitHeader the postitHeader to set
	 */
	public void setPostitHeader(Label postitHeader);

	/**
	 * @return the postitHeader
	 */
	public Label getPostitHeader();

	/**
	 * Method executed when next button is clicked.
	 */
	public void goNext();

	/**
	 * Method executed when "as template" button is clicked.
	 */
	public void asTemplate();

	/**
	 * Method executed when back button is clicked.
	 */
	public void goBack();

	/**
	 * Method executed to refresh view.
	 */
	public void refresh();

}