
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Configuration class for the assignment. Holds variables that are given by problem statements.
 *
 * @author Brandon Han
 */
class AssignmentConfig {

	// Assignment-wide variables
	String id;
	int h, g, d, u, n;

	// Part 1 variables
	int[] centers, actions;

	/**
	 * Constructor for configuration class. Data file lines should be structured as follows. <br>
	 * variable = data <br>
	 * list_variable = item_1, item_2, item_3, ..., item_n <br>
	 *
	 * @param filename the name of the file to read from
	 */
	AssignmentConfig(String filename) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));

		id = readString("id", in.readLine());
		h = readInt("h", in.readLine());
		g = readInt("g", in.readLine());
		d = readInt("d", in.readLine());
		u = readInt("u", in.readLine());
		centers = readIntArray("centers", in.readLine());
		actions = readIntArray("actions", in.readLine());
		n = readInt("n", in.readLine());

		in.close();
	}

	/**
	 * Reads an integer from a given String. Checks whether the variable name matches the expected
	 * value.
	 *
	 * @param title the variable name to expect
	 * @param data the data to read
	 * @return the extracted integer
	 * @throws RuntimeException when the variable name does not match the expected name
	 */
	int readInt(String title, String data) {
		String[] input = data.split("=");

		if(input[0].trim().equals(title)) {
			return Integer.parseInt(input[1].trim());
		} else {
			throw new RuntimeException("Failed to parse data");
		}
	}

	/**
	 * Reads a String from a given String. Checks whether the variable name matches the expected
	 * value.
	 *
	 * @param title the variable name to expect
	 * @param data the data to read
	 * @return the extracted String
	 * @throws RuntimeException when the variable name does not match the expected name
	 */
	String readString(String title, String data) {
		String[] input = data.split("=");

		if(input[0].trim().equals(title)) {
			return input[1].trim();
		} else {
			throw new RuntimeException("Failed to parse data");
		}
	}

	/**
	 * Reads a list of integers from a given String. Checks whether the variable name matches the 
	 * expected value.
	 *
	 * @param title the variable name to expect
	 * @param data the data to read
	 * @return the extracted integer array
	 * @throws RuntimeException when the variable name does not match the expected name
	 */
	int[] readIntArray(String title, String data) {
		String[] input = data.split("=");
		int[] out;

		if(input[0].trim().equals(title)) {
			input = input[1].split(",");
			out = new int[input.length];

			for(int i = 0; i < out.length; i++) {
				out[i] = Integer.parseInt(input[i].trim());
			}

			return out;
		} else {
			throw new RuntimeException("Failed to parse data");
		}
	}

}

