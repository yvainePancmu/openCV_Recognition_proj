package application;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * Pane showed when the error occurs
 * @author Yunzhou Ning
 *
 */
public class ErrorHandling extends BorderPane{
	public ErrorHandling() {
		this("Some Error occur, try again");
	}
	
		public ErrorHandling(String info) {
			Image image = new Image("alert.png");
			ImageView reminderImage = new ImageView(image);
			BorderPane.setAlignment(reminderImage, Pos.CENTER);
			setTop(reminderImage);
			Label label = new Label("You have an error:\n" + info);
			label.setFont(new Font("Arial",20));
			label.setPadding(new Insets(40));
			setCenter(label);
			Button btCancel = new Button("Cancel");
			btCancel.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent ae){
				Stage currentStage = (Stage)(btCancel.getScene().getWindow());
				currentStage.close();
				}
				});
			HBox hbox = new HBox(30);
			hbox.getChildren().add(btCancel);
			hbox.setAlignment(Pos.CENTER);
			hbox.setPadding(new Insets(30));
			setBottom(hbox);
		}}



    