package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Palette;
import view.MandelCanvas;

public class StandardMandelbrot implements Mandelbrot{

	private MandelCanvas canvas;
	private Palette palette = Palette.rainbow(); //default to rainbow because rainbow is nice.

	private Rectangle2D range = new Rectangle2D.Double(-2.5d, -1d, 3.5d, 2d);//the scale range for the mandelbrot set, for handling the scaling of pixels later.

	private BufferedImage bmp; //may decide to simply render the generated image to a file., at least at first. Can do on canvas later if time permits.

	private int byteLen = 0; //byte length of the image

	private boolean useAnisotropicFilter = false; //flag to decide whether to apply the sexy filter

	private int _anisotropicIterationCount = 5;//iteration count for anisotropy

	private Dimension imageDimension;

	private boolean isCurrent = false;//flag to determine whether the image has been drawn, currently.

	private static int maxIterations = 1000;

	/*
	 * Note there are special rendering effects which I can add later on
	 * TODO
	 */

	public StandardMandelbrot(MandelCanvas canvas, Palette chosenPalette){
		this.canvas = canvas;
		this.palette = chosenPalette;
		imageDimension = new Dimension(canvas.getWidth(),canvas.getHeight());
		initBmp();
	}

	public StandardMandelbrot(int width, int height){
		imageDimension = new Dimension(width,height);
		initBmp();
	}

	/**
	 * Set the image size, and re-initialize the image accordingly (since cannot dynamically adjust
	 * the image size without creating a new image
	 */
	public void setImageSize(Dimension d){
		imageDimension = d;
		//re-initialize the image
		initBmp();
	}

	/**
	 * Do initilization stuff for the bitmap image.
	 */
	public void initBmp(){
		bmp = new BufferedImage(imageDimension.width,imageDimension.height,BufferedImage.TYPE_INT_RGB);
		isCurrent = false;
	}

	/**
	 * Test method to draw the generated pallete. Test whether it is to out liking..
	 */
	public void testPallete(Palette palette){

		BufferedImage image = (BufferedImage)palette.getPaletteImage();
		try{
			File outputFile = new File("test.png");
			ImageIO.write(image,"bmp",outputFile);
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public void setPalette(Palette p){
		this.palette = p;
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
	 * Set a pixel on the bitmap image array surface.
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setPixel24Bpp(int x, int y, Color color,Graphics2D gpx){
		System.out.println("x;"+x+"y;"+y);
		gpx.setColor(color);
		gpx.drawLine(x, y, x, y);
	}

	@Override
	public void execute() {
		Graphics2D gpx = bmp.createGraphics();
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		for(int x = 0; x < width; x++){
			//calculate the scaled x coordinate
			double sx = scaleXPosition(x);
			for(int y =0; y < height; y++){
				//calculate the scaled y coordinate
				double sy = scaleYPosition(y);
				Color pointColor = calculateMandelbrotPoint(sx, sy, palette);
				//plot the point
				canvas.plot(x,y,pointColor);
				setPixel24Bpp(x, y, pointColor,gpx);
			}
		}
		//save the image
		try{
			File outputFile = new File("test.png");
			ImageIO.write(bmp,"bmp",outputFile);
		}catch(IOException e){
			System.err.println(e);
		}
	}

	/**
	 * <summary>
	 * Calculate the colour at the specified point in the mandelbrot set
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
