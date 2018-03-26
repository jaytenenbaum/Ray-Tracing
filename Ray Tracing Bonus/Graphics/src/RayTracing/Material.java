package RayTracing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Colors.Color;
/**
 * A material of an object, containing constants for computing
 * Reflection, diffuse etc...
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Material {
	Color diffuseColor;
	Color specularColor;
	float phongSpecularityCoeff;
	Color reflectionColor;
	float transparency;
	int specialNumber;
	String imageLoc;
	BufferedImage image;
	int id;
	
	public Material(int id, Color diffuseColor, Color specularColor, float phongSpecularityCoeff, Color reflectionColor,
			float transparency, int specialNumber, String imageLoc) {
		super();
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.phongSpecularityCoeff = phongSpecularityCoeff;
		this.reflectionColor = reflectionColor;
		this.transparency = transparency;
		this.specialNumber = specialNumber;
		this.imageLoc = imageLoc;
		this.id = id;
		if (imageLoc != null) {
			try {
				image = ImageIO.read(new File(imageLoc));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean isSpecial(){
		return specialNumber != 0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diffuseColor == null) ? 0 : diffuseColor.hashCode());
		result = prime * result + Float.floatToIntBits(phongSpecularityCoeff);
		result = prime * result + ((reflectionColor == null) ? 0 : reflectionColor.hashCode());
		result = prime * result + ((specularColor == null) ? 0 : specularColor.hashCode());
		result = prime * result + Float.floatToIntBits(transparency);
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
		Material other = (Material) obj;
		if (diffuseColor == null) {
			if (other.diffuseColor != null)
				return false;
		} else if (!diffuseColor.equals(other.diffuseColor))
			return false;
		if (Float.floatToIntBits(phongSpecularityCoeff) != Float.floatToIntBits(other.phongSpecularityCoeff))
			return false;
		if (reflectionColor == null) {
			if (other.reflectionColor != null)
				return false;
		} else if (!reflectionColor.equals(other.reflectionColor))
			return false;
		if (specularColor == null) {
			if (other.specularColor != null)
				return false;
		} else if (!specularColor.equals(other.specularColor))
			return false;
		if (Float.floatToIntBits(transparency) != Float.floatToIntBits(other.transparency))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Material [diffuseColor=" + diffuseColor + ", specularColor=" + specularColor
				+ ", phongSpecularityCoeff=" + phongSpecularityCoeff + ", reflectionColor=" + reflectionColor
				+ ", transparency=" + transparency + ", imageLoc=" + imageLoc +"]";
	}
	public Color getDiffuseColor() {
		return diffuseColor;
	}
	public void setDiffuseColor(Color diffuseColor) {
		this.diffuseColor = diffuseColor;
	}
	public Color getSpecularColor() {
		return specularColor;
	}
	public void setSpecularColor(Color specularColor) {
		this.specularColor = specularColor;
	}
	public float getPhongSpecularityCoeff() {
		return phongSpecularityCoeff;
	}
	public void setPhongSpecularityCoeff(float phongSpecularityCoeff) {
		this.phongSpecularityCoeff = phongSpecularityCoeff;
	}
	public Color getReflectionColor() {
		return reflectionColor;
	}
	public void setReflectionColor(Color reflectionColor) {
		this.reflectionColor = reflectionColor;
	}
	public float getTransparency() {
		return transparency;
	}
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

}
