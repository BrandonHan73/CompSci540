
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Jama.Matrix;

/**
 * Main class. Generates an output file for A7 of CS540 Summer 2024 based off of the input 
 * parameters in parameter.txt.
 *
 * @author Brandon Han
 */
public class Assignment7 {

	public static void main(String[] args) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
		out.println("##a: 7");

		AssignmentConfig config = new AssignmentConfig("parameters.txt");
		out.println("##id: " + config.id);

		Part1(out, config);
		Part2(out, config);

		out.println("##10: true");
		out.println("##11: :)");

		out.close();

	}

	/**
	 * Part 1 of the assignment. Runs all code relevant to questions labeled "Part 1"
	 */
	static void Part1(PrintWriter out, AssignmentConfig config) {
		Simulator sim = new Simulator(config);

		// Question 1
		out.println("##1:");

		Feature[] features = sim.calcFeatures();
		Feature.print(out, features);

		// Question 2
		out.println("##2:");

		TwoLayerNetwork net = new TwoLayerNetwork(2, config.n, 1);
		net.backpropogation(features, 1024);

		net.printHidden(out);

		// Question 3
		out.println("##3:");

		net.printOutput(out);

		// Question 4
		out.println("##4:");

		int[] actions = sim.calcActions(config.centers, net);
		Simulator.printActions(out, actions);

		// Question 5
		out.println("##5:");

		out.println(sim.calcFitness(actions));

	}

	/**
	 * Part 2 of the assignment. Runs all code relevant to questions labeled "Part 2"
	 */
	static void Part2(PrintWriter out, AssignmentConfig config) {
		Simulator sim = new Simulator(config);

		// Question 6
		out.println("##6:");

		TwoLayerNetwork net = TwoLayerNetwork.trainNetwork(config.n, sim, 8, 128);

		net.printHidden(out);

		// Question 7
		out.println("##7:");

		net.printOutput(out);

		// Question 8
		out.println("##8:");

		int[] actions = sim.calcActions(config.centers, net);
		Simulator.printActions(out, actions);

		// Question 9
		out.println("##9:");

		out.println(sim.calcFitness(actions));

	}

}

