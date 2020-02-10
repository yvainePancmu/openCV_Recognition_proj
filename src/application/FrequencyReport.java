package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
/**
 * The window to show the frequency report
 * @author Yunzhou Ning
 *
 */
public class FrequencyReport extends BorderPane {
	//start and end date
	String start;
	String end;
	//for the data in the two piecharts
	ObservableList<Data> answerM = FXCollections.observableArrayList();
	ObservableList<Data> answerF = FXCollections.observableArrayList();

	public FrequencyReport(String start, String end) {
		this.start = start;
		this.end = end;
		//set the banner on the top
		VBox vbox = new VBox(30);
		vbox.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView("banner.jpg");
		imageView.setFitHeight(200);
		imageView.setFitWidth(800);
		vbox.getChildren().add(imageView);
		//set the title
		Label title = new Label("Frequency Report during " + start + " and " + end);
		title.setFont(new Font("Arial", 20));
		vbox.getChildren().add(title);
		vbox.setPadding(new Insets(0, 0, 30, 0));
		setTop(vbox);
		StackPane malePane = new StackPane();
		PieChart malePie = new PieChart();
		//get the data for two piecharts
		getChartData();
		malePie.setData(answerM);// get from database
		malePie.setTitle("Male");
		//display the male piechart on the left
		malePie.setLegendSide(Side.RIGHT);
		malePie.setClockwise(false);
		malePie.setLabelsVisible(false);
		malePie.setPrefSize(400, 150);
		malePane.getChildren().add(malePie);
		setLeft(malePane);
		StackPane femalePane = new StackPane();
		PieChart femalePie = new PieChart();
		//display the female piechart on the right
		femalePie.setData(answerF);// get from database
		femalePie.setTitle("Female");
		femalePie.setLegendSide(Side.RIGHT);
		femalePie.setClockwise(false);
		femalePie.setLabelsVisible(false);
		femalePie.setPrefSize(400, 150);
		femalePane.getChildren().add(femalePie);
		setRight(femalePane);
		StackPane placeholder = new StackPane();
		placeholder.setPadding(new Insets(0, 0, 40, 0));
		setBottom(placeholder);
	}
	//get the record data and group them by gender and visit reasons
	private ObservableList<Data>[] getChartData() {
		ArrayList<ReportResult> list;
		try {
			list = new RecordDAOImpl().generateFrequency(start, end);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStudGender().equals("Male")) {
					answerM.add(new PieChart.Data(list.get(i).getVisReason(), list.get(i).getCategoryNum()));
				} else if (list.get(i).getStudGender().equals("Female")) {
					answerF.add(new PieChart.Data(list.get(i).getVisReason(), list.get(i).getCategoryNum()));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
