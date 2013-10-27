package controller;

import java.util.List;

/**
 * Probably a terrible name for a class.
 * However! This class is responsible for the creation of ecs grid jobs.
 * Handles the tricky things like fault tolerance etc which I don't want to worry about
 * At a higher level.
 * @author iainw
 *
 */
public class GridHandler {
	
	public GridHandler(List<String> commands){
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
	}

}
