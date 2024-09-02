
import java.io.PrintWriter;

public abstract class Policy {

	static final int valueCalculationRepetitions = 256;

	final int rows, cols;

	Policy(int r, int c) {
		rows = r;
		cols = c;
	}

	int evaluate(int r, int c) {
		double rand = Math.random();
		double[] dist = getDistribution(r, c);
		for(int i = 0; i < dist.length; i++) {
			rand -= dist[i];
			if(rand < 0) {
				return i;
			}
		}

		throw new RuntimeException("Did not generate a valid probability distribution.");
	}

	abstract double[] getDistribution(int r, int c);

	double[][] calcValueFunctionIteration(double beta, int[][] reward, double[][] prev) {
		double[][] out = copy(prev);
		double[] dist;

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				out[r][c] = 0;
				dist = getDistribution(r, c);

				out[r][c] += dist[0] * (reward[(r - 1 + rows) % rows][c] + beta * prev[(r - 1 + rows) % rows][c]);
				out[r][c] += dist[1] * (reward[(r + 1 + rows) % rows][c] + beta * prev[(r + 1 + rows) % rows][c]);
				out[r][c] += dist[2] * (reward[r][(c - 1 + cols) % cols] + beta * prev[r][(c - 1 + cols) % cols]);
				out[r][c] += dist[3] * (reward[r][(c + 1 + cols) % cols] + beta * prev[r][(c + 1 + cols) % cols]);
			}
		}

		return out;
	}

	double[][] calcValueFunction(double beta, int[][] reward) {
		double[][] out = copyToDouble(reward);

		for(int rep = 0; rep < valueCalculationRepetitions; rep++) {
			out = calcValueFunctionIteration(beta, reward, out);
		}

		return out;
	}

	void printActions(PrintWriter out) {
		for(int r = 0; r < rows - 1; r++) {
			for(int c = 0; c < cols; c++) {
				out.print(evaluate(r, c));
				out.print(", ");
			}
		}

		for(int c = 0; c < cols - 1; c++) {
			out.print(evaluate(rows - 1, c));
			out.print(", ");
		}
		out.println(evaluate(rows - 1, cols - 1));
	}

	int findHighestValue(double[][] values) {
		int r = argmax(values);
		int c = argmax(values[r]);

		return r * rows + c;
	}

	static void printValueFunction(PrintWriter out, double[][] value) {
		for(int r = 0; r < value.length - 1; r++) {
			for(double col : value[r]) {
				out.print(Math.round(col * 10000) / 10000.0);
				out.print(", ");
			}
		}

		for(int c = 0; c < value[value.length - 1].length - 1; c++) {
			out.print(Math.round(value[value.length - 1][c] * 10000) / 10000.0);
			out.print(", ");
		}
		out.println(Math.round(value[value.length - 1][value[value.length - 1].length - 1] * 10000) / 10000.0);
	}

	static double[][] copy(double[][] matrix) {
		double[][] out = new double[matrix.length][];
		for(int r = 0; r < out.length; r++) {
			out[r] = new double[matrix[r].length];
			for(int c = 0; c < out[r].length; c++) {
				out[r][c] = matrix[r][c];
			}
		}
		return out;
	}

	static double[][][] copy(double[][][] tensor) {
		double[][][] out = new double[tensor.length][][];
		for(int r = 0; r < out.length; r++) {
			out[r] = copy(tensor[r]);
		}
		return out;
	}

	static int[][] copy(int[][] matrix) {
		int[][] out = new int[matrix.length][];
		for(int r = 0; r < out.length; r++) {
			out[r] = new int[matrix[r].length];
			for(int c = 0; c < out[r].length; c++) {
				out[r][c] = matrix[r][c];
			}
		}
		return out;
	}

	static double[][] copyToDouble(int[][] matrix) {
		double[][] out = new double[matrix.length][];
		for(int r = 0; r < out.length; r++) {
			out[r] = new double[matrix[r].length];
			for(int c = 0; c < out[r].length; c++) {
				out[r][c] = matrix[r][c];
			}
		}
		return out;
	}

	static double max(double[] values) {
		return values[ argmax(values) ];
	}

	static int argmax(double[][] values) {
		double max = max(values[0]);
		int out = 0;

		double test;
		for(int i = 1; i < values.length; i++) {
			test = max(values[i]);
			if(test > max) {
				max = test;
				out = i;
			}
		}

		return out;
	}

	static int argmax(double[] values) {
		double best = values[0];
		int out = 0;

		for(int i = 1; i < values.length; i++) {
			if(values[i] > best) {
				best = values[i];
				out = i;
			}
		}

		return out;
	}

	static int argmax(double v1, double v2, double v3, double v4) {
		return argmax(new double[] { v1, v2, v3, v4 });
	}

}

