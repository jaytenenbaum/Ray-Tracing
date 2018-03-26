package RayTracing;

import Colors.Color;
/**
 * A class containing a point source of light. It has a position,
 * a color, shadow intensity, specular intensity and radius (for soft shadows).
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Light {
	Point3d position;
	Color color;
	float specularIntensity;
	float shadowIntensity;
	float lightRadius;
	public Point3d getPosition() {
		return position;
	}
	public void setPosition(Point3d position) {
		this.position = position;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public float getSpecularIntensity() {
		return specularIntensity;
	}
	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}
	public float getShadowIntensity() {
		return shadowIntensity;
	}
	public Light(Point3d position, Color color, float specularIntensity, float shadowIntensity, float lightRadius) {
		super();
		this.position = position;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lightRadius = lightRadius;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + Float.floatToIntBits(lightRadius);
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + Float.floatToIntBits(shadowIntensity);
		result = prime * result + Float.floatToIntBits(specularIntensity);
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
		Light other = (Light) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (Float.floatToIntBits(lightRadius) != Float.floatToIntBits(other.lightRadius))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (Float.floatToIntBits(shadowIntensity) != Float.floatToIntBits(other.shadowIntensity))
			return false;
		if (Float.floatToIntBits(specularIntensity) != Float.floatToIntBits(other.specularIntensity))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Light [position=" + position + ", color=" + color + ", specularIntensity=" + specularIntensity
				+ ", shadowIntensity=" + shadowIntensity + ", lightRadius=" + lightRadius + "]";
	}
	public void setShadowIntensity(float shadowIntensity) {
		this.shadowIntensity = shadowIntensity;
	}
	public float getLightRadius() {
		return lightRadius;
	}
	public void setLightRadius(float lightRadius) {
		this.lightRadius = lightRadius;
	}

}
