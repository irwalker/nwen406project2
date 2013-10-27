package controller;

import java.awt.Color;

import model.Palette;

public interface Mandelbrot {
	
	public void execute();
	
	/**
	 * 
	 * @param x0
	 * @param y0
	 * @param p
	 * @return
	 */
	public Color calculateMandelbrotPoint(double x0, double y0, Palette p);

}
