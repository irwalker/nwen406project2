package controller;

import view.MandelFrame;

public class Base {
	
	private MandelFrame frame;
	
	public Base(){
		frame = new MandelFrame("MandelBrot Set Demonstration");
	}	
	
	public static void main(String[] args){
		new Base();
	}

}
