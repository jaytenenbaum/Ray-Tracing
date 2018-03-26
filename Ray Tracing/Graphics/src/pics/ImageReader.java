package pics;

import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * A class with a utility to read and recieve the color of a pixel from
 * an image, in the relative x,y pixels (relX,relY between 0,1)
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class ImageReader {
	public static Colors.Color getPixel(BufferedImage image, double relX, double relY){
	    int w = image.getWidth();
	    int h = image.getHeight();

	    int dataBuffInt = image.getRGB((int)(Math.max(relX*(w-1)/1.01,0)), (int)(Math.max(relY*(h-1)/1.01,0)));// 0 to w-1, 0 to h-1

	    Color c = new java.awt.Color(dataBuffInt);

	    return new Colors.Color((float)(float)(c.getRed())/255.0,(float)(float)(c.getGreen())/255.0, (float)(float)(c.getBlue())/255.0);
	}
}
