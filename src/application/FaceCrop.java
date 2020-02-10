package application;

import java.util.ArrayList;

import org.opencv.core.Core;  
import org.opencv.core.Mat;  
import org.opencv.core.MatOfRect;  
import org.opencv.core.Point;  
import org.opencv.core.Rect;  
import org.opencv.core.Scalar;  
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;  
  
/**
 * 
 *Detect face and save the largest face in the file.
 *
 */
 
public class FaceCrop {
 
	public static double calcArea(Rect rect)
	{
		return rect.width*rect.height;
	}
	
	public static String xmlfilePath="/Users/yangxin/Desktop/Java_Course/opencv-3.4.7/data/haarcascades/haarcascade_frontalface_alt.xml";
	public static void faceCrop(String inputImageFilename,String outputImageFilename) 
	{  
		CascadeClassifier faceDetector = new CascadeClassifier(xmlfilePath);  
		Mat image = Imgcodecs.imread(inputImageFilename);  
		// Detect Face
		MatOfRect faceDetections = new MatOfRect();  
		faceDetector.detectMultiScale(image, faceDetections);  
		System.out.println(String.format("Detect %s face", faceDetections.toArray().length));  
		// Find the largest face
		Rect maxRect = new Rect(0,0,0,0);
		ArrayList<Rect> rectlist = new ArrayList<Rect>();
		for (Rect rect : faceDetections.toArray()) 
		{  
			rectlist.add(rect);
			if(calcArea(maxRect)<calcArea(rect))
			{
				maxRect=rect;
		
			}
			//Draw rectangle on the face
			//Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));  
		}

		if(calcArea(maxRect)>0){
		//Create the copy of the face
		Mat roi_img = new Mat(image,maxRect); 
		Mat tmp_img = new Mat();
		//Copy the face
		roi_img.copyTo(tmp_img);
		//Save the largest face
		Imgcodecs.imwrite(outputImageFilename, tmp_img);
		}
	}  
}  

