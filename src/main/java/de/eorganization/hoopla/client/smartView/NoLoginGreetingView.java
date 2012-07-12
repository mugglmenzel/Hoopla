package de.eorganization.hoopla.client.smartView;



public class NoLoginGreetingView extends AbstractView {


	public NoLoginGreetingView() {
		super(false, false, false);
		getHeading().setContents("Welcome to the Hoopla");
		getInstructions().setContents("Please log in to use Aotearoa!");
		getPostit().setContents("Please log in to use Aotearoa!");

	}

	@Override
	public void goBack() {
	}

	@Override
	public void goNext() {
	}

	@Override
	public void refresh() {
	}

	@Override
	public void asTemplate() {
	}

}