package RayTracing;

import Colors.Color;

public class RayTracingUtils {
	/**
	 * The main recursive function of tracing the ray. 
	 */
	public static Color traceRay(Ray ray, Environment env, int maxRecursionLevel, float numOfShadowRays) 
	{
		// stopping condition
		if (maxRecursionLevel <= 0)
			return env.backgroundColor;
		// find next ray intersection
		Intersection hit = findClosestIntersection(ray, env);
		
		if(hit == null)
		{
			return env.backgroundColor;
		}
		

		// Epsilon definition
		float epsilon = (float)0.0003;
		
		// Ambient light definition;
		float ambient = (float)0.0;
		
		// Color of the pixel, initialize with diffuse * ambient 
		Color col = getDiffuseColor(hit).mulScalar(ambient);
		// get point ray hit
		Point3d hitPoint = hit.getIntersection(); 
		// get backwards ray
		Vector3d V = ray.getNormalizedRay().direction.times(-1);
		
		/*
		 * Iterate over lights, to compute the shadow
		 */
		for (int i=0; i<env.lights.size(); i++)
		{
			Light light = env.lights.get(i);
			Point3d lightPos = light.getPosition();
			Vector3d L = (hitPoint.minus(lightPos)).normalized();
			
			// This will be the color added by current light 
			Color lightColor = new Color(0,0,0);
			
			
			// This part calculates soft shadows...
			float lightRadius = light.getLightRadius();
			
			// Create two perpendicular vectors
			Vector3d X = L.cross(V).normalized();
			Vector3d Y = X.cross(L).normalized();
			//go over grid, and compute shadow
			for (float j = -numOfShadowRays/2; j < numOfShadowRays/2; j++)
			{
				for (float h = -numOfShadowRays/2; h < numOfShadowRays/2; h++)
				{
					//calculate light in location of grid
					Light l = new Light(lightPos.plus(X.times(((j + (float)Math.random())/numOfShadowRays)*lightRadius)).plus(Y.times(((h + (float)Math.random())/numOfShadowRays)*lightRadius)), light.getColor(), light.specularIntensity, light.shadowIntensity, light.lightRadius);
					// get the shadow
					lightColor = lightColor.addColor(getLightColor(ray, env, hit, epsilon, hitPoint, V, l));
				}
			}
			// take average light color
			lightColor = lightColor.mulScalar(1/(numOfShadowRays*numOfShadowRays));
			
			col = col.addColor(lightColor);
		}
			
		// Refraction calculation
		try
		{
			if (hit.surface.getMat().transparency != 0)
			{
				V = ray.getNormalizedRay().direction;
				//get refracted ray (move forward by epsilon)
				Ray refrRay = new Ray(hitPoint.plus(V.times(epsilon)), V);
				
				// if refracted ray is still inside object, move it out of object
				Intersection lightHit = findClosestIntersection(refrRay, env);
				if (lightHit != null)
				{
					if (lightHit.surface.equals(hit.surface))
					{
						refrRay = new Ray(lightHit.intersection.plus(V.times(epsilon)), V);
					}
				}
				
				// calculate refracted color recursively
				Color tr = traceRay(refrRay, env, maxRecursionLevel - 1, numOfShadowRays);
				col = (tr.mulScalar(hit.surface.getMat().transparency)).addColor(col.mulScalar(1-hit.surface.getMat().transparency));
			}
		}
		catch (Exception e)	{}
		
		// Reflection calculation
		try
		{
			if (!hit.surface.getMat().reflectionColor.equals(new Color(0,0,0)))
			{
				// geometric calculations for reflection
				V = ray.getNormalizedRay().direction.times(-1);
				Vector3d N = hit.surface.getUnitNormalAtPoint(ray, hitPoint);
				Vector3d R = (N.times(N.dot(V.times(2)))).minusVector(V);
				Ray reflRay = new Ray(hitPoint.plus(R.times(epsilon)), R);
				// calculate reflected color recursively
				Color tr = traceRay(reflRay, env, maxRecursionLevel - 1, numOfShadowRays);
				col = col.addColor(tr.mulColor(hit.surface.getMat().reflectionColor));
			}
		}
		catch (Exception e)	{}
		
		// Make sure the color is bounded by 1 (0.99 is close enough :) )
		if (col.bCoeff > 0.99)
			col.bCoeff = (float)0.99;
		if (col.rCoeff > 0.99)
			col.rCoeff = (float)0.99;
		if (col.gCoeff > 0.99)
			col.gCoeff = (float)0.99;	
		
		return col;
	}
	/**
	 * Get the diffuse color of hit. Without the bonus, this function would
	 * just return the diffuse color of the surface of hit.
	 * With the bonus, we should possibly take color from image or make 
	 * a pattern.
	 * @param hit
	 * @return
	 */
	private static Color getDiffuseColor(Intersection hit) {
		ISurface surface = hit.surface;
		Material material = surface.getMat();
		Point3d point = hit.intersection;
		// case that we have a special plane (checkerboard, rays or circles)
		if(surface instanceof Plane && material.isSpecial()){
			Plane pln = (Plane)surface;
			//calculation of vectors on plane
			Vector3d vec = new Vector3d(1.1f,1.1f,1.1f).normalized();
			Vector3d right = vec.cross(pln.normal).normalized();
			Vector3d forward = right.cross(pln.normal).normalized();
			Point3d center = new Point3d(0, 0, pln.offset);
			// local plane coordinates
			float fVal = forward.dot(point.minus(center));
			float rVal = right.dot(point.minus(center));
			Color diffuseColor = material.getDiffuseColor();
			// calculate whether to negate or not
			if(isEven(func(fVal, rVal, material.specialNumber))){
				//negate color
				return new Color(1-diffuseColor.rCoeff, 1-diffuseColor.gCoeff, 1-diffuseColor.bCoeff);
			}
		}
		// case that we have a special box (showing an image)
		if(surface instanceof Box && material.imageLoc!=null){
			Box box = (Box)surface;
			Ray movedRay = box.getTwistedRay(new Ray(point, new Vector3d(0, 0, 0)));
			Point3d movedCenter = movedRay.source;
			// show image only on one plane of the box
			if(movedCenter.z<-(1-1.0e-2)*box.dz/2){
				// calculate relative box coordinates
				double relativeX = movedCenter.x/box.dx+0.5;
				double relativeY = 1-(movedCenter.y/box.dy+0.5);
				// return relevant image pixel
				return pics.ImageReader.getPixel(material.image, relativeX, relativeY);
			}
		}
		// basic case, just return surface diffuse color
		return new Color(hit.surface.getMat().getDiffuseColor());
	}
	/**
	 * different results for each type of special number 
	 * (if 1, we return a function that creates stripes, if 2
	 * then squares, if 3 then circles) 
	 */
	private static int func(float fVal, float rVal, int specialNumber) {
		if(specialNumber ==1) return (int)( Math.atan((fVal)/(rVal))*10);// outward stripes
		if(specialNumber ==2) return (int)(fVal*5)+(int)(rVal*5);// squares
		if(specialNumber ==3) return (int)(Math.sqrt((fVal)*(fVal)+(rVal)*(rVal))*5);// circles
		return 0;
	}

