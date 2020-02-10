package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * Pane for user to select date and type of report
 * @author Yunzhou Ning
 *
 */

public class ReportOrigin extends VBox {
	//combo boxes for date selection
	final ComboBox<String> dateComboBox1 = new ComboBox();
	final ComboBox<String> dateComboBox2 = new ComboBox();
	public ReportOrigin() {
		//set the combo boxes
		setAlignment(Pos.CENTER);
		setSpacing(30);
		setPadding(new Insets(0, 0, 30, 0));
		ImageView imageView = new ImageView("banner.jpg");
		imageView.setFitHeight(200);
		imageView.setFitWidth(800);
		getChildren().add(imageView);
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		Label from = new Label("From: ");
		Label to = new Label("TO: ");
		to.setPadding(new Insets(0, 0, 0, 20));
		String[] dateArray = new String[30];
		//set the date range
		for (int i = 1; i < 10; i++) {
			dateArray[i - 1] = "2019-11-0" + i;
		}
		for (int i = 10; i < 31; i++) {
			dateArray[i - 1] = "2019-11-" + i;
		}
		dateComboBox1.getItems().addAll(dateArray);
		dateComboBox2.getItems().addAll(dateArray);
		hbox.getChildren().addAll(from, dateComboBox1, to, dateComboBox2);
		getChildren().add(hbox);
		//set two buttons for two different report
		Button categoryReport = new Button("Category Report");
		CategoryReportHandler categoryReportHandler = new CategoryReportHandler();
		categoryReport.setOnAction(categoryReportHandler);
		getChildren().add(categoryReport);
		Button frequencyReport = new Button("Frequency Report");
		FrequencyReportHandler frequencyReportHandler = new FrequencyReportHandler();
		frequencyReport.setOnAction(frequencyReportHandler);
		getChildren().add(frequencyReport);
	}
	//button for pop up category report
	class CategoryReportHandler implements EventHandler<ActionEvent> {
		// Override the handle method
		public void handle(ActionEvent e) {
			// should be written in the database
			String[] result = getDate();
			if (result != null) {
				String start = result[0];
				String end = result[1];
				try {
					Stage categoryReportStage = new Stage();
					CategoryReport categoryReportPane = new CategoryReport(start, end);
					Scene categoryReportScene = new Scene(categoryReportPane);
					categoryReportStage.setScene(categoryReportScene);
					categoryReportStage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}
	//button for pop up frequency report
	class FrequencyReportHandler implements EventHandler<ActionEvent> {
		// Override the handle method
		public void handle(ActionEvent e) {
			String[] result = getDate();
			if (result != null) {
				String start = result[0];
				String end = result[1];
				try {
					Stage frequencyReportStage = new Stage();
					FrequencyReport frequencyReportPane = new FrequencyReport(start, end);
					Scene frequencyReportScene = new Scene(frequencyReportPane);
					frequencyReportStage.setScene(frequencyReportScene);
					frequencyReportStage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}
	//get the date range from the combo boxes and use them to determine the date range for the report
	public String[] getDate() {
		String start;
		String end;
		if (dateComboBox1.getValue() != null && !dateComboBox1.getValue().toString().isEmpty()
				&& dateComboBox2.getValue() != null && !dateComboBox2.getValue().toString().isEmpty()) {
			// reason should be written into database
			start = dateComboBox1.getValue().toString();
			end = dateComboBox2.getValue().toString();
			//make sure that user pick the right date
			if(start.compareTo(end) < 0) {
				dateComboBox1.setValue(null);
				dateComboBox2.setValue(null);
				return new String[] { start, end };
			}
			else
			{
				ErrorHandling err = new ErrorHandling("The end date should bigger than berginning");
				Stage stage = new Stage();
				Scene scene = new Scene(err);
				stage.setScene(scene);
				stage.show();
				return null;
			}
				
		} else
			return null;
	}
}
