package application;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
/**
 * The window to show the category report
 * @author Yunzhou Ning
 *
 */
public class CategoryReport extends BorderPane {
	XYChart.Series<String, Number> series = new XYChart.Series<>();
	String start;
	String end;
	//start is start date, end is the end date
	public CategoryReport(String start, String end) {
		this.start = start;
		this.end = end;
		// set the banner on the top
		ImageView imageView = new ImageView("banner.jpg");
		imageView.setFitHeight(200);
		imageView.setFitWidth(800);
		setTop(imageView);
		//Set the bar chart axis
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("meet faculty", "pay tution",
				"borrow tools", "complaint", "query", "collect assignement")));

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Times");

		// Creating the Bar chart
		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setPadding(new Insets(30));
		barChart.setTitle("Report on Categories during " + start + " and " + end);
		barChart.setBarGap(30);
		// Prepare XYChart.Series objects by setting data
		setSeriesData();
		series.setName("Times");
		// Setting the data to bar chart
		barChart.getData().addAll(series);
		setCenter(barChart);
	}
	//fetch the data from the database and set the data for the bar chart
	private void setSeriesData() {
		RecordDAOImpl recordFinder = new RecordDAOImpl();
		ArrayList<ReportResult> list;
		try {
			list = recordFinder.generateCategory(start, end);
			for (int i = 0; i < list.size(); i++) {
				series.getData().add(new XYChart.Data<>(list.get(i).getVisReason(), list.get(i).getCategoryNum()));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
