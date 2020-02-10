package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.imageio.ImageIO;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * The pane showed when the nobody in the camera can be recognized
 * @author Yunzhou Ning
 *
 */
public class Alert extends BorderPane {
	//combo box for selecting reasons
	final ComboBox<String> reasonComboBox = new ComboBox();
	//picture to be saved for new student
	Image newPicture;

	public Alert(ImageView picture) {
		//Set the top banner
		ImageView alertImage = new ImageView("alert.png");
		//Set the combo box
		reasonComboBox.getItems().addAll("meet faculty", "pay tution", "borrow tools", "complaint", "query",
				"collect assignement");
		newPicture = picture.getImage();
		reasonComboBox.setPromptText("Visit reason");
		reasonComboBox.setEditable(true);
		BorderPane.setAlignment(alertImage, Pos.CENTER);
		setTop(alertImage);
		//Set the labels and buttons
		Label label = new Label("This person cannot be recognized, do you want to add this person to database?");
		label.setFont(new Font("Arial", 20));
		label.setPadding(new Insets(30));
		setCenter(label);
		Button btCancel = new Button("Cancel");
		//close the window when cancel is clicked
		btCancel.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				Stage currentStage = (Stage) (btCancel.getScene().getWindow());
				currentStage.close();
			}
		});
		//set an action for the yes button
		Button btYes = new Button("Yes");
		BtYesHandler btYesHandler = new BtYesHandler();
		btYes.setOnAction(btYesHandler);
		HBox hbox = new HBox(30);
		hbox.getChildren().add(btCancel);
		hbox.getChildren().add(btYes);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(30));
		setBottom(hbox);
	}

	class BtYesHandler implements EventHandler<ActionEvent> {
		//textfield for inputing information
		TextField tfName = new TextField();
		TextField tfGender = new TextField();
		TextField tfProgram = new TextField();
		Button btSubmit;
		Stage addInfoStage;

		// Override the handle method
		public void handle(ActionEvent e) {
			//Set the pane
			GridPane addInfoPane = new GridPane();
			addInfoPane.setPadding(new Insets(30));

			addInfoPane.add(new Label("Name: "), 0, 0);
			addInfoPane.add(new Label("Gender: "), 0, 1);
			addInfoPane.add(new Label("Program: "), 0, 2);
			addInfoPane.add(new Label("Visit reason"), 0, 3);
			addInfoPane.add(tfName, 1, 0);
			addInfoPane.add(tfGender, 1, 1);
			addInfoPane.add(tfProgram, 1, 2);
			addInfoPane.add(reasonComboBox, 1, 3);
			btSubmit = new Button("Submit");
			addInfoPane.add(btSubmit, 1, 4);
			//set the action for the submit button
			BtSubmitHandler btSubmitHandler = new BtSubmitHandler();
			btSubmit.setOnAction(btSubmitHandler);
			addInfoStage = new Stage();
			Scene addInfoScene = new Scene(addInfoPane);
			addInfoStage.setScene(addInfoScene);
			addInfoStage.setTitle("Add New Student");
			addInfoStage.show();
			Stage currentStage = (Stage) (getScene().getWindow());
			currentStage.close();

		}
		// pop up when submit is clicked
		class BtSubmitHandler implements EventHandler<ActionEvent> {
			// Override the handle method
			public void handle(ActionEvent e) {
				//get the info from the textfield and comboBox, which are the input of the users
				String name = tfName.getText();
				String gender = tfGender.getText();
				String program = tfProgram.getText();
				String visitReason = reasonComboBox.getValue().toString();
				//if input not complete, pop up a new window to alert user
				if(name.equals("")|| gender.equals("") || program.equals("") || visitReason.equals("")) {
					ErrorHandling err = new ErrorHandling("The information you entered are not complete");
					Stage stage = new Stage();
					Scene scene = new Scene(err);
					stage.setScene(scene);
					stage.show();
				}
				// else, write the information into the database
				else {
					//connections to database
					StudentDAOImpl stuController = new StudentDAOImpl();
					RecordDAOImpl recordController = new RecordDAOImpl();
					try {
						//save the imageview locally as a png file
						File outputFile = new File(
								"/Users/cloudyboat/Desktop/Java/teamProject/openCV_Recognition/bin/image/" + name + ".png");

						BufferedImage bImage = SwingFXUtils.fromFXImage(newPicture, null);

						try {
							ImageIO.write(bImage, "png", outputFile);

						} catch (IOException e1) {
							throw new RuntimeException(e1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//allocate the new id 
						int newID = stuController.IDCount().size() + 1;
						//save to the database
						stuController.addStudent(newID, name, gender,
								"/Users/cloudyboat/Desktop/Java/teamProject/openCV_Recognition/bin/image/" + name + ".png",
								program, 1);
						recordController.updateRecord(newID, visitReason, new Timestamp(System.currentTimeMillis()));

					} catch (Exception e1) {

						e1.printStackTrace();
					}
					// pop up a new stage when succeed and close the current window
					Stage succeedStage = new Stage();
					Label succeed = new Label("Succeeded!");
					succeed.setFont(new Font("Arial", 20));
					succeed.setPadding(new Insets(20));
					VBox succeedPane = new VBox(30);
					succeedPane.setPadding(new Insets(20));
					succeedPane.setAlignment(Pos.CENTER);
					succeedPane.getChildren().add(succeed);
					Button btok = new Button("OK");
					succeedPane.getChildren().add(btok);
					btok.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							succeedStage.close();
						}
					});
					Scene succeedScene = new Scene(succeedPane);
					succeedStage.setScene(succeedScene);
					addInfoStage.close();
					succeedStage.show();
				}
				
			}
		}
	}

}
