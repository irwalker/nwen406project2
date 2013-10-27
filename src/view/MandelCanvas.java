package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class MandelCanvas extends Canvas{	
	
	public MandelCanvas(int width, int height){
		super();
		this.setBounds(0, 50, width, height);	//set the min Y 50 from default so we do not overwrite the buttons	
	}	
	
	public void plot(int px, int py, Color color){
		//draw a dot at this point on the canvas
		Graphics graphics = this.getGraphics();
		graphics.setColor(color);
		graphics.drawLine(px, py, px, py);
	}
	
}