	private static boolean isEven(int i) {
		return i%2==0;
	}

	/**
	 * Get the color added by one source of light. 
	 */
	private static Color getLightColor(Ray ray, Environment env, Intersection hit, float epsilon,
			Point3d hitPoint, Vector3d V, Light light) 
	{
		Color lightColor = new Color(0,0,0);
		Point3d lightPos = light.getPosition();
		Vector3d L = (hitPoint.minus(lightPos)).normalized();
		Ray lightRay = new Ray(lightPos,L);
		
		// Find first intersection
		Intersection lightHit = findClosestIntersection(lightRay, env);
		if (lightHit == null)
			return lightColor;

		// Transparency of all the objects the light go through, initialized with 1
		float transparency = 1;
		
		if (!lightHit.surface.equals(hit.surface) && lightHit.intersection.distance(lightPos) < hit.intersection.distance(lightPos))
			transparency = lightHit.surface.getMat().transparency;
		
		ISurface lastSurface = lightHit.surface;
		
		// Update transparency according to the objects the light go through
		while (lightHit != null && !lightHit.surface.equals(hit.surface) && lightHit.intersection.distance(lightPos) < hit.intersection.distance(lightPos) && transparency != 0)
		{
			if (!lightHit.surface.equals(lastSurface))
				transparency *=lightHit.surface.getMat().transparency;
			lightHit = findClosestIntersection(new Ray(lightHit.intersection.plus(L.times(epsilon)),L), env);
		}
		
		// Calculate the intensity of the light using shadow intensity
		float lightIntensity = (1 - light.shadowIntensity) + transparency*light.shadowIntensity;
		
		Vector3d N = hit.surface.getUnitNormalAtPoint(lightRay, hitPoint);
		L = L.times(-1);
		float diffDot = L.dot(N);
		Vector3d R = (N.times(N.dot(L.times(2)))).minusVector(L);
		float specDot = (float) Math.pow(R.dot(V), hit.surface.getMat().getPhongSpecularityCoeff());
		
		if (diffDot <= 0)
			return lightColor;
			
		// Calculate diffuse color
		lightColor = lightColor.addColor(light.getColor().mulColor(getDiffuseColor(hit)).mulScalar(diffDot * lightIntensity));
		
		// Calculate specular color
		if (R.dot(V) > 0 && R.dot(N) > 0 && N.dot(V) > 0)
		{
			lightColor = lightColor.addColor(light.getColor().mulColor(hit.surface.getMat().specularColor).mulScalar(specDot * lightIntensity));
		}
		return lightColor;
	}
	/**
	 * Calculate the closest intersection of ray with environment.
	 * We get first the closest surface, and then return the intersection
	 * with it.
	 */
	private static Intersection findClosestIntersection(Ray ray, Environment env) {
		ISurface closestSurface = getClosestSurface(ray, env);
		if(closestSurface == null){
			return null;
		}
		return new Intersection(closestSurface, closestSurface.getIntersection(ray));
	}
	/**
	 * Calculate the closest surface to ray in environment.
	 */
	private static ISurface getClosestSurface(Ray ray, Environment env) {
		ISurface closestSurface = null;
		float minimumDistance = Float.MAX_VALUE;
		// iterate over surfaces
		for ( ISurface surface : env.getSurfaces()) {
			// get intersection (null if doesn't exist)
			Point3d intersection = surface.getIntersection(ray);
			if(intersection!=null){
				// update to closest, if intersection is better than previous closest
				float distance = intersection.distance(ray.getSource());
				if(distance < minimumDistance){
					closestSurface = surface;
					minimumDistance = distance;
				}
			}
		}
		return closestSurface;
	}
	
//	private static ArrayList<Intersection> getIntersectingIntersectionsCloserThan(Ray ray, Environment env, Point3d point) {
//		ArrayList<Intersection> ans = new ArrayList<>(); 
//		
//		for ( ISurface surface : env.getSurfaces()) {
//			Point3d intersection = surface.getIntersection(ray);
//			if(intersection!=null && intersection.distance(ray.source) < point.distance(ray.source)){
//				ans.add(new Intersection(surface, intersection));
//			}
//		}
//		return ans;
//	}
}
