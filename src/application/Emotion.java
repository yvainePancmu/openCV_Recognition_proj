package application;


import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * calls detectFaces function and print out results 
 * some of codes in this class uses codes in https://cloud.google.com/vision/docs/
 * @param arg Command line arguments
 * @return 
 * @throws Exception   on errors while closing the client.
 * @throws IOException on Input/Output errors.
 */
public class Emotion {
	
	public static void main(String[] args) throws IOException, Exception {
		
		int data[] = detectFaces("/Users/cloudyboat/Desktop/Java/teamProject/openCV_Recognition/temp0.png");
		// print out emotion values
		for (int i : data) {
			System.out.println(i);
		}
	}


	/**
	 * Detects sentiment in specified local image using google Vision API. 
	 *
	 * @param filePath The path to the file to perform sentiment analysis on.
	 * @return int[] an integer array of four types of emotions
	 * @throws Exception   on errors while closing the client.
	 * @throws IOException on Input/Output errors.
	 */
	public static int[] detectFaces(String filePath) throws Exception, IOException {
		
		
		List<AnnotateImageRequest> requests = new ArrayList<>();
		// read picture from path and turn into bytes
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
		// build image and its features
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
		// form request of the image and its features
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		
		requests.add(request);
		//  create a client
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			
			// send request to google api
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			// get response
			List<AnnotateImageResponse> responses = response.getResponsesList();

			AnnotateImageResponse res = responses.get(0);
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
			}
			// get emotion annotation
			FaceAnnotation annotation = res.getFaceAnnotationsList().get(0);
			
			// an integer array to store data
			int data[] = new int[4];
			data[0] = annotation.getAngerLikelihoodValue();
			data[1] = annotation.getJoyLikelihoodValue();
			data[2] = annotation.getSurpriseLikelihoodValue();
			data[3] = annotation.getSorrowLikelihoodValue();
			// return the array
			return data;
		}
	}
}

