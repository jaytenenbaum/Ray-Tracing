package RayTracing;

import java.util.ArrayList;
/**
 * A class containing a cylinder. A cylinder is defined first by its version
 * assuming we don't rotate around the axes. Then we have its center,
 * its material and the length and radius.
 * Then we add rotation using the "Vector3d rotationAroundAxes".
 * To save time in computations, we also save the cos and sin of those values.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Cylinder implements ISurface {
	Point3d center;
	float length;
	float radius;
	Vector3d rotationAroundAxes;
	Vector3d cosAroundAxes;
	Vector3d sinAroundAxes;
	Material mat;
	static final double epsilon = 1.0e-2;
	Vector3d unitVector = null;
	public Cylinder(Point3d center, float length, float radius, Vector3d rotationAroundAxes, Material mat) {
		super();
		this.center = center;
		this.length = length;
		this.radius = radius;
		this.rotationAroundAxes = rotationAroundAxes;
		this.mat = mat;
		this.cosAroundAxes = getCosAroundAxes(rotationAroundAxes);
		this.sinAroundAxes = getSinAroundAxes(rotationAroundAxes);
		
	}
	/**
	 * Calculate the vector of sins of the rotations around axes.
	 * We first convert the rotation to radians for the sin function.
	 */
	private Vector3d getSinAroundAxes(Vector3d rotationAroundAxes2) {
		Vector3d rads = rotationAroundAxes2.degToRad();
		return new Vector3d((float)Math.sin(rads.dx),(float)Math.sin(rads.dy),(float)Math.sin(rads.dz));
	}
	/**
	 * Calculate the vector of cos' of the rotations around axes.
	 * We first convert the rotation to radians for the cos function.
	 */
	private Vector3d getCosAroundAxes(Vector3d rotationAroundAxes2) {
		Vector3d rads = rotationAroundAxes2.degToRad();
		return new Vector3d((float)Math.cos(rads.dx),(float)Math.cos(rads.dy),(float)Math.cos(rads.dz));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + Float.floatToIntBits(length);
		result = prime * result + ((mat == null) ? 0 : mat.hashCode());
		result = prime * result + Float.floatToIntBits(radius);
		result = prime * result + ((rotationAroundAxes == null) ? 0 : rotationAroundAxes.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cylinder other = (Cylinder) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (Float.floatToIntBits(length) != Float.floatToIntBits(other.length))
			return false;
		if (mat == null) {
			if (other.mat != null)
				return false;
		} else if (!mat.equals(other.mat))
			return false;
		if (Float.floatToIntBits(radius) != Float.floatToIntBits(other.radius))
			return false;
		if (rotationAroundAxes == null) {
			if (other.rotationAroundAxes != null)
				return false;
		} else if (!rotationAroundAxes.equals(other.rotationAroundAxes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cylinder [center=" + center + ", length=" + length + ", radius=" + radius + ", rotationAroundAxes="
				+ rotationAroundAxes + ", mat=" + mat + "]";
	}

	public Point3d getCenter() {
		return center;
	}

	public void setCenter(Point3d center) {
		this.center = center;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Vector3d getRotationAroundAxes() {
		return rotationAroundAxes;
	}

	public void setRotationAroundAxes(Vector3d rotationAroundAxes) {
		this.rotationAroundAxes = rotationAroundAxes;
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}
	public Vector3d getCosAroundAxes() {
		return cosAroundAxes;
	}

	public void setCosAroundAxes(Vector3d cosAroundAxes) {
		this.cosAroundAxes = cosAroundAxes;
	}

	public Vector3d getSinAroundAxes() {
		return sinAroundAxes;
	}

	public void setSinAroundAxes(Vector3d sinAroundAxes) {
		this.sinAroundAxes = sinAroundAxes;
	}
//	public Vector3d getUnitVector(){
//		if(unitVector==null){
//			Vector3d dir = rotationAroundAxes.degToRad();
//			unitVector = new Vector3d(0,0,1);
//			unitVector = new Vector3d(unitVector.dx, (float) (unitVector.dy * Math.cos(dir.dx) - unitVector.dz * Math.sin(dir.dx)),
//					(float) (unitVector.dy * Math.sin(dir.dx) + unitVector.dz * Math.cos(dir.dx)));
//			unitVector = new Vector3d((float) (unitVector.dx * Math.cos(dir.dy) + unitVector.dz * Math.sin(dir.dy)),unitVector.dy ,
//					(float) (unitVector.dz * Math.cos(dir.dy)-unitVector.dx * Math.sin(dir.dy)));
//			unitVector = new Vector3d((float) (unitVector.dx * Math.cos(dir.dz) - unitVector.dy * Math.sin(dir.dz)),
//					(float) (unitVector.dx * Math.sin(dir.dz) + unitVector.dy * Math.cos(dir.dz)),unitVector.dz);
//		}
//		return unitVector;
//	}
	/**
	 * Calculate the intersection of a cylinder and a ray.
	 * We first twist the ray, to assume the cylinder isn't rotated around axes,
	 * and is centered at (0,0,0).
	 * Then we compute the best intersection under that assumption.
	 * Finally we rotate the intersection back to get the wanted answer.
	 */
	@Override
	public Point3d getIntersection(Ray r) {
		Ray movedRay = getTwistedRay(r);
		Point3d intersection = getIntersectionAssumingCylIsCanonical(movedRay);
		if(intersection==null){
			return null;
		}
		return getUnTwistRay(new Ray(intersection, new Vector3d(0,0,0))).source;
		
	}
	/**
	 * Get the ray assuming cylinder is centered at (0,0,0) and not twisted.
	 * First move the ray, and then twist it. 
	 */
	private Ray getTwistedRay(Ray r) {
		r = new Ray(new Point3d(r.source.minus(center)), r.direction);
		return Ray.twistRotation(r,cosAroundAxes,sinAroundAxes);
	}
	/**
	 * Retrieve the original ray before the twisting.
	 */
	private Ray getUnTwistRay(Ray r) {
		r = Ray.untwistRotation(r,cosAroundAxes,sinAroundAxes);
		return new Ray(r.source.plus(new Vector3d(center)), r.direction);
	}
	

	/**
	 * Get intersection of cylinder with the ray, assuming
	 * we rotated the ray already. (cylinder is canonical)
	 * We first compute all 4 possible intersections (two from lids and
	 * 2 from outer shell), then choose the best one.
	 */
	private Point3d getIntersectionAssumingCylIsCanonical(Ray movedRay) {
		ArrayList<Point3d> possibleIntersections = getPossibleIntersectionsCanonical(movedRay);
		return closestLegalIntersection(possibleIntersections,movedRay);
	}
	/**
	 * Take the best intersection out of the bunch (both closest and on the right side of ray) 
	 */
	private Point3d closestLegalIntersection(ArrayList<Point3d> possibleIntersections, Ray movedRay) {
		ArrayList<Point3d> rightSideIntersections = new ArrayList<>();
		for (Point3d p: possibleIntersections) {
			if(movedRay.direction.dot(p.minus(movedRay.source))>0){
				rightSideIntersections.add(p);
			}
		}
		if(rightSideIntersections.size()==0){
			return null;
		}
		
		double minDist = Double.MAX_VALUE;
		Point3d minPoint = null;
		for (Point3d p: rightSideIntersections) {
			if(p.distance(movedRay.source)<minDist){
				minPoint = p;
				minDist = p.distance(movedRay.source);
			}
		}
		return minPoint;
	}
	/**
	 * Get as an ArrayList of doubles the solutions to the equation
	 * ax^2+bx+c=0
	 */
	public static ArrayList<Double> getBinomialAnswers(double a, double b, double c){
		double delta = b*b-4*a*c;
		if(delta<0){
			return new ArrayList<>();
		}
		ArrayList<Double> ans = new ArrayList<>();
		ans.add((-b+Math.sqrt(delta))/(2*a));
		ans.add((-b-Math.sqrt(delta))/(2*a));
		return ans;
	}
	/**
	 * Get the possible intersections of the given ray with the cylinder.
	 * We first check the intersections of the lids:
	 * 		take each lids plane, intersect and check if it is in area inside outer shell.
	 * Then we check the intersections of the outer shell:
	 * 		intersect geometrically, and check if it is between two lids.
	 */
	private ArrayList<Point3d> getPossibleIntersectionsCanonical(Ray movedRay) {
		ArrayList<Point3d> possibleIntersections = new ArrayList<>();
		
		/**
		 * check for two lids intersections 
		 */
		Plane firstLid = new Plane(new Vector3d(0,0,1), length/2);
		Point3d intersec1 = firstLid.getIntersection(movedRay);
		// if intersection exists and in limits of radius of cylinder
		if(intersec1!=null && intersec1.x*intersec1.x+intersec1.y*intersec1.y<=radius*radius){
			possibleIntersections.add(intersec1);
		}
		Plane secondLid = new Plane(new Vector3d(0,0,1), -length/2);
		Point3d intersec2 = secondLid.getIntersection(movedRay);
		// if intersection exists and in limits of radius of cylinder
		if(intersec2!=null && intersec2.x*intersec2.x+intersec2.y*intersec2.y<=radius*radius){
			possibleIntersections.add(intersec2);
		}
		
		/**
		 * check for the cylinder itself
		 */
		Ray ray = movedRay.getNormalizedRay();
		Vector3d dir = ray.direction;
		Point3d src = ray.source;
		
		float a = dir.dx*dir.dx+dir.dy*dir.dy;
		float b = 2*src.x*dir.dx+2*src.y*dir.dy;
		float c = src.x*src.x+src.y*src.y-radius*radius;
		ArrayList<Double> intersectionTs = getBinomialAnswers(a, b, c);
		// intersection points of ray with outer shell
		ArrayList<Point3d> intersectionPoints = getPoints(intersectionTs, src, dir);
		for (Point3d point3d : intersectionPoints) {
			// if intersection is in limits of length of cylinder (between lids)
			if(Math.abs(point3d.z)<length/2){
				possibleIntersections.add(point3d);
			}
		}
		return possibleIntersections;
	}
	/**
	 * retrieve the points on the ray from the given ray and t
	 * (calculate p_0+tv given t,p_0,v)
	 */
	private ArrayList<Point3d> getPoints(ArrayList<Double> intersectionTs, Point3d src, Vector3d dir) {
		ArrayList<Point3d> ans = new ArrayList<>();
		for (Double t : intersectionTs) {
			ans.add(src.plus(dir.times(t.floatValue())));
		}
		return ans;
	}
	
	/**
	 * Get unit normal vector on cylinder.
	 * We first twist the ray, compute on the twisted ray,
	 * and return the ray to original twist.
	 */
	@Override
	public Vector3d getUnitNormalAtPoint(Ray entraceRay, Point3d point) {
		Point3d movedPoint = getTwistedRay(new Ray(point, new Vector3d(0,0,0))).source;
		Vector3d normal  = getNormalAssumingCylIsCanonical(movedPoint);
		Ray normalRay = new Ray(movedPoint, normal);
		Vector3d direction = getUnTwistRay(normalRay).direction;
		return direction;
		
	}
	/**
	 * Calculate normal vector of point assuming cylinder is canonical.
	 * We first detect whether point is on lids of outer shell, then return
	 * the relevant vector.
	 */
	private Vector3d getNormalAssumingCylIsCanonical(Point3d point) {
		//on lid
		if(Math.abs(point.z)>(1-epsilon)*length/2){
			return ((new Vector3d(0,0,1)).times(point.z)).normalized();
		}
		// on galil part
		return (new Vector3d(point.x, point.y, 0)).normalized();
	}

}
