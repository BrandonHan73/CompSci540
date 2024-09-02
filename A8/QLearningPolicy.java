
import java.io.PrintWriter;

public class QLearningPolicy extends Policy {

	static final int qFunctionCalculationRepetitions = valueCalculationRepetitions;

	double[][][] Q;

	QLearningPolicy(int r, int c) {
		super(r, c);

		Q = new double[rows][cols][4];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				for(int d = 0; d < 4; d++) {
					Q[i][j][d] = 2 * Math.random() - 1;
				}
			}
		}
	}

	double[] getDistribution(int r, int c) {
		double[] out = new double[4];

		int action = argmax(Q[r][c]);

		out[action] = 1;
		return out;
	}

	void calcQFunction(double beta, int[][] reward) {
		for(int rep = 0; rep < qFunctionCalculationRepetitions; rep++) {
			calcQFunctionIteration(beta, reward);
		}
	}

	void calcQFunctionIteration(double beta, int[][] reward) {
		double[][][] save = copy(Q);
		int destRow = 0, destCol = 0;
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				for(int d = 0; d < 4; d++) {
					switch(d) {
						case 0:
						destRow = (r - 1 + rows) % rows;
						destCol = c;
						break;
						case 1:
						destRow = (r + 1 + rows) % rows;
						destCol = c;
						break;
						case 2:
						destRow = r;
						destCol = (c - 1 + cols) % cols;
						break;
						case 3:
						destRow = r;
						destCol = (c + 1 + cols) % cols;
						break;
					}

					Q[r][c][d] = reward[destRow][destCol] + beta * max(save[destRow][destCol]);
				}
			}
		}
	}

	void printQFunction(PrintWriter out) {
		for(double[][] row : Q) {
			for(double[] col : row) {
				for(int d = 0; d < col.length - 1; d++) {
					out.print(Math.round(col[d] * 10000) / 10000.0);
					out.print(", ");
				}
				out.println(Math.round(col[col.length - 1] * 10000) / 10000.0);
			}
		}
	}

}

