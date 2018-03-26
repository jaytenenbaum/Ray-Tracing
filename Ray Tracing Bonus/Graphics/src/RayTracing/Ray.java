package RayTracing;
/**
 * A class representing the ray (has a source and a direction)
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Ray {
	Point3d source;
	Vector3d direction;
	/**
	 * Get the normalized ray starting in the source with the same direction 
	 */
	public Ray getNormalizedRay(){
		return new Ray(source, direction.normalized());
	}
	public Point3d getSource() {
		return source;
	}
	public void setSource(Point3d source) {
		this.source = source;
	}
	public Vector3d getDirection() {
		return direction;
	}
	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}
	@Override
	public String toString() {
		return "Ray [source=" + source + ", direction=" + direction + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Ray other = (Ray) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
	public Ray(Point3d source, Vector3d direction) {
		super();
		this.source = source;
		this.direction = direction;
	}
	/**
	 * Construct a normalized ray from the camera to the relative point on the screen.
	 * We start by finding the up and right vectors of the screen in 3d.
	 * Then we find top left of screen, and then move down and right relatively.
	 */
	public static Ray constructUnitRayThroughPixel(Camera cam, float relativeX, float relativeY) {
		Point3d p_0 = cam.getPosition();
		Vector3d normalizedLookingDirection = ((cam.getLookAtPoint()).minus(p_0)).normalized();
		Point3d centerOfScreen = p_0.plus(normalizedLookingDirection.times(cam.screenDistance));
		
		Vector3d upDirection = (cam.getUpVector()).normalized();
		Vector3d rightDirection = (normalizedLookingDirection.cross(upDirection).times(-1f)).normalized();
		Point3d topLeftOfScreen = (centerOfScreen.plus(upDirection.times(cam.getScreenHeight()/2))).plus(rightDirection.times(-1*cam.getScreenWidth()/2));
		
		
		
		
		Point3d pointOnScreen = (topLeftOfScreen.plus(rightDirection.times(relativeX*cam.getScreenWidth()))).plus(upDirection.times(-1*relativeY*cam.getScreenHeight()));
		Vector3d normalizedDirection = (pointOnScreen.minus(cam.getPosition())).normalized();
		return new Ray(p_0, normalizedDirection);
	}
	/**
	 * Get the rotated version of the ray around x axis, given by the vectors of cos 
	 * and sin of the angle around x axis.
	 */
	public Ray getRotatedX(Vector3d cosAroundAxes, Vector3d sinAroundAxes) {
		float mathCosRot = cosAroundAxes.dx;
		float mathSinRot = sinAroundAxes.dx;
		Point3d src = this.source;
		Vector3d dir = this.direction;
		Point3d newSrc = new Point3d(src.x, (float) (src.y * mathCosRot - src.z * mathSinRot),
				(float) (src.y * mathSinRot + src.z * mathCosRot));
		Vector3d newDir = new Vector3d(dir.dx, (float) (dir.dy * mathCosRot - dir.dz * mathSinRot),
				(float) (dir.dy * mathSinRot + dir.dz * mathCosRot));
		return new Ray(newSrc, newDir);
	}
	/**
	 * Get the rotated version of the ray around y axis, given by the vectors of cos 
	 * and sin of the angle around y axis.
	 */
	public Ray getRotatedY(Vector3d cosAroundAxes, Vector3d sinAroundAxes) {
		float mathCosRot = cosAroundAxes.dy;
		float mathSinRot = sinAroundAxes.dy;
		Point3d src = this.source;
		Vector3d dir = this.direction;
		Point3d newSrc = new Point3d((float) (src.x * mathCosRot + src.z * mathSinRot),src.y ,
				(float) (src.z * mathCosRot-src.x * mathSinRot));
		Vector3d newDir = new Vector3d((float) (dir.dx * mathCosRot + dir.dz * mathSinRot),dir.dy ,
				(float) (dir.dz * mathCosRot-dir.dx * mathSinRot));
		return new Ray(newSrc, newDir);
	}
	/**
	 * Get the rotated version of the ray around z axis, given by the vectors of cos 
	 * and sin of the angle around z axis.
	 */
	public Ray getRotatedZ(Vector3d cosAroundAxes, Vector3d sinAroundAxes) {
		float mathCosRot = cosAroundAxes.dz;
		float mathSinRot = sinAroundAxes.dz;
		Point3d src = this.source;
		Vector3d dir = this.direction;
		// maybe if z is backwards we should fix this...
		Point3d newSrc = new Point3d((float) (src.x * mathCosRot - src.y * mathSinRot),
				(float) (src.x * mathSinRot + src.y * mathCosRot),src.z);
		Vector3d newDir = new Vector3d((float) (dir.dx * mathCosRot - dir.dy * mathSinRot),
				(float) (dir.dx * mathSinRot + dir.dy * mathCosRot),dir.dz);
		return new Ray(newSrc, newDir);
	}
	/**
	 * Get the real version of the ray given by the vectors of cos 
	 * and sin of the angle around axes. Here we are untwisting a vector back,
	 * So we twist by x->y->z
	 */
	public static Ray untwistRotation(Ray r, Vector3d cosAroundAxes, Vector3d sinAroundAxes) {
		r = r.getRotatedX(cosAroundAxes, sinAroundAxes);
		r = r.getRotatedY(cosAroundAxes, sinAroundAxes);
		r = r.getRotatedZ(cosAroundAxes, sinAroundAxes);
		return r;
	}
	/**
	 * Get the rotated (normalized assuming object isn't twisted)
	 * version of the ray given by the vectors of cos and sin of the angle around
	 * axes. Here we are twisting a vector, so we twist by reverse of x->y->z
	 * which is (-z)->(-y)->(-x)
	 */
	public static Ray twistRotation(Ray r, Vector3d cosAroundAxes, Vector3d sinAroundAxes) {
		r = r.getRotatedZ(cosAroundAxes, sinAroundAxes.times(-1));
		r = r.getRotatedY(cosAroundAxes, sinAroundAxes.times(-1));
		r = r.getRotatedX(cosAroundAxes, sinAroundAxes.times(-1));
		return r;
	}
}
