package Colors;
/**
 * A class representing a color with (r,g,b), where each 
 * value is a coefficient between 0 and 1 meaning how
 * strong is each component of color.
 * @author Jay Tenenbaum + Alon Goldberg
 */
public class Color {
	public float rCoeff; //between 0 and 1
	public float gCoeff; //between 0 and 1
	public float bCoeff; //between 0 and 1
	public Color(float rCoeff, float gCoeff, float bCoeff) {
		super();
		this.rCoeff = rCoeff;
		this.gCoeff = gCoeff;
		this.bCoeff = bCoeff;
	}
	
	public Color(Color col) {
		super();
		this.rCoeff = col.rCoeff;
		this.gCoeff = col.gCoeff;
		this.bCoeff = col.bCoeff;
	}
	
	public Color(double rCoeff, double gCoeff, double bCoeff) {
		this.rCoeff = (float) rCoeff;
		this.gCoeff = (float) gCoeff;
		this.bCoeff = (float) bCoeff;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(bCoeff);
		result = prime * result + Float.floatToIntBits(gCoeff);
		result = prime * result + Float.floatToIntBits(rCoeff);
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
		Color other = (Color) obj;
		if (Float.floatToIntBits(bCoeff) != Float.floatToIntBits(other.bCoeff))
			return false;
		if (Float.floatToIntBits(gCoeff) != Float.floatToIntBits(other.gCoeff))
			return false;
		if (Float.floatToIntBits(rCoeff) != Float.floatToIntBits(other.rCoeff))
			return false;
		return true;
	}
	
	public Color addColor(Color col){
		return new Color(this.rCoeff + col.rCoeff, this.gCoeff + col.gCoeff, this.bCoeff + col.bCoeff);
	}
	
	public Color mulColor(Color col){
		return new Color(this.rCoeff * col.rCoeff, this.gCoeff * col.gCoeff, this.bCoeff * col.bCoeff);
	}
	
	public Color mulScalar(float scalar){
		return new Color(this.rCoeff * scalar, this.gCoeff * scalar, this.bCoeff * scalar);
	}
	
	public Color addNum(float num){
		return new Color(this.rCoeff + num, this.gCoeff + num, this.bCoeff + num);
	}
	
	@Override
	public String toString() {
		return "Color [rCoeff=" + rCoeff + ", gCoeff=" + gCoeff + ", bCoeff=" + bCoeff + "]";
	}
	
	public float getrCoeff() {
		return rCoeff;
	}
	
	public void setrCoeff(float rCoeff) {
		this.rCoeff = rCoeff;
	}
	
	public float getgCoeff() {
		return gCoeff;
	}
	
	public void setgCoeff(float gCoeff) {
		this.gCoeff = gCoeff;
	}
	
	public float getbCoeff() {
		return bCoeff;
	}
	
	public void setbCoeff(float bCoeff) {
		this.bCoeff = bCoeff;
	}
}
