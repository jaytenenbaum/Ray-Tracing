package RayTracing;
/**
 * A class containing an intersection of a ray with a surface.
 * We hold both the surface intersecting to and the intersection point itself.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Intersection {
	ISurface surface;
	Point3d intersection;
	public ISurface getSurface() {
		return surface;
	}
	public void setSurface(ISurface surface) {
		this.surface = surface;
	}
	public Point3d getIntersection() {
		return intersection;
	}
	public void setIntersection(Point3d intersection) {
		this.intersection = intersection;
	}
	@Override
	public String toString() {
		return "Intersection [surface=" + surface + ", intersection=" + intersection + "]";
	}
	public Intersection(ISurface surface, Point3d intersection) {
		super();
		this.surface = surface;
		this.intersection = intersection;
	}
	
	
}
