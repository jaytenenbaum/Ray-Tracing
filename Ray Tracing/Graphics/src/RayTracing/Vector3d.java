package RayTracing;
/**
 * A class holding a 3d vector (dx, dy, dz)
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Vector3d {
	float dx;
	float dy;
	float dz;
	public Vector3d(float dx, float dy, float dz) {
		super();
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	public Vector3d(Vector3d v) {
		super();
		this.dx = v.getDx();
		this.dy = v.getDy();
		this.dz = v.getDz();
	}
	
	public Vector3d(Point3d p) {
		this.dx = p.getX();
		this.dy = p.getY();
		this.dz = p.getZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(dx);
		result = prime * result + Float.floatToIntBits(dy);
		result = prime * result + Float.floatToIntBits(dz);
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
		Vector3d other = (Vector3d) obj;
		if (Float.floatToIntBits(dx) != Float.floatToIntBits(other.dx))
			return false;
		if (Float.floatToIntBits(dy) != Float.floatToIntBits(other.dy))
			return false;
		if (Float.floatToIntBits(dz) != Float.floatToIntBits(other.dz))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Vector3d [dx=" + dx + ", dy=" + dy + ", dz=" + dz + "]";
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
	/**
	 * Cartesian size of the vector
	 */
	public float norm(){
		return (float) Math.sqrt(this.getDx()*this.getDx()+this.getDy()*this.getDy()+this.getDz()*this.getDz());
	}
	/**
	 * Get normalized vector (each axis is divided by the norm)
	 */
	public Vector3d normalized(){
		float norm = this.norm();
		return new Vector3d(this.dx/norm,this.dy/norm,this.dz/norm);
	}
	
	/**
	 * Dot product with another vector 
	 */
	public float dot(Vector3d p) {
		return this.dx*p.dx+this.dy*p.dy+this.dz*p.dz;
	}
	
	public Vector3d plusVector(Vector3d p) {
		return new Vector3d(this.dx+p.dx,this.dy+p.dy,this.dz+p.dz);
	}
	
	public Vector3d minusVector(Vector3d p) {
		return this.plusVector(p.times(-1));
	}
	/**
	 * Multiply vector by constant 
	 */
	public Vector3d times(float f) {
		return new Vector3d(dx*f, dy*f, dz*f);
	}
	
	/** 
	 * Cross product the given vector with this vector
	 */
	public Vector3d cross (Vector3d v) {
		float x,y,z;

		x = (dy*v.dz) - (dz*v.dy);
		y = (dz*v.dx) - (dx*v.dz);
		z = (dx*v.dy) - (dy*v.dx);
		return new Vector3d(x,y,z);
	}
	/**
	 * Applied on a vector of degrees (i.e. (-90,45,90) to get the radian version (-pi/2, pi/4, pi/2)) 
	 */
	public Vector3d degToRad() {
		return new Vector3d((float)Math.toRadians(this.dx),(float)Math.toRadians(this.dy), (float)Math.toRadians(this.dz));
	}
}
