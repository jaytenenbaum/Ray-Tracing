package RayTracing;

import java.util.ArrayList;

import Colors.Color;
/**
 * A class containing the whole environment of the ray tracing.
 * Contains the different objects (surfaces), the lights, the camera
 * and the background color of the scene.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Environment {
	ArrayList<ISurface> surfaces;
	ArrayList<Light> lights;
	Color backgroundColor;
	Camera cam;
	public Camera getCam() {
		return cam;
	}


	public void setCam(Camera cam) {
		this.cam = cam;
	}
	
	public Environment() {
		this.surfaces = new ArrayList<ISurface>();
		this.lights = new ArrayList<Light>();
	}


	public ArrayList<ISurface> getSurfaces() {
		return surfaces;
	}


	public void setSurfaces(ArrayList<ISurface> surfaces) {
		this.surfaces = surfaces;
	}


	public ArrayList<Light> getLights() {
		return lights;
	}


	public void setLights(ArrayList<Light> lights) {
		this.lights = lights;
	}


	public Color getBackgroundColor() {
		return backgroundColor;
	}


	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}


	@Override
	public String toString() {
		return "Environment [surfaces=" + surfaces + ", lights=" + lights + ", backgroundColor=" + backgroundColor
				+ ", cam=" + cam + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((backgroundColor == null) ? 0 : backgroundColor.hashCode());
		result = prime * result + ((lights == null) ? 0 : lights.hashCode());
		result = prime * result + ((surfaces == null) ? 0 : surfaces.hashCode());
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
		Environment other = (Environment) obj;
		if (backgroundColor == null) {
			if (other.backgroundColor != null)
				return false;
		} else if (!backgroundColor.equals(other.backgroundColor))
			return false;
		if (lights == null) {
			if (other.lights != null)
				return false;
		} else if (!lights.equals(other.lights))
			return false;
		if (surfaces == null) {
			if (other.surfaces != null)
				return false;
		} else if (!surfaces.equals(other.surfaces))
			return false;
		return true;
	}

}
