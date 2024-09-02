
public class RandomPolicy extends Policy {

	RandomPolicy(int r, int c) {
		super(r, c);
	}

	double[] getDistribution(int r, int c) {
		return new double[] { 0.25, 0.25, 0.25, 0.25 };
	}

}

