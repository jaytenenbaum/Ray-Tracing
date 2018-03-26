package RayTracing;
/**
 * A class containing a camera. It has the position,
 * the point it looks at, the up vector, the screen distance,
 * width and height.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Camera {
	Point3d position;
	Point3d lookAtPoint;
	Vector3d upVector;
	float screenDistance;
	float screenWidth;
	float screenHeight;
	
	public float getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}
	public Point3d getPosition() {
		return position;
	}
	public void setPosition(Point3d position) {
		this.position = position;
	}
	public Point3d getLookAtPoint() {
		return lookAtPoint;
	}
	public void setLookAtPoint(Point3d lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
	}
	public Vector3d getUpVector() {
		return upVector;
	}
	public void setUpVector(Vector3d upVector) {
		this.upVector = upVector;
	}
	public float getScreenDistance() {
		return screenDistance;
	}
	public void setScreenDistance(float screenDistance) {
		this.screenDistance = screenDistance;
	}
	public float getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}
	@Override
	public String toString() {
		return "Camera [position=" + position + ", lookAtPoint=" + lookAtPoint + ", upVector=" + upVector
				+ ", screenDistance=" + screenDistance + ", screenWidth=" + screenWidth + ", screenHeight="
				+ screenHeight + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lookAtPoint == null) ? 0 : lookAtPoint.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + Float.floatToIntBits(screenDistance);
		result = prime * result + Float.floatToIntBits(screenHeight);
		result = prime * result + Float.floatToIntBits(screenWidth);
		result = prime * result + ((upVector == null) ? 0 : upVector.hashCode());
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
		Camera other = (Camera) obj;
		if (lookAtPoint == null) {
			if (other.lookAtPoint != null)
				return false;
		} else if (!lookAtPoint.equals(other.lookAtPoint))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (Float.floatToIntBits(screenDistance) != Float.floatToIntBits(other.screenDistance))
			return false;
		if (Float.floatToIntBits(screenHeight) != Float.floatToIntBits(other.screenHeight))
			return false;
		if (Float.floatToIntBits(screenWidth) != Float.floatToIntBits(other.screenWidth))
			return false;
		if (upVector == null) {
			if (other.upVector != null)
				return false;
		} else if (!upVector.equals(other.upVector))
			return false;
		return true;
	}
	public Camera(Point3d position, Point3d lookAtPoint, Vector3d upVector, float screenDistance, float screenWidth, float screenHeight) {
		super();
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	
}
