package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import model.Palette;

import controller.DistributedMandelbrot;
import controller.StandardMandelbrot;

public class MandelFrame extends JFrame{

	private MandelCanvas canvas;

	public MandelFrame(String title){
		super(title);
		setUpGUI();
	}

	private void setUpGUI(){
		addButtons();
		addCanvas();
		this.pack();
		this.setVisible(true);
		this.setSize(1000, 1000);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Add the canvas
	 */
	private void addCanvas(){
		canvas = new MandelCanvas(1000, 1000);
		canvas.setVisible(true);
		canvas.setPreferredSize(new Dimension(1000, 1000));
		this.add(canvas);
	}

	/**
	 * Setup the buttons
	 */
	private void addButtons(){
		Container content = this.getContentPane();
	    content.setBackground(Color.white);
	    content.setLayout(new FlowLayout());
		//create a menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLUE);

		JButton standardButton = new JButton("Standard Mandelbrot");
		standardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//get a few options off of the user.
				int width=  Integer.parseInt(JOptionPane.showInputDialog("Input width of result image"));
				int height = Integer.parseInt(JOptionPane.showInputDialog("Input height of result image"));
				canvas.setPreferredSize(new Dimension(width,height));
				canvas.setBounds(10, 50, width, height);

				//create a list of options for the palette.
				PaletteList paletteList = new PaletteList("Choose a Color Scheme");
				paletteList.displayOptions();
				Palette chosenPalette = paletteList.getChosenPalette();

				//Execute the standard mandelbrot action
				StandardMandelbrot mandelbrot = new StandardMandelbrot(canvas, chosenPalette);
				//mandelbrot.testPallete(chosenPalette);
				mandelbrot.execute(); //TODO uncomment this for the final product
			}
		});
		menuBar.add(standardButton);

		JButton distributedButton = new JButton("Distributed Mandelbrot");
		distributedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//get a few options off of the user.
				int width=  Integer.parseInt(JOptionPane.showInputDialog("Input width of result image"));
				int height = Integer.parseInt(JOptionPane.showInputDialog("Input height of result image"));
				canvas.setPreferredSize(new Dimension(width,height));
				canvas.setBounds(10, 50, width, height);

				//create a list of options for the palette.
				PaletteList paletteList = new PaletteList("Choose a Color Scheme");
				paletteList.displayOptions();
				Palette chosenPalette = paletteList.getChosenPalette();
				System.out.println(chosenPalette.currentPallete);

				DistributedMandelbrot mandelbrot = new DistributedMandelbrot(canvas, chosenPalette, new Dimension(width,height));
				mandelbrot.execute();
			}
		});
		menuBar.add(distributedButton);
		content.add(menuBar);
	}

	/**
	 * Simple inner class to allow for the selection of a palette.
	 * @author iainw
	 *
	 */
	private class PaletteList extends JFrame{

		private Palette chosenPalette = Palette.fire();//default to fire

		public PaletteList(String title){
			super(title);
		}

		public void displayOptions(){
			Object[] possibilities = Palette.getOptions();
			String s = (String) JOptionPane.showInputDialog(this,"Select the colour scheme you would prefer",
					"Choose Colour Scheme",
					JOptionPane.PLAIN_MESSAGE,
					null,
					possibilities,
					"Rainbow");
			if(s.equals("Rainbow")){
				chosenPalette = Palette.rainbow();
			}else if(s.equals("Gray")){
				chosenPalette = Palette.gray();
			}else if(s.equals("Gray Circular")){
				chosenPalette = Palette.grayScaleCircular();
			}else if(s.equals("Gray Inverse Circular")){
				chosenPalette = Palette.negativeGrayScaleCircular();
			}else if(s.equals("Ice Blue")){
				chosenPalette = Palette.iceBlue();
			}else if(s.equals("Fire")){
				chosenPalette = Palette.fire();
			}else if(s.equals("Blue to Yellow")){
				chosenPalette = Palette.blueGreenYellow();
			}else if(s.equals("Purple")){
				chosenPalette = Palette.purpleOrangeRed();
			}else{//default to Fire because it looks best.
				chosenPalette = Palette.fire();
			}
		}

		public Palette getChosenPalette(){
			return chosenPalette;
		}
	}


}
