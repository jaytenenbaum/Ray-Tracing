package RayTracing;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import Colors.Color;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
	public Environment env;
	public int imageWidth;
	public int imageHeight;
	int numOfShadowRays;
	int maxRecursionLevel;
	byte[] rgbData;
	/**
	 * Compute the color of the pixel (x,y) 
	 */
	private Color rayTrace(int x, int y, Environment env) {
		Ray ray = Ray.constructUnitRayThroughPixel(env.getCam(), ((float)x)/((float)imageWidth) , ((float)y)/((float)imageHeight));
		return RayTracingUtils.traceRay(ray, env, maxRecursionLevel, (float)numOfShadowRays);
	}
	


	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {
		try {

			RayTracer tracer = new RayTracer();

            // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;


			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];
			
			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Parses the scene file and creates the scene.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		env = new Environment();
		ArrayList<Material> materials = new ArrayList<Material>();
		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					Point3d position = new Point3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					Point3d lookAtPoint = new Point3d(Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
					Vector3d upVector = new Vector3d(Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]));
                    Camera cam = new Camera(position, lookAtPoint, upVector, Float.parseFloat(params[9]), Float.parseFloat(params[10]),Float.parseFloat(params[10])*((float)this.imageHeight)/((float)this.imageWidth));                    
					env.setCam(cam);

					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					Color backgroundColor = new Color(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					env.setBackgroundColor(backgroundColor);
					numOfShadowRays = Integer.parseInt(params[3]);
					maxRecursionLevel = Integer.parseInt(params[4]);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					
					Color diffuseColor = new Color(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					Color specularColor = new Color(Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
					Color reflectionColor = new Color(Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]));
					float phongSpecularityCoeff = Float.parseFloat(params[9]);
					float transparency = Float.parseFloat(params[10]);

					// this is for materials on planes, which we can make either
					// outward stripes (if equals 1)
					// squares (if equals 2)
					// circles (if equals 3)
					// default is 0, which plane is showed as normal
					int specialNumber = 0;
					if(params.length==12){
						specialNumber = Integer.parseInt(params[11]);
					}
					// this is for materials on boxes, which we specify file location
					String imageLoc = null;
					if(params.length==13){
						imageLoc =params[12];
					}
					Material material = new Material(1 + materials.size(), diffuseColor, specularColor, phongSpecularityCoeff, reflectionColor, transparency, specialNumber, imageLoc);
					materials.add(material);
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					Point3d center = new Point3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					float radius = Float.parseFloat(params[3]);
					int matIndex = Integer.parseInt(params[4]);
					env.getSurfaces().add(new Sphere(center, radius, materials.get(matIndex-1)));
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					Vector3d normal = new Vector3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					float offset = Float.parseFloat(params[3]);
					int matIndex = Integer.parseInt(params[4]);
					env.getSurfaces().add(new Plane(normal, offset, materials.get(matIndex-1)));
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("cyl"))
				{
					Point3d center = new Point3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					float length = Float.parseFloat(params[3]);
					float radius = Float.parseFloat(params[4]);
					Vector3d rotationAroundAxes = new Vector3d(Float.parseFloat(params[5]), Float.parseFloat(params[6]), Float.parseFloat(params[7]));
					int matIndex = Integer.parseInt(params[8]);
					env.getSurfaces().add(new Cylinder(center, length, radius, rotationAroundAxes, materials.get(matIndex-1)));
					System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
				}
				else if (code.equals("box"))
				{
					Point3d center = new Point3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					float dx = Float.parseFloat(params[3]);
					float dy = Float.parseFloat(params[4]);
					float dz = Float.parseFloat(params[5]);
					Vector3d rotationAroundAxes = new Vector3d(Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]));
					int matIndex = Integer.parseInt(params[9]);
					env.getSurfaces().add(new Box(center, dx, dy, dz, rotationAroundAxes, materials.get(matIndex-1)));
					System.out.println(String.format("Parsed Box (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					Point3d position = new Point3d(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
					Color color = new Color(Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
					float specularIntensity = Float.parseFloat(params[6]);
					float shadowIntensity = Float.parseFloat(params[7]);
					float lightRadius = Float.parseFloat(params[8]);
					env.getLights().add(new Light(position, color, specularIntensity, shadowIntensity, lightRadius));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);
		r.close();
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		for (int x = 0; x < this.imageWidth; x++) {
			// render the x'th line multithreaded
			renderLineMultiThreaded(x);
		}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}



	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2109299985872926772L;

		public RayTracerException(String msg) {  super(msg); }
	}
	/**
	 * A class to update the rgb value of the computed color by ray tracing
	 * @author Jay Tenenbaum + Alon Goldberg
	 */
	public static class MyRunnable implements Runnable {
		int x,y;
		RayTracer ray;
 
		MyRunnable(int x, int y, RayTracer ray) {
			this.x=x;
			this.y=y;
			this.ray=ray;
		}
 
		@Override
		public void run() {
			Color colorOfPixel = ray.rayTrace(x,y, ray.env);
			ray.rgbData[(y * ray.imageWidth + x) * 3] = (byte)(Math.round(colorOfPixel.rCoeff*256));
			ray.rgbData[(y * ray.imageWidth + x) * 3 + 1] = (byte)(Math.round(colorOfPixel.gCoeff*256));
			ray.rgbData[(y * ray.imageWidth + x) * 3 + 2] = (byte)(Math.round(colorOfPixel.bCoeff*256));
		}
	}
	
	/**
	 * Render the x'th line of the scene multithreaded
	 */
	private void renderLineMultiThreaded(int x) {
		// init number of threads (between 1 and 32)
		int MYTHREADS = Math.min(32,Math.max(Runtime.getRuntime().availableProcessors(),1));
		// create thread pool
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		// create and execute all the threads for pixels in the x'th line
		for (int y = 0; y < this.imageHeight; y++) {
			Runnable worker = new MyRunnable(x,y,this);
			executor.execute(worker);
			
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
			
		}
	}
	
	/**
	 * Bonus - create video (frames of the video) of a jumping ball.
	 */
	public void jumping_ball() throws IOException, RayTracerException {
		String sceneFileName = "examples\\bonus_video1.txt";

		final float frame_rate = 25;
		
		int first_jump_time = (int)(frame_rate * 1.5);
		int jump_time = first_jump_time;
		int frame = 0;
		
		// Parse scene file:
		parseScene(sceneFileName);
		
		for (int i = 0; i < 6; i++) {
			int start_j = (i == 0) ? jump_time / 2 : 0;
			for (int j = start_j; j < jump_time; j++, frame++) {
				float inner_time = 2 * j / (float)jump_time - 1;
				float max_height = 1.8f * (float)Math.pow(jump_time / (float)first_jump_time, 2);
				float height = max_height * (1 - inner_time * inner_time);
				
				for (ISurface s : env.surfaces) {
					if (s.getMat().id == 1) {
						((Sphere)s).center.y = height - 0.5f;
					}
				}
				
				
				// Render scene:
				String outputFileName = String.format("c:\\tmp\\%03d.png", frame);
				renderScene(outputFileName);
				System.out.println(frame);
			}
			
			jump_time = (int)(jump_time * 0.8);
		}
	}
	
	/**
	 * Bonus - create video (frames of the video) of a scene while the camera is moving.
	 */
	public void building_video() throws IOException, RayTracerException {
		String sceneFileName = "examples\\bonus_video2.txt";
		
		final float frame_rate = 25;
		final float length = 4; 
		
		// Parse scene file:
		parseScene(sceneFileName);
		
		for (int frame = 0; frame < length * frame_rate; frame++) {
			String outputFileName = String.format("c:\\tmp\\%03d.png", frame);
			float time = frame / length / frame_rate;
			//time =1;
			float time2 = 1 - (1-time)*(1-time);
			

			float alpha = time2 * (float)Math.PI / 2;
			float beta = (1-time) * (float)Math.PI / 6;
			float y = (float)(-Math.sin(alpha) * 3.7 + 4);
			float dist = (float)(-Math.cos(alpha) * 7 - 0.4);
			float x = dist * (float)Math.sin(beta);
			float z = dist * (float)Math.cos(beta);
			
			Point3d position = new Point3d(x, y, z);
			Point3d lookAtPoint = new Point3d(0, .5f, 1);
			Vector3d upVector = new Vector3d(0, 1, 0);
            Camera cam = new Camera(position, lookAtPoint, upVector, 1, 1, 1f*((float)this.imageHeight)/((float)this.imageWidth));                    
			env.setCam(cam);
			
			// Render scene:
			renderScene(outputFileName);
		}
	}
}
