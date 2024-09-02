
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Main class. Generates an output file for A8 of CS540 Summer 2024 based off of the input 
 * parameters in parameter.txt.
 *
 * @author Brandon Han
 */
public class Assignment8 {

	public static void main(String[] args) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
		out.println("##a: 8");

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

		// Question 1
		out.println("##1:");

		Policy rPol = new RandomPolicy(config.n, config.n);
		double[][] rValue = rPol.calcValueFunction(config.beta, config.rewards);
		Policy.printValueFunction(out, rValue);

		// Question 2
		out.println("##2:");

		Policy gPol = new GreedyPolicy(config.rewards);
		gPol.printActions(out);

		// Question 3
		out.println("##3:");

		double[][] gValue = gPol.calcValueFunction(config.beta, config.rewards);
		Policy.printValueFunction(out, gValue);

	}

	/**
	 * Part 2 of the assignment. Runs all code relevant to questions labeled "Part 2"
	 */
	static void Part2(PrintWriter out, AssignmentConfig config) {

		// Question 4
		out.println("##4:");

		QLearningPolicy qPol = new QLearningPolicy(config.n, config.n);
		qPol.printQFunction(out);

		// Question 5
		out.println("##5:");

		qPol.calcQFunctionIteration(config.beta, config.rewards);
		qPol.printQFunction(out);

		// Question 6
		out.println("##6:");

		qPol.calcQFunction(config.beta, config.rewards);
		qPol.printQFunction(out);

		// Question 7
		out.println("##7:");

		qPol.printActions(out);

		// Question 8
		out.println("##8:");

		double[][] qValue = qPol.calcValueFunction(config.beta, config.rewards);
		Policy.printValueFunction(out, qValue);

		// Question 9
		out.println("##9:");

		int target = qPol.findHighestValue(qValue);
		out.println(target);

	}

}

