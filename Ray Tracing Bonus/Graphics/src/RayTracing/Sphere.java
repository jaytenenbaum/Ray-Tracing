package RayTracing;
/**
 * A class containing a sphere (with its 3d-point center, the radius and the material)
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Sphere implements ISurface {
	Point3d center;
	float radius;
	Material mat;

	/**
	 * Get intersection with a ray r.
	 * 
	 * Algorithm:
	 * We normalize the ray, and calculate the values t for which
	 * r(t) = p_0+tv will be on the circle. Then we check which of the t's
	 * are in front of the ray, and take the minimum of them. We finally return
	 * the point represented by that t.
	 */
	@Override
	public Point3d getIntersection(Ray r) {
		Ray normalizedRay = r.getNormalizedRay();
		Point3d p_0 = normalizedRay.source;
		Vector3d v = normalizedRay.direction;
		Vector3d l = center.minus(p_0);
		float t_ca = l.dot(v);
		if(t_ca<0){
			return null;
		}
		float d_sqrd = l.dot(l)-t_ca*t_ca;
		if(d_sqrd>radius*radius){
			return null;
		}
		float t_hc = (float) Math.sqrt(radius*radius-d_sqrd);
		
		Float best_t = minLargerThan0(t_ca-t_hc,t_ca+t_hc);
		if(best_t!=null){
		return p_0.plus(v.times(best_t));
		} else {
			return null;
		}
	}
	/**
	 * Return the minimum float out of the two, which is larger than 0. Return
	 * 0 otherwise.
	 */
	private Float minLargerThan0(float f, float g) {
		if (f > 0 && g > 0) {
			return (float) Math.min(f, g);
		} else if (f > 0) {
			return f;
		} else if (g > 0) {
			return g;
		} else {
			return null;
		}
	}
	/**
	 * Get the unit normal vector pointing out of the circle starting from a given point
	 * on the circle. Simply the vector connecting the center and the point.
	 */
	public Vector3d getUnitNormalOnPoint(Point3d p) {
		return p.minus(center).normalized();
	}

	public Sphere(Point3d center, float radius, Material mat) {
		super();
		this.center = center;
		this.radius = radius;
		this.mat = mat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + ((mat == null) ? 0 : mat.hashCode());
		result = prime * result + Float.floatToIntBits(radius);
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
		Sphere other = (Sphere) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (mat == null) {
			if (other.mat != null)
				return false;
		} else if (!mat.equals(other.mat))
			return false;
		if (Float.floatToIntBits(radius) != Float.floatToIntBits(other.radius))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sphere [center=" + center + ", radius=" + radius + ", mat=" + mat + "]";
	}

	public Point3d getCenter() {
		return center;
	}

	public void setCenter(Point3d center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}
	
	/**
	 * Get the unit normal vector pointing out of the circle starting from a given point
	 * on the circle, given the entrance ray. The point defines it in a one-to-one correspondence. 
	 */
	@Override
	public Vector3d getUnitNormalAtPoint(Ray entraceRay, Point3d point) {
		return getUnitNormalOnPoint(point);
	}

}
