package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Palette;

/**
 * This is the class that will be called by the scripts. Handles one node of the entire script process.
 * Palette could be moved to an inner class to simplify the build process.
 * Returns some data.
 * @author walkeriain
 *
 */
public class DistributedScript {

	private Dimension imageDimension;

	private Color[] colors;

	private Rectangle2D range = new Rectangle2D.Double(-2.5d, -1d, 3.5d, 2d);//the scale range for the mandelbrot set, for handling the scaling of pixels later.

		public DistributedScript(int row,int width,int height,String palette){
		System.out.println(palette);
		imageDimension = new Dimension(width,height);
		Palette p = null;
		if(palette.equals(Palette.RAINBOW)){
			p = Palette.rainbow();
		}else if(palette.equals(Palette.GRAY_PALETTE)){
			p = Palette.gray();
		}else if(palette.equals(Palette.GRAY_CIRCULAR)){
			p = Palette.grayScaleCircular();
		}else if(palette.equals(Palette.GRAY_INVERSE_CIRCULAR)){
			p = Palette.negativeGrayScaleCircular();
		}else if(palette.equals(Palette.ICE_BLUE)){
			p = Palette.iceBlue();
		}else if(palette.equals(Palette.FIRE)){
			p = Palette.fire();
		}else if(palette.equals(Palette.BLUE_YELLOW)){
			p = Palette.blueGreenYellow();
		}else if(palette.equals(Palette.PURPLE)){
			p = Palette.purpleOrangeRed();
		}
		execute(row,height,p);
	}

	private void execute(int col, int height, Palette palette){
		System.out.println(palette.currentPallete);
		colors = new Color[height];
		double sx = scaleXPosition(col);
		for(int y=0;y<height;y++){
			//calculate the scaled y coordinate
			double sy = scaleYPosition(y);
			Color pointColor = calculateMandelbrotPoint(sx, sy, palette);
			colors[y] = pointColor; //Y is the height point
		}
	}

	public double scaleYPosition(int y){
		return scale(y, imageDimension.height,range.getY(),range.getMaxY());
	}

	public double scaleXPosition(int x){
		return scale(x, imageDimension.width,range.getX(),range.getMaxX());
	}

	private double scale(int coordinate,int max, double scaleMin, double scaleMax){
		double scaledVal = (((double)coordinate / (double)max) * (scaleMax - scaleMin)) + scaleMin;
		return scaledVal;
	}

	/**
	 * <summary>
	 * Calculate the colour at the specified point in the mandelbrot set
	 * </summary>
	 */
	private Color calculateMandelbrotPoint(double x0, double y0, Palette p){
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

	/**
	 * INPUT - first argument should be the CURRENT ROW	of the thing that we are rendering.
	 * Second argument is the PALETTE currently being used
	 * @param args
	 */
	public static void main(String[] args){
		int row = Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);
		int height = Integer.parseInt(args[2]);
		String palette = args[3];
		DistributedScript distributedScript = new DistributedScript(row,width,height,palette);
	}

	public Color[] getColors(){
		return this.colors;
	}

}
