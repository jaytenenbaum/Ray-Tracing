package RayTracing;
/**
 * A class containing a plane (ax+by+cz = d)
 * Where normal is (a,b,c) and offset is d.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Plane implements ISurface {
	Vector3d normal;
	float offset;
	Material mat;
	public Plane(Vector3d normal, float offset, Material mat) {
		super();
		this.normal = normal;
		this.offset = offset;
		this.mat = mat;
	}
	
	public Plane(Vector3d normal, float offset) {
		this.normal = normal;
		this.offset = offset;
		this.mat = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mat == null) ? 0 : mat.hashCode());
		result = prime * result + ((normal == null) ? 0 : normal.hashCode());
		result = prime * result + Float.floatToIntBits(offset);
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
		Plane other = (Plane) obj;
		if (mat == null) {
			if (other.mat != null)
				return false;
		} else if (!mat.equals(other.mat))
			return false;
		if (normal == null) {
			if (other.normal != null)
				return false;
		} else if (!normal.equals(other.normal))
			return false;
		if (Float.floatToIntBits(offset) != Float.floatToIntBits(other.offset))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Plane [normal=" + normal + ", offset=" + offset + ", mat=" + mat + "]";
	}

	public Vector3d getNormal() {
		return normal;
	}

	public void setNormal(Vector3d normal) {
		this.normal = normal;
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	/**
	 * Return the intersection of given plane with a ray.
	 * We simply calculate the intersection, if exists, using the
	 * Parametric way of the ray. (geometric way)
	 */
	@Override
	public Point3d getIntersection(Ray r) {
		Point3d p_0 = r.source;
		Vector3d v = r.direction.normalized();
		Vector3d N = normal;
		float c = -this.offset;
		float t = -(p_0.dot(N)+c)/(v.dot(N));
		if(t>0){
			return p_0.plus(v.times(t));
		} else {
			return null;
		}
	}

	/**
	 * Return the ray starting from point,
	 * and pointing to the side that the entrance ray
	 * comes from. (We have the normal of the plane as a variable)
	 */
	@Override
	public Vector3d getUnitNormalAtPoint(Ray entraceRay, Point3d point) {
		this.normal = this.normal.normalized();
		float cosAngle =  entraceRay.getNormalizedRay().getDirection().dot(this.normal);
		if (cosAngle <= 0)
			return new Vector3d(this.normal);
		else
			return this.normal.times(-1);
	}

}
