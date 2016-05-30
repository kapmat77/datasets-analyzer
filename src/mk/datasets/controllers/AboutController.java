package mk.datasets.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by Kapmat on 2016-05-30.
 */
public class AboutController {

	@FXML private Label aboutLabel;

	public Label getAboutLabel() {
		return aboutLabel;
	}

	public void setAboutLabel(Label aboutLabel) {
		this.aboutLabel = aboutLabel;
	}
}
