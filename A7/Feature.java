
import java.io.PrintWriter;

/**
 * Represents a pair of features. Uses integer for feature type. Capable (and recommended) to 
 * record a label. 
 *
 * @author Brandon Han
 */
class Feature {

	// Features
	int horiz, vert;

	// Label
	int label;

	/**
	 * Basic constructor. Does not assign a label. 
	 *
	 * @param h value to assign to horiz feature
	 * @param v value to assign to vert feature
	 */
	Feature(int h, int v) {
		this(h, v, -1);
	}

	/**
	 * Basic constructor. Assigns a label.
	 *
	 * @param h value to assign to horiz feature
	 * @param v value to assign to vert feature
	 * @param l value to assign to label
	 */
	Feature(int h, int v, int l) {
		horiz = h;
		vert = v;
		label = l;
	}

	@Override
	public String toString() {
		return horiz + ", " + vert;
	}

	/**
	 * Prints the given feature array to the given writer.
	 *
	 * @param out the writer to output to.
	 * @param feature the features to write.
	 */
	static void print(PrintWriter out, Feature[] features) {
		for(Feature f : features) {
			out.println(f);
		}
	}

	/**
	 * Prints the evaluation for the given feature array to the given writer.
	 *
	 * @param out the writer to output to.
	 * @param feature the features to evaluate.
	 */
	static void printEvaluation(PrintWriter out, Feature[] features, TwoLayerNetwork net) {
		for(Feature f : features) {
			out.print(Math.round(net.feedForward(f)));
			out.print(",");
		}
		out.println();
	}
}

