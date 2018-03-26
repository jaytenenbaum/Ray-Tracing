package RayTracing;

import java.util.ArrayList;
/**
 * A class containing a box. A box is defined first by its version
 * assuming we don't rotate around the axes. Then we have its center,
 * its material and the sizes of the box on the different axes.
 * Then we add rotation using the "Vector3d rotationAroundAxes".
 * To save time in computations, we also save the cos and sin of those values.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Box implements ISurface {
	Point3d center;
	float dx;
	float dy;
	float dz;
	Vector3d rotationAroundAxes;
	Vector3d cosAroundAxes;
	Vector3d sinAroundAxes;
	Material mat;
	static final double epsilon = 1.0e-2;
	public Box(Point3d center, float dx, float dy, float dz, Vector3d rotationAroundAxes, Material mat) {
		super();
		this.center = center;
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.rotationAroundAxes = rotationAroundAxes;
		this.mat = mat;
		this.cosAroundAxes = getCosAroundAxes(rotationAroundAxes);
		this.sinAroundAxes = getSinAroundAxes(rotationAroundAxes);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + ((cosAroundAxes == null) ? 0 : cosAroundAxes.hashCode());
		result = prime * result + Float.floatToIntBits(dx);
		result = prime * result + Float.floatToIntBits(dy);
		result = prime * result + Float.floatToIntBits(dz);
		result = prime * result + ((mat == null) ? 0 : mat.hashCode());
		result = prime * result + ((rotationAroundAxes == null) ? 0 : rotationAroundAxes.hashCode());
		result = prime * result + ((sinAroundAxes == null) ? 0 : sinAroundAxes.hashCode());
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
		Box other = (Box) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (cosAroundAxes == null) {
			if (other.cosAroundAxes != null)
				return false;
		} else if (!cosAroundAxes.equals(other.cosAroundAxes))
			return false;
		if (Float.floatToIntBits(dx) != Float.floatToIntBits(other.dx))
			return false;
		if (Float.floatToIntBits(dy) != Float.floatToIntBits(other.dy))
			return false;
		if (Float.floatToIntBits(dz) != Float.floatToIntBits(other.dz))
			return false;
		if (mat == null) {
			if (other.mat != null)
				return false;
		} else if (!mat.equals(other.mat))
			return false;
		if (rotationAroundAxes == null) {
			if (other.rotationAroundAxes != null)
				return false;
		} else if (!rotationAroundAxes.equals(other.rotationAroundAxes))
			return false;
		if (sinAroundAxes == null) {
			if (other.sinAroundAxes != null)
				return false;
		} else if (!sinAroundAxes.equals(other.sinAroundAxes))
			return false;
		return true;
	}

	public Point3d getCenter() {
		return center;
	}

	public void setCenter(Point3d center) {
		this.center = center;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getDz() {
		return dz;
	}

	public void setDz(float dz) {
		this.dz = dz;
	}

	public Vector3d getRotationAroundAxes() {
		return rotationAroundAxes;
	}

	public void setRotationAroundAxes(Vector3d rotationAroundAxes) {
		this.rotationAroundAxes = rotationAroundAxes;
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

	public static double getEpsilon() {
		return epsilon;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	@Override
	public String toString() {
		return "Box [center=" + center + ", dx=" + dx + ", dy=" + dy + ", dz=" + dz + ", rotationAroundAxes="
				+ rotationAroundAxes + ", cosAroundAxes=" + cosAroundAxes + ", sinAroundAxes=" + sinAroundAxes
				+ ", mat=" + mat + "]";
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
	/**
	 * Calculate the intersection of a box and a ray.
	 * We first twist the ray, to assume the box isn't rotated around axes,
	 * and is centered at (0,0,0).
	 * Then we compute the best intersection under that assumption.
	 * Finally we rotate the intersection back to get the wanted answer.
	 */
	@Override
	public Point3d getIntersection(Ray r) {
		Ray movedRay = getTwistedRay(r);
		Point3d intersection = getIntersectionAssumingBoxIsCanonical(movedRay);
		if(intersection==null){
			return null;
		}
		return getUnTwistRay(new Ray(intersection, new Vector3d(0,0,0))).source;
		
	}
	/**
	 * Get the ray assuming box is centered at (0,0,0) and not twisted.
	 * First move the ray, and then twist it. 
	 */
	Ray getTwistedRay(Ray r) {
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
	 * Get intersection of box with the ray, assuming
	 * we rotated the ray already. (box is canonical)
	 * We first compute all 6 possible intersections, then
	 * Chose the best one.
	 */
	private Point3d getIntersectionAssumingBoxIsCanonical(Ray movedRay) {
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
	 * Get the possible intersections of the given ray with the box.
	 * We go over all 6 planes of the box, intersect with them,
	 * and add an intersection of the ray intersects the plane
	 * inside the borders of the box. 
	 */
	private ArrayList<Point3d> getPossibleIntersectionsCanonical(Ray movedRay) {

		ArrayList<Point3d> possibleIntersections = new ArrayList<>();
		//right plane
		Plane xRight = new Plane(new Vector3d(1,0,0), dx/2);
		Point3d intersecxRight = xRight.getIntersection(movedRay);
		if(intersecxRight!=null && Math.abs(intersecxRight.y)<dy/2 && Math.abs(intersecxRight.z)<dz/2){
			possibleIntersections.add(intersecxRight);
		}
		//left plane		
		Plane xLeft = new Plane(new Vector3d(1,0,0), -dx/2);
		Point3d intersecxLeft = xLeft.getIntersection(movedRay);
		if(intersecxLeft!=null && Math.abs(intersecxLeft.y)<dy/2 && Math.abs(intersecxLeft.z)<dz/2){
			possibleIntersections.add(intersecxLeft);
		}
		//down plane
		Plane yDown = new Plane(new Vector3d(0,1,0), -dy/2);
		Point3d intersecyDown = yDown.getIntersection(movedRay);
		if(intersecyDown!=null && Math.abs(intersecyDown.x)<dx/2 && Math.abs(intersecyDown.z)<dz/2){
			possibleIntersections.add(intersecyDown);
		}
		//up plane
		Plane yUp = new Plane(new Vector3d(0,1,0), dy/2);
		Point3d intersecyUp = yUp.getIntersection(movedRay);
		if(intersecyUp!=null && Math.abs(intersecyUp.x)<dx/2 && Math.abs(intersecyUp.z)<dz/2){
			possibleIntersections.add(intersecyUp);
		}
		//inward plane
		Plane zIn = new Plane(new Vector3d(0,0,1), dz/2);
		Point3d interseczIn = zIn.getIntersection(movedRay);
		if(interseczIn!=null && Math.abs(interseczIn.x)<dx/2 && Math.abs(interseczIn.y)<dy/2){
			possibleIntersections.add(interseczIn);
		}
		//outward plane
		Plane zOut = new Plane(new Vector3d(0,0,1), -dz/2);
		Point3d interseczOut = zOut.getIntersection(movedRay);
		if(interseczOut!=null && Math.abs(interseczOut.x)<dx/2 && Math.abs(interseczOut.y)<dy/2){
			possibleIntersections.add(interseczOut);
		}

		return possibleIntersections;
	}
	/**
	 * Get unit normal vector on box.
	 * We first twist the ray, compute on the twisted ray,
	 * and return the ray to original twist.
	 */
	@Override
	public Vector3d getUnitNormalAtPoint(Ray entraceRay, Point3d point) {
		Point3d movedPoint = getTwistedRay(new Ray(point, new Vector3d(0,0,0))).source;
		Vector3d normal  = getNormalAssumingBoxIsCanonical(movedPoint);
		Ray normalRay = new Ray(movedPoint, normal);
		Vector3d direction = getUnTwistRay(normalRay).direction;
		return direction;
		
	}
	/**
	 * Calculate normal vector of point assuming box is canonical.
	 * We first detect which of the the 6 sides of the box
	 * the point is on. Then take unit vector on the relevant axis.
	 */
	private Vector3d getNormalAssumingBoxIsCanonical(Point3d point) {
		// point is on right/left planes
		if (Math.abs(point.x) > (1 - epsilon) * dx / 2) {
			return ((new Vector3d(1, 0, 0)).times(point.x)).normalized();
		}
		// point is on up/down planes
		if (Math.abs(point.y) > (1 - epsilon) * dy / 2) {
			return ((new Vector3d(0, 1, 0)).times(point.y)).normalized();
		}
		// point is on inward/outward planes
		return ((new Vector3d(0, 0, 1)).times(point.z)).normalized();

	}

	@Override
	public Material getMat() {
		return mat;
	}

}
