
public class GreedyPolicy extends Policy {

	int[][] actions;

	GreedyPolicy(int[][] rewards) {
		super(rewards.length, rewards[0].length);

		actions = new int[rows][cols];
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				actions[r][c] = argmax(
					rewards[(r - 1 + rows) % rows][c],
					rewards[(r + 1 + rows) % rows][c],
					rewards[r][(c - 1 + cols) % cols],
					rewards[r][(c + 1 + cols) % cols]
				);
			}
		}
	}

	double[] getDistribution(int r, int c) {
		double[] out = new double[4];
		out[ actions[r][c] ] = 1;
		return out;
	}

}

