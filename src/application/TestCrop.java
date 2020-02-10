package application;

import java.io.File;
 
import org.opencv.core.Core;
 
/**
 * 
 * Crop the face in batch
 *
 */
public class TestCrop {  
  static int i=0;
  public static void main(String[] args) {  
      faceCrop();
	  //testDeleteFile();
  }
  
public static void faceCrop() {
	 
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
    for(int i=28; i<=30; i++) {
    	FaceCrop.faceCrop("/Users/yangxin/Downloads/照片3/c_" + i + ".jpg","/Users/yangxin/Downloads/照片3/c_" + i + ".jpg");
    }
    System.out.println("Over");
}  
}