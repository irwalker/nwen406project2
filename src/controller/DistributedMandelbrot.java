package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import view.MandelCanvas;

import model.Palette;

/**
 * Algorithm to handle the distribution of calculation of the mandelbrot set
 * @author iainw
 *
 */
public class DistributedMandelbrot implements Mandelbrot{

	private MandelCanvas canvas;
	private Palette palette;
	private Dimension imageDimension;
	private Rectangle2D range = new Rectangle2D.Double(-2.5d, -1d, 3.5d, 2d);//the scale range for the mandelbrot set, for handling the scaling of pixels later.

	public DistributedMandelbrot(MandelCanvas canvas, Palette palette, Dimension dimensions){
		this.canvas = canvas;
		this.palette = palette;
		imageDimension = new Dimension(canvas.getWidth(),canvas.getHeight());
	}

	/**
	 * Execute the mandelbrot rendering in parallel.
	 * Parallelization is executed by row.
	 */
	@Override
	public void execute() {
		//so we want to split function into seperate rows.
		//Firstly, create a number of ecs grid jobs based on the size of the canvas.?
		//Just as a test, spawn a number of Distributed Scripts.
		//distribution code must go here.
		//Split by column because that's how the standard algorithm renders it. Means that
		//Ideally we can see how this version is faster!

		//OK now let's change this into calling a bash script!

		for(int x=0; x < imageDimension.width;x++){
			//call script
			ProcessBuilder pb = new ProcessBuilder("callscript.sh",Integer.toString(imageDimension.width), Integer.toString(imageDimension.height), palette.currentPallete);

			//need a way to build in fault tolerance. TODO build in fault tolerance
			//to check when a script or node has failed and
		}

		for(int x=0; x < imageDimension.width;x++){
			DistributedScript script = new DistributedScript(x, imageDimension.width, imageDimension.height, palette.currentPallete);
			Color[] colors = script.getColors(); // the array
			for(int y=0;y<imageDimension.height;y++){
				Color pointColor = colors[y];
				canvas.plot(x, y, pointColor);
			}
		}

	}

	/**
	 * <summary>
	 * Calculate the colour at the specified point in the mandelbrot set
	 * this is pretty useless in the distributed system code, since the spawned scripts will actually
	 * be calculating this.
	 * </summary>
	 */
	public Color calculateMandelbrotPoint(double x0, double y0, Palette p){
		double x = 0.0;
		double y = 0.0;
		Color color = null;
		int iteration = 0;

		double xtemp = 0;
		Color[] colors = p.getColours();
		while ((((x * x) + (y * y)) < (2 * 2)) && (iteration < colors.length)) {
			xtemp = (x * x) - (y * y) + x0;
			y = 2 * x * y + y0;
			x = xtemp;
			iteration = iteration + 1;
			color = colors[iteration - 1];
		}
		return color;
	}
}
