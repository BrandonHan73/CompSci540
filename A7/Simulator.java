
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all methods relevant to simulating the Flappy Bird game. 
 *
 * @author Brandon Han
 */
public class Simulator {

	// Constant values defined by the problem statement
	final int pipeHigh = 100, pipeLow = 0;
	final int defaultStartX = 0, defaultStartY = 50;

	// Configuration based on student ID
	AssignmentConfig config;

	/**
	 * Basic constructor. Stores configuration by reference.
	 *
	 * @param config desired configuration
	 */
	Simulator(AssignmentConfig config) {
		this.config = config;
	}

	/**
	 * Calculates Feature array using the pipe centers and actions from the configuration variable. 
	 * Features are generated after each move is taken. 
	 *
	 * @return resulting features
	 */
	Feature[] calcFeatures() {
		return calcFeatures(config.centers, config.actions);
	}

	/**
	 * Calculates the fitness of a given network based off of the pipe centers in the configuration
	 * variable. Fitness calculation is the total distance travelled minus the distance between the
	 * bird and the last pipe. If the network finished the game, the fitness is the total distance 
	 * travelled.
	 *
	 * @param net the network to evaluate
	 * @return the fitness of the given network
	 */
	double calcFitness(TwoLayerNetwork net) {
		return calcFitness(config.centers, net);
	}

	/**
	 * Calculates the fitness of a given network based off of the pipe centers in the configuration
	 * variable. Fitness calculation is the total distance travelled minus the distance between the
	 * bird and the last pipe. If the network finished the game, the fitness is the total distance 
	 * travelled.
	 *
	 * @param actions the actions taken by the network
	 * @return the fitness of the given network
	 */
	double calcFitness(int[] actions) {
		return calcFitness(config.centers, actions);
	}

	/**
	 * Simulates the given network and returns an array of actions taken.
	 *
	 * @param centers the centers of each pipe
	 * @param net the network to evaluate
	 * @return the actions taken by the network
	 */
	int[] calcActions(int[] centers, TwoLayerNetwork net) {

		// Initialize bird position to (0, 50)
		int birdX = defaultStartX, birdY = defaultStartY;

		int distToPipe = 0;
		// Stores index of the next pipe
		int pipeIndex = 0;

		int action;
		List<Integer> actionList = new ArrayList();

		// Loop through every action
		while(pipeIndex < centers.length) {

			// Calculate desired action
			action = (int) Math.round(net.feedForward(new Feature(distToPipe, centers[pipeIndex] - birdY)));
			actionList.add(action);

			// Update bird location
			birdX++;
			distToPipe--;
			birdY += action == 1 ? config.u : -1;

			// Check for game fail
			if(distToPipe == 0) {
				if(Math.abs(centers[pipeIndex] - birdY) > (config.g / 2)) {
					break;
				}
			}

			// Update pipe index if necessary
			if(distToPipe < 0) {
				distToPipe += config.h;
				pipeIndex++;
			}
			
		}

		int[] out = new int[actionList.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = actionList.get(i);
		}
		return out;
	}

	/**
	 * Calculates the fitness of a given network based off of the given pipe centers. Fitness
	 * calculation is the total distance travelled minus the distance between the bird and the last
	 * pipe. If the network finished the game, the fitness is the total distance travelled.
	 *
	 * @param centers the centers of each pipe
	 * @param actions the actions taken by the network
	 * @return the fitness of the given network
	 */
	double calcFitness(int[] centers, int[] actions) {

		// Initialize bird position to (0, 50)
		int birdX = defaultStartX, birdY = defaultStartY;

		int distToPipe = 0;
		// Stores index of the next pipe
		int pipeIndex = 0;

		// Loop through every action
		for(int action : actions) {

			// Update bird location
			distToPipe--;
			birdX++;
			birdY += action == 1 ? config.u : -1;

			// Check for game fail
			if(distToPipe == 0) {

				if(Math.abs(centers[pipeIndex] - birdY) > (config.g / 2)) {
					return birdX - Math.abs(centers[pipeIndex] - birdY);
				}
			}

			// Update next pipe if needed
			if(distToPipe < 0) {
				pipeIndex++;
				distToPipe += config.h;
			}
			
		}

		return (centers.length - 1) * config.h;
	}

	/**
	 * Calculates the fitness of a given network based off of the given pipe centers. Fitness
	 * calculation is the total distance travelled minus the distance between the bird and the last
	 * pipe. If the network finished the game, the fitness is the total distance travelled.
	 *
	 * @param centers the centers of each pipe
	 * @param net the network to evaluate
	 * @return the fitness of the given network
	 */
	double calcFitness(int[] centers, TwoLayerNetwork net) {

		// Initialize bird position to (0, 50)
		int birdX = defaultStartX, birdY = defaultStartY;

		int distToPipe = 0;
		// Stores index of the next pipe
		int pipeIndex = 0;

		int action;

		// Loop through every action
		while(pipeIndex < centers.length) {

			// Calculate action
			action = (int) Math.round(net.feedForward(new Feature(distToPipe, centers[pipeIndex] - birdY)));

			// Update bird location
			distToPipe--;
			birdX++;
			birdY += action == 1 ? config.u : -1;

			// Check for game fail
			if(distToPipe == 0) {
				if(Math.abs(centers[pipeIndex] - birdY) > (config.g / 2)) {
					return birdX - Math.abs(centers[pipeIndex] - birdY);
				}
			}

			// Update next pipe if needed
			if(distToPipe < 0) {
				pipeIndex++;
				distToPipe += config.h;
			}
			
		}

		return (centers.length - 1) * config.h;
	}

	/**
	 * Calculates Feature array using the pipe centers and actions from the given variables. Features
	 * are generated before each move is taken. 
	 *
	 * @centers the centers of every pipe in order
	 * @actions the actions to take on each frame
	 * @return resulting features
	 */
	Feature[] calcFeatures(int[] centers, int[] actions) {

		// Initialize bird position to (0, 50)
		int birdY = defaultStartY;

		// Output variable
		Feature[] out = new Feature[actions.length];

		int distToPipe = 0;
		// Stores index of the next pipe
		int pipeIndex = 0;

		// Loop through every action
		for(int i = 0; i < out.length; i++) {

			// Create feature object for this cycle
			out[i] = new Feature(distToPipe, centers[pipeIndex] - birdY, actions[i]);

			// Update bird location
			distToPipe--;
			birdY += actions[i] == 1 ? config.u : -1;

			// Update next pipe if needed
			if(distToPipe < 0) {
				pipeIndex++;
				distToPipe += config.h;
			}
			
		}

		return out;
	}

	/**
	 * Prints a list of actions to the given writer.
	 *
	 * @param out the writer to print to
	 * @param actions the actions to write
	 */
	static void printActions(PrintWriter out, int[] actions) {
		for(int i : actions) {
			out.print(i);
			out.print(", ");
		}
		out.println();
	}

}

