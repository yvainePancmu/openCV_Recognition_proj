package application;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.opencv.videoio.VideoCapture;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the face detection/tracking/recognition.
 * Part of this class uses codes in https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html
 * 
 * 
 */
public class FaceDetectionController {
	// FXML buttons
	@FXML
	private Button cameraButton;
	@FXML
	private Button reportButton;
	@FXML
	private Button pictureButton;
	// the FXML area for showing the current frame
	@FXML
	private ImageView originalFrame;
	@FXML
	private ImageView welcomeImage;
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;

	// face cascade classifier
	private CascadeClassifier cascade;
	//we use both opencv and javacv here, so the convertor between them are needed
	private OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2;
	private OpenCVFrameConverter.ToMat convertor;
	//face recognizer
	private FaceRecognizer fr;
	//name list and idlist fetch from database
	private ArrayList<Integer> idList;
	private ArrayList<String> nameList;

	/**
	 * Init the controller, at start time
	 */
	protected void init() {
		//fetch the name list and id list
		StudentDAOImpl stuController = new StudentDAOImpl();
		try {
			idList = stuController.IDCount();
			nameList = stuController.NameList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.capture = new VideoCapture();
		//load the detecter
		this.cascade = new CascadeClassifier(
				"/Users/cloudyboat/Desktop/Java/teamProject/newBuild/opencv-3.4.7/data/haarcascades/haarcascade_frontalface_alt.xml");
		//load the recognizer
		fr = FisherFaceRecognizer.create();
		fr.read("/Users/cloudyboat/Desktop/Java/teamProject/javaCV/src/face_recognition/FisherRecognize_6.xml");
		//set the parameter for the recognizer
		fr.setThreshold(620.0); 
		cameraButton.setDisable(false);
		// set a fixed width for the frame
		originalFrame.setFitWidth(600);
		// preserve image ratio
		originalFrame.setPreserveRatio(true);
		this.convertor = new OpenCVFrameConverter.ToMat();
		this.converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
	}

	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void startCamera() {
		if (!this.cameraActive) {
			// start the video capture
			this.capture.open(0);
			// is the video stream available?
			if (this.capture.isOpened()) {
				this.pictureButton.setDisable(false);
				this.cameraActive = true;
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					@Override
					public void run() {
						// effectively grab and process a single frame
						Mat javacvMat = grabFrame(new ArrayList<Integer>());
						org.opencv.core.Mat frame = converter2.convert(convertor.convert(javacvMat));
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(originalFrame, imageToShow);
					}
				};
				//set the mutli threads
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				// update the button content
				this.cameraButton.setText("Stop Camera");
			} else {
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		} else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.cameraButton.setText("Start Camera");
			this.pictureButton.setDisable(true);
			// stop the timer
			this.stopAcquisition();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame(ArrayList<Integer> recognizedIDs) {

		Mat javacvMat = new Mat();
		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				org.opencv.core.Mat opencvMat = new org.opencv.core.Mat();
				// read the current frame
				this.capture.read(opencvMat);

				// if the frame is not empty, process it
				if (!opencvMat.empty()) {
					javacvMat = convertor.convert(converter2.convert(opencvMat));
					//detect face and see if they can be recognized
					javacvMat = detectAndDisplay(javacvMat, recognizedIDs);
					// Frame is sent to be recognized and squares are added to recognized faces
				}

			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return javacvMat;
	}

	/**
	 * Method for face detection, recognition and tracking
	 * @param frame it looks for faces in this frame
	 */
	private Mat detectAndDisplay(Mat scr, ArrayList<Integer> recognizedIDs) {
		Mat grayscr = new Mat();
		Mat face = new Mat();
		Mat roi = new Mat();
		opencv_imgproc.cvtColor(scr, grayscr, opencv_imgproc.COLOR_BGRA2GRAY);// transform the colorful to gray
		opencv_imgproc.equalizeHist(grayscr, grayscr);// equalize
		//use detector to detect faces
		RectVector faces = new RectVector();
		cascade.detectMultiScale(grayscr, faces);
		IntPointer label = new IntPointer(1);
		DoublePointer confidence = new DoublePointer(1);
		for (int i = 0; i < faces.size(); i++) {
			Rect face_i = faces.get(i);
			//add squares of the faces to the frame
			opencv_imgproc.rectangle(scr, face_i, new Scalar(0, 255, 0, 1));
			roi = new Mat(grayscr, face_i);
			opencv_imgproc.resize(roi, face, new Size(80, 80));// adjust to the training images
			//use the recognizer to recognize faces
			fr.predict(face, label, confidence);
			int predictedLabel = label.get(0);// get the recognized labels
			recognizedIDs.add(predictedLabel);
			//if recognized
			if (idList.contains(predictedLabel)) {
				int index = idList.indexOf(predictedLabel);
				String box_text = "people:" + nameList.get(index);
				int pos_x = Math.max(face_i.tl().x() - 10, 0);
				int pos_y = Math.max(face_i.tl().y() - 10, 0);
				//add the name corresponding to the id to the frame
				opencv_imgproc.putText(scr, box_text, new Point(pos_x, pos_y), opencv_imgproc.FONT_HERSHEY_PLAIN, 1.0,
						new Scalar(0, 255, 0, 2.0));
			} //if not recognized
			else {
				int pos_x = Math.max(face_i.tl().x() - 10, 0);
				int pos_y = Math.max(face_i.tl().y() - 10, 0);
				// put the unknown people to the square
				opencv_imgproc.putText(scr, "UnknownPeople!", new Point(pos_x, pos_y),
						opencv_imgproc.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 0, 255, 2.0));
			}
		}
		return scr;
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition() {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		if (this.capture.isOpened()) {
			// release the camera
			this.capture.release();
		}
	}

	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view  the {@link ImageView} to update
	 * @param image the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}

	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed() {
		this.stopAcquisition();
	}

	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void giveReport() {
		Stage reportStage = new Stage();
		ReportOrigin paneReportOrigin = new ReportOrigin();
		Scene scene = new Scene(paneReportOrigin);
		reportStage.setTitle("Report Origin");
		reportStage.setScene(scene);
		reportStage.show();
	}

	/**
	 * The action triggered by pushing the button "check in" on the GUI
	 */
	@FXML
	protected void takePicture() {
		if (this.cameraActive) {
			ArrayList<Integer> recognizedIDs = new ArrayList<>();
			//get a frame and transfrom it into imageview
			Mat javacvMat = grabFrame(recognizedIDs);
			org.opencv.core.Mat frame = converter2.convert(convertor.convert(javacvMat));
			Image picture = Utils.mat2Image(frame);
			ImageView pictureView = new ImageView(picture);
			//when only one detected
			if (recognizedIDs.size() <= 1) {
				switch (recognizedIDs.get(0)) {
				//-1 means not recognized
				case -1:
					showAlert(pictureView);
					break;
				default:
					showDashboard(recognizedIDs.get(0), pictureView);
				}
			} // when more than one faces detected
			else {
				//check if any face can be recognized
				boolean recognized = false;
				for (int i = 0; i < recognizedIDs.size(); i++) {
					if (recognizedIDs.get(i) != -1) {
						recognized = true;
						break;
					}
				}
				//if at least one face can be recognized, show the dashboards of all of them
				if (recognized) {
					for (int i = 0; i < recognizedIDs.size(); i++) {
						if (recognizedIDs.get(i) != -1) {
							showDashboard(recognizedIDs.get(i), pictureView);
						}
					}
				} // if nobody can be recognized
				else {
					showAlert(pictureView);
				}
			}
		}
	}
	/**
	 * show the alert pane
	 */

	protected void showAlert(ImageView imageView) {
		Alert alertPane = new Alert(imageView);
		Stage sAlert = new Stage();
		Scene sceneAlert = new Scene(alertPane);
		sAlert.setTitle("Alert");
		sAlert.setScene(sceneAlert);
		sAlert.show();
	}
	/**
	 * show the dashboard of a stu
	 */

	protected void showDashboard(int id, ImageView pictureView) {
		DashBoard dashBoard = new DashBoard(id + "", pictureView);
		Stage sDashboard = new Stage();
		Scene sceneDashboard = new Scene(dashBoard);
		sDashboard.setTitle("Dashboard");
		sDashboard.setScene(sceneDashboard);
		sDashboard.show();
	}

}

// reference: 