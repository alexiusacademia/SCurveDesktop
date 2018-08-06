package scurvedesktop.commons;

public class SCurveNode {
	private double abscissa;
	private double ordinate;
	
	public SCurveNode(double x, double y) {
		this.abscissa = x;
		this.ordinate = y;
	}
	
	public double getAbscissa() {
		return this.abscissa;
	}
	
	public double getOrdinate() {
		return this.ordinate;
	}
}
