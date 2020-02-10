package application;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.GridPane;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * The window to show the Dashboard when people are recognized
 * @author Yunzhou Ning
 *
 */
public class DashBoard extends GridPane {
	TextField tfVisitReason = new TextField();
	final ComboBox<String> reasonComboBox = new ComboBox();
	//set the tables and data for tables for announcements and records
	private ObservableList<AnnouncementProperty> announcementData;
	private ObservableList<RecordProperty> recordData;
	private final TableView announcementTable = new TableView();
	private final TableView recordTable = new TableView();
	//the unique image number for the image taken from camera
	private static int imageNo;
	private String sid;
	//the recognized student
	Student stu;
	//controllers for the tables in the database
	StudentDAOImpl stuController;
	AnnounceDAOImpl annController;
	RecordDAOImpl recordController;

	public DashBoard(String sid, ImageView imageView) {
		this.sid = sid;
		try {
			stuController = new StudentDAOImpl();
			annController = new AnnounceDAOImpl();
			recordController = new RecordDAOImpl();
			//find the student via sid
			stu = stuController.studentDetail(Integer.parseInt(sid));
			//set the banner
			ImageView banner = new ImageView("banner.jpg");
			banner.setFitHeight(200);
			banner.setFitWidth(800);
			add(banner, 0, 0);
			//Set the mainGridPane, which is the frame of the dashboard
			
			GridPane mainGridPane = new GridPane();
			mainGridPane.setAlignment(Pos.CENTER);
			mainGridPane.setPadding(new Insets(10.0));
			mainGridPane.setHgap(50);
			mainGridPane.setVgap(20);
			//get the image of the student and set it to the left top corner
			Image stuImage = new Image(stu.getImagePath());
			ImageView stuImageview = new ImageView(stuImage);
			stuImageview.setPreserveRatio(true);
			stuImageview.setFitHeight(200);
			StackPane imagePane = new StackPane(stuImageview);
			mainGridPane.add(imagePane, 0, 0);
			//get the general information of the stu, and display them on the right top corner
			GridPane infoGridPane = new GridPane();
			infoGridPane.setVgap(10);
			Label lbInfo = new Label("Information");
			lbInfo.setFont(new Font("Arial", 25));
			infoGridPane.add(lbInfo, 0, 0);
			VBox dataFields = new VBox(10);
			dataFields.getChildren().add(new Label("Name: "));
			dataFields.getChildren().add(new Label("Gender: "));
			dataFields.getChildren().add(new Label("Program: "));
			dataFields.getChildren().add(new Label("Visit times: "));
			dataFields.getChildren().add(new Label("Visit Reason: "));
			infoGridPane.add(dataFields, 0, 1);
			VBox information = new VBox(10);
			information.getChildren().add(new Label(stu.getStudentName()));
			information.getChildren().add(new Label(stu.getGender()));
			information.getChildren().add(new Label(stu.getStudentProgram()));
			information.getChildren().add(new Label(stu.getVisitTimes() + ""));
			// A new record should be created via visitRecordController
			//set the comboBox for user to select the visit reason for this time
			reasonComboBox.getItems().addAll("meet faculty", "pay tution", "borrow tools", "complaint", "query",
					"collect assignement");
			reasonComboBox.setPromptText("Visit reason");
			reasonComboBox.setEditable(true);
			information.getChildren().add(reasonComboBox);
			Button btSubmit = new Button("Submit");
			SubmitHandler submitHandler = new SubmitHandler();
			btSubmit.setOnAction(submitHandler);
			information.getChildren().add(btSubmit);
			infoGridPane.add(information, 1, 1);
			mainGridPane.add(infoGridPane, 1, 0);
			// set the announcement table to the middle left
			announcementTable.setEditable(true);
			announcementTable.setMaxHeight(200);
			Label announceLabel = new Label("Announcement");
			announceLabel.setFont(new Font("Arial", 20));
			VBox announcement = new VBox();
			announcement.setSpacing(5);
			announcement.setPadding(new Insets(10, 0, 0, 10));
			announcement.getChildren().addAll(announceLabel, announcementTable);
			//initialize the columns
			TableColumn anNumberCol = new TableColumn("Announcement No.");
			anNumberCol.setPrefWidth(150);
			anNumberCol.setCellValueFactory(new PropertyValueFactory<>("announcementNo"));

			TableColumn anContentCol = new TableColumn("Announcement Content");
			anContentCol.setCellValueFactory(new PropertyValueFactory<>("announcementContent"));
			anContentCol.setPrefWidth(180);
			//call the method to fetch the data from the database and set them into table
			setAnnouncementData();
			announcementTable.setItems(announcementData);
			announcementTable.getColumns().addAll(anNumberCol, anContentCol);
			mainGridPane.add(announcement, 0, 1);
			// set the Visit Record Table to the middle right
			recordTable.setEditable(true);
			recordTable.setMaxHeight(200);
			Label recordLabel = new Label("Visit Record");
			recordLabel.setFont(new Font("Arial", 20));
			VBox visitRecord = new VBox();
			visitRecord.setSpacing(5);
			visitRecord.setPadding(new Insets(10, 0, 0, 10));
			visitRecord.getChildren().addAll(recordLabel, recordTable);
			//initialize the record table
			TableColumn recordNoCol = new TableColumn("Record No.");
			recordNoCol.setPrefWidth(100);
			recordNoCol.setCellValueFactory(new PropertyValueFactory<>("recordID"));
			TableColumn recordReasonCol = new TableColumn("Reason");
			recordReasonCol.setCellValueFactory(new PropertyValueFactory<>("recordReason"));
			recordReasonCol.setPrefWidth(100);
			TableColumn recordDateCol = new TableColumn("Date");
			recordDateCol.setPrefWidth(100);
			recordDateCol.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
			//call the method to fetch the data from the database and set them into table
			setRecordData();
			recordTable.setItems(recordData);
			recordTable.getColumns().addAll(recordNoCol, recordReasonCol, recordDateCol);
			mainGridPane.add(visitRecord, 1, 1);
			// display the emotion at the left down corner
			VBox extraInfo = new VBox(10);
			Label emotionLabel = new Label("Emotion");
			emotionLabel.setFont(new Font("Arial", 20));
			extraInfo.getChildren().add(emotionLabel);
			//call the static method in class Emotion and get results in four emotions
			int[] emotion;
			emotion = getEmotion(imageView.getImage());
			GridPane labelGridPane = new GridPane();
			labelGridPane.setVgap(10);
			labelGridPane.setHgap(10);
			labelGridPane.add(new Label("Anger: "), 0, 0);
			labelGridPane.add(new Label("Joy: "), 0, 1);
			labelGridPane.add(new Label("Surprise: "), 0, 2);
			labelGridPane.add(new Label("Sorrow: "), 0, 3);
			labelGridPane.add(new Label(emotion[0] + "/5"), 1, 0);
			labelGridPane.add(new Label(emotion[1] + "/5"), 1, 1);
			labelGridPane.add(new Label(emotion[2] + "/5"), 1, 2);
			labelGridPane.add(new Label(emotion[3] + "/5"), 1, 3);
			labelGridPane.setPadding(new Insets(0, 0, 0, 80));
			extraInfo.getChildren().add(labelGridPane);
			mainGridPane.add(extraInfo, 0, 2);
			// display the piechart for visit reason of the student
			StackPane chartPane = new StackPane();
			PieChart pieChart = new PieChart();
			// set the data for the piechart
			pieChart.setData(getChartData());// get from database
			pieChart.setTitle("Visit Reasons");
			pieChart.setLegendSide(Side.RIGHT);
			pieChart.setClockwise(false);
			pieChart.setLabelsVisible(false);
			pieChart.setPrefSize(400, 150);
			chartPane.getChildren().add(pieChart);
			mainGridPane.add(chartPane, 1, 2);
			add(mainGridPane, 0, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// take the image from camera, save it locally and call the method in the Emotion class to get emotion values
	private int[] getEmotion(Image image) {
		File outputFile = new File("temp" + imageNo + ".png");
		int[] emotion = new int[4];
		BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		try {
			ImageIO.write(bImage, "png", outputFile);
			emotion = Emotion.detectFaces("temp" + imageNo + ".png");
			imageNo++;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emotion;
	}
	//fetch the data for the piechart
	private ObservableList<Data> getChartData() {
		ObservableList<Data> answer = FXCollections.observableArrayList();

		ArrayList<ReportResult> list;
		try {
			list = recordController.queryVisReason(Integer.parseInt(sid));
			for (int i = 0; i < list.size(); i++) {
				answer.add(new PieChart.Data(list.get(i).getVisReason(), list.get(i).getCategoryNum()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}
	//fetch the data for the announcement table
	private void setAnnouncementData() {
		try {
			ArrayList<Announcement> announces = annController.queryAnnounce(Integer.parseInt(sid));
			ArrayList<AnnouncementProperty> announcesProperty = new ArrayList<AnnouncementProperty>();
			for (int i = 0; i < announces.size(); i++) {
				announcesProperty.add(new AnnouncementProperty("" + announces.get(i).getAnnounceId(),
						announces.get(i).getAnnounceContent()));
			}
			announcementData = FXCollections.observableArrayList(announcesProperty);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//fetch the data for the record table
	private void setRecordData() {
		try {
			ArrayList<Record> records = recordController.queryRecord(Integer.parseInt(sid));
			ArrayList<RecordProperty> recordProperty = new ArrayList<RecordProperty>();
			for (int i = 0; i < records.size(); i++) {
				recordProperty.add(new RecordProperty("" + records.get(i).getRecordId(), records.get(i).getReason(),
						records.get(i).getRecordDate().toString()));
			}
			recordData = FXCollections.observableArrayList(recordProperty);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// allow the user to select the reason for this visit and write it in the database
	class SubmitHandler implements EventHandler<ActionEvent> {
		// Override the handle method
		public void handle(ActionEvent e) {
			if (reasonComboBox.getValue() != null && !reasonComboBox.getValue().toString().isEmpty()) {
				// reason should be written into database
				String reason = reasonComboBox.getValue().toString();
				reasonComboBox.setValue(null);
				try {
					recordController.updateRecord(Integer.parseInt(sid), reason,
							new Timestamp(System.currentTimeMillis()));
					stuController.addTimes(stu.getStudentID(), stu.getVisitTimes() + 1);
					Stage confirmStage = new Stage();
					VBox confirmPane = new VBox(30);
					confirmPane.setPadding(new Insets(30, 30, 30, 30));
					confirmPane.setAlignment(Pos.CENTER);
					Label confirmation = new Label("The record has been successfully saved");
					confirmation.setFont(new Font("Arial", 20));
					Button btok = new Button("OK");
					btok.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							confirmStage.close();
						}
					});
					confirmPane.getChildren().addAll(confirmation, btok);
					Scene confirmScene = new Scene(confirmPane);
					confirmStage.setScene(confirmScene);
					confirmStage.show();
					//reset the record table because the new data are available
					setRecordData();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
