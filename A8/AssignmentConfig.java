
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
	int n;
	int[][] rewards;
	double beta;

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
		n = readInt("n", in.readLine());
		rewards = readIntMatrix("rewards", in.readLine());
		beta = readDouble("beta", in.readLine());

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
	 * Reads an double from a given String. Checks whether the variable name matches the expected
	 * value.
	 *
	 * @param title the variable name to expect
	 * @param data the data to read
	 * @return the extracted double
	 * @throws RuntimeException when the variable name does not match the expected name
	 */
	double readDouble(String title, String data) {
		String[] input = data.split("=");

		if(input[0].trim().equals(title)) {
			return Double.parseDouble(input[1].trim());
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
	 * expected value. Expected format is as follows. <br>
	 * list_name = item1, item2, item3
	 * Commas separate each item. No comma should be placed at the end of the string.
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

	/**
	 * Reads a matrix of integers from a given String. Checks whether the variable name matches the 
	 * expected value. Expected format is as follows. <br>
	 * matrix_name = item1, item2, item3; item4, item5, item6; item7, item8, item9 <br>
	 * Commas separate columns and semicolons separate rows. No comma or semicolon should be placed
	 * at the end of the data line. 
	 *
	 * @param title the variable name to expect
	 * @param data the data to read
	 * @return the extracted integer matrix
	 * @throws RuntimeException when the variable name does not match the expected name
	 */
	int[][] readIntMatrix(String title, String data) {
		String[] input = data.split("=");
		int[][] out;

		String[] line;
		if(input[0].trim().equals(title)) {
			input = input[1].split(";");
			out = new int[input.length][];

			for(int i = 0; i < out.length; i++) {
				line = input[i].split(",");

				out[i] = new int[line.length];
				for(int j = 0; j < line.length; j++) {
					out[i][j] = Integer.parseInt(line[j].trim());
				}
			}

			return out;
		} else {
			throw new RuntimeException("Failed to parse data");
		}
	}

}

