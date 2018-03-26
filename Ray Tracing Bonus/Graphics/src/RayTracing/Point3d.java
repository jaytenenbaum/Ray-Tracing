package RayTracing;
/**
 * A class holding a 3d point (x, y, z)
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Point3d {
	float x;
	float y;
	float z;
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	@Override
	public String toString() {
		return "Point3d [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Point3d other = (Point3d) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	public Point3d(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Point3d(Vector3d v) {
		super();
		this.x = v.dx;
		this.y = v.dy;
		this.z = v.dz;
	}
	
	public Vector3d minus(Point3d p){
		return new Vector3d(this.x-p.x, this.y-p.y, this.z-p.z);
	}
	
	public Point3d plus(Vector3d v){
		return new Point3d(this.x+v.dx, this.y+v.dy, this.z+v.dz);
	}
	/**
	 * Dot product (axis-wise) with vector
	 */
	public float dot(Vector3d v){
		return this.x*v.dx + this.y*v.dy + this.z*v.dz;
	}
	/**
	 * Distance from point p is the norm of one minus the second
	 */
	public float distance(Point3d p){
		return (this.minus(p)).norm();
	}
	
}
