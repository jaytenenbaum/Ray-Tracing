package RayTracing;

public interface ISurface {
	/**
	 * Return the intersection of ray and surface
	 */
	public Point3d getIntersection(Ray r);
	/**
	 * Return the unit ray starting at point and pointing outwards 
	 */
	public Vector3d getUnitNormalAtPoint(Ray entraceRay, Point3d point);
	/**
	 * Return the material of the surface
	 */
	public Material getMat();
}
