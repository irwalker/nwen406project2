package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Class to handle to palette setup and retrieveal options.
 * May model this as just a large array of colors, starting from one end
 * of spectrum moving to another.
 * @author iainw
 *
 */
public class Palette {

	//Palette options
	public static final String RAINBOW = "Rainbow";
	public static final String GRAY_PALETTE = "Gray";
	public static final String GRAY_CIRCULAR = "Gray Circular";
	public static final String GRAY_INVERSE_CIRCULAR = "Gray Inverse Circular";
	public static final String ICE_BLUE = "Ice Blue";
	public static final String BLUE_YELLOW = "Blue to Yellow";
	public static final String PURPLE = "Purple";
	public static final String FIRE = "Fire";

	private static final String[] options = {"Rainbow","Gray","Gray Circular","Gray Inverse Circular","Ice Blue","Fire","Blue to Yellow","Purple"};

	//Colors we need. some are not defined in Java, so just put them here for consistencies sake.s
	private static final Color RED = Color.RED;
	private static final Color ORANGE = Color.ORANGE;
	private static final Color YELLOW = Color.YELLOW;
	private static final Color GREEN = Color.GREEN;
	private static final Color BLUE = Color.BLUE;
	private static final Color VIOLET = new Color(138, 43, 226); // Not with Java
	private static final Color INDIGO = new Color(75, 0, 130);   // Not with Java
	private static final Color BLACK = Color.BLACK;
	private static final Color WHITE = Color.WHITE;
	private static final Color CYAN = Color.CYAN;
	private static final Color GRAY = Color.GRAY;
	private static final Color LIGHTGREEN = new Color(0x40, 0xFF, 0x40); //Not with Java
	private static final Color VERY_DARK_MAGENTA = new Color(0xFF, 0x80, 0xFF); //Not with this version of java

	public Color[] colours;

	//holds a bitmap that shows the palette.
	private static BufferedImage bmp = null;

	private static Palette rainbow = null;
	private static Palette gray = null;
	private static Palette grayCircle = null;
	private static Palette grayCircleNeg = null;
	private static Palette iceBlue = null;
	private static Palette fire = null;
	private static Palette blueGreenYellow = null;
	private static Palette purpleOrangeRed = null;

	public static String currentPallete;

	public static Palette rainbow(){
		if(rainbow == null){
			rainbow = Palette.createGradient(new Color[] {BLUE, INDIGO, VIOLET, RED, ORANGE, YELLOW, GREEN }, 1000);
		}
		currentPallete = RAINBOW;
		return rainbow;
	}

	public static Palette gray(){
		if(gray == null){
			gray = Palette.createGradient(new Color[] {BLACK,WHITE}, 256);
		}
		currentPallete = GRAY_PALETTE;
		return gray;
	}

	public static Palette grayScaleCircular(){
		if(grayCircle == null){
			grayCircle = Palette.createGradient(new Color[] {BLACK, WHITE, BLACK}, 512);
		}
		currentPallete = GRAY_CIRCULAR;
		return grayCircle;
	}

	public static Palette negativeGrayScaleCircular(){
		if(grayCircleNeg == null){
			grayCircleNeg = Palette.createGradient(new Color[] {WHITE,BLACK,WHITE}, 512);
		}
		currentPallete = GRAY_INVERSE_CIRCULAR;
		return grayCircleNeg;
	}

	public static Palette iceBlue(){
		if(iceBlue == null){
			iceBlue = Palette.createGradient(new Color[]{BLACK,BLUE,CYAN,WHITE}, 1024);
		}
		currentPallete = ICE_BLUE;
		return iceBlue;
	}

	public static Palette fire(){
		if(fire == null){
			fire = Palette.createGradient(new Color[]{BLACK,RED,ORANGE,YELLOW,GRAY,WHITE},1024);
		}
		currentPallete = FIRE;
		return fire;
	}

	public static Palette blueGreenYellow(){
		if(blueGreenYellow == null){
			blueGreenYellow = Palette.createGradient(new Color[]{BLUE,GREEN,LIGHTGREEN,YELLOW}, 1024);
		}
		currentPallete = BLUE_YELLOW;
		return blueGreenYellow;
	}

	public static Palette purpleOrangeRed(){
		if(purpleOrangeRed == null){
			purpleOrangeRed = Palette.createGradient(new Color[]{VERY_DARK_MAGENTA,ORANGE,RED,WHITE}, 1024);
		}
		currentPallete = PURPLE;
		return purpleOrangeRed;
	}

	/**
	 * calculate and return an image that displays the palette color range.
	 * @return
	 */
	public BufferedImage getPaletteImage(){
		if(bmp == null){
			int width = 200;
			int height = 25;

			//create a new bitmap 200x25
			bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			//need a graphics sort of object to write into the image.
			Graphics2D gfx = bmp.createGraphics();

			//calculate the number of pixles per colour for the palette
			float perColor = (float)width / colours.length;

			for(int c=0;c<colours.length;c++){
				//calculate the x coordinate of this color
				int px = (int)(c*perColor);

				//draw a line at that point, down the image.
				gfx.setColor(colours[c]); //set the color correctly first
				gfx.drawLine(px, 0, px, height);
			}
		}
		return bmp;
	}

	/**
	 * Create a new color palette from a gradient. The colour points are spread out evenly
	 * and a gradient is calculated between them.
	 * @param colors
	 * @param length
	 * @return
	 */
	public static Palette createGradient(Color[] colourPoints, int length){
		//calculate the length of each gradient section
		int gradientLength = length / (colourPoints.length - 1);

		//if the color points do not divide perfectly, some padding will be needed
		int pad = length & colourPoints.length;

		//current palette index
		int index = 0;

		//the output palette, return result.
		Palette result = new Palette(length);

		//the last color (must be a last and current color to calculate the gradient
		Color last = null;
		Color current = null;
		for(int i = 0; i < colourPoints.length; i++){
			current = colourPoints[i];
			//does the last colour have a value?
			if(last != null){
				//calculate the amount to shift each component:
				int delta_r = current.getRed() - last.getRed();
				int delta_g = current.getGreen() - last.getGreen();
				int delta_b = current.getBlue() - last.getBlue();

				//calculate the per-colour increment
				float shift_r = (float)delta_r / (float)gradientLength;
				float shift_g = (float)delta_g / (float)gradientLength;
				float shift_b = (float)delta_b / (float)gradientLength;

				//create a step-color for each color in the gradient range
				for(int j=0;j<gradientLength;j++){
					//calculate the new red,green and blue values. clamp the upper boundaries
					int r = (int)((float)last.getRed() + (shift_r * (float)j)); if (r > 255) r = 255; if (r < 0) r = 0;
					int g = (int)((float)last.getGreen() + (shift_g * (float)j)); if (g > 255) g = 255; if (g < 0) g = 0;
                    int b = (int)((float)last.getBlue() + (shift_b * (float)j)); if (b > 255) b = 255; if (b < 0) b = 0;

                    //create and store the actual color
                    result.colours[index]=  new Color(r,g,b);;
                    //increment the index
                    index++;
				}
			}
			last = current;
		}

		//populate any remaining colours in the palette
		while(index < result.colours.length){
			result.colours[index++] = last;
		}
		//return the palette.
		return result;
	}

	public Palette(int length){
		colours = new Color[length];
	}

	public Color[] getColours(){
		return colours;
	}

	public static Object[] getOptions(){
		return options;
	}

}
