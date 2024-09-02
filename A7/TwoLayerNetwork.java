
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import Jama.Matrix;

/**
 * Represents a single neural network with one hidden layer. Capable of mutation and
 * backpropogation. Uses sigmoid as the activation function.
 *
 * @author Brandon Han
 */
public class TwoLayerNetwork {

	// Base backpropogation learning rate
	final static double baseAlpha = 0.4;

	// Weights and biases
	Matrix hiddenWeights, outputWeights;
	Matrix hiddenBiases, outputBiases;

	// Stores fitness for training
	double fitness;

	/**
	 * Placeholder contructor. Should not be used on any network.
	 */
	TwoLayerNetwork() {
	}

	/**
	 * Basic constructor. Initializes weights and biases to a value between -1 and 1. 
	 *
	 * @param input the number of input neurons
	 * @param hidden the number of neurons in the hidden layer
	 * @param output the number of output neurons
	 */
	TwoLayerNetwork(int input, int hidden, int output) {
		hiddenWeights = Matrix.random(input, hidden).times(2).minus(ones(input, hidden));
		outputWeights = Matrix.random(hidden, output).times(2).minus(ones(hidden, output));

		hiddenBiases = Matrix.random(1, hidden).times(2).minus(ones(1, hidden));
		outputBiases = Matrix.random(1, output).times(2).minus(ones(1, output));
	}

	// -------- Backpropogation / Gradient descent methods --------

	/**
	 * Evaluates a singular Feature. Does not consider the label stored in the given Feature.
	 *
	 * @param input the Feature to evaluate
	 * @return the resulting analysis
	 */
	double feedForward(Feature input) {
		return feedForward(new double[] {input.horiz, input.vert})[0];
	}

	/**
	 * Evaluates the given double array. 
	 *
	 * @param input the data to evaluate
	 * @return a double array with representing the output of the network
	 */
	double[] feedForward(double[] input) {
		Matrix inputMat = new Matrix(new double[][] {input});

		Matrix hiddenActivation = sigmoid(inputMat.times(hiddenWeights).plus(hiddenBiases));
		Matrix outputActivation = sigmoid(hiddenActivation.times(outputWeights).plus(outputBiases));

		return outputActivation.getRowPackedCopy();
	}

	/**
	 * Performs stochastic gradient descent using square loss to calculate cost. Learning rate 
	 * decreases with successive generations. Assumes labels are either 1 or 0 and uses the values
	 * present in the Feature field. Trains for the given number of generations.
	 *
	 * @param input the features and labels to train on
	 * @param generations the number of cycles to train for
	 */
	void backpropogation(Feature[] input, int generations) {
		Matrix inputMat, hiddenActivation, outputActivation;
		Matrix dCdwHidden, dCdwOutput, dCdbHidden, dCdbOutput;
		Matrix y;
		double alpha;
		Feature[] data;

		// Iterate through every generation
		for(int gen = 1; gen <= generations; gen++) {

			data = shuffle(input);

			// Set decreasing learning rate based on generation
			alpha = baseAlpha / (Math.log(gen) + 1);

			// Stochastic gradient descent
			for(Feature in : data) {

				// Find activations
				inputMat = new Matrix(new double[] {in.horiz, in.vert}, 1);
				hiddenActivation = sigmoid(inputMat.times(hiddenWeights).plus(hiddenBiases));
				outputActivation = sigmoid(hiddenActivation.times(outputWeights).plus(outputBiases));

				// Create correct label
				y = new Matrix(1, 1, in.label);

				// Calculate derivative using square loss

				// For output layer, dC/dz = dC/db = (a - y) * a(1 - a)
				// 1 row, output columns
				dCdbOutput = outputActivation.minus(y).arrayTimes(outputActivation).arrayTimes(ones(outputActivation).minus(outputActivation));
				dCdwOutput = hiddenActivation.transpose().times(dCdbOutput);

				// For hidden layer, dC/dz = dC/db = dC/dz * w * a(1 - a)
				// 1 row, hidden columns
				dCdbHidden = dCdbOutput.times(outputWeights.transpose()).arrayTimes(hiddenActivation).arrayTimes(ones(hiddenActivation).minus(hiddenActivation));
				dCdwHidden = inputMat.transpose().times(dCdbHidden);

				// Gradient descent equation

				// w <- w - alpha * dC/dw
				hiddenWeights.minusEquals(dCdwHidden.times(alpha));
				outputWeights.minusEquals(dCdwOutput.times(alpha));

				// b <- b - alpha * dC/db
				hiddenBiases.minusEquals(dCdbHidden.times(alpha));
				outputBiases.minusEquals(dCdbOutput.times(alpha));
			}

			// System.out.println(measureAccuracy(input));

		}
	}

	/**
	 * Evalutes the accuracy of this network using the given data as the test set.
	 *
	 * @param data the test set
	 * @return the accuracy of this network on the range from 0 to 1
	 */
	double measureAccuracy(Feature[] data) {
		int count = 0;
		for(Feature d : data) {
			if(Math.round(feedForward(d)) == d.label) {
				count++;
			}
		}
		return (double) count / data.length;
	}

	// -------- Genetic algorithm methods --------

	/**
	 * Uses the genetic algorithm to train a TwoLayerNetwork. Uses the given simulator to run the
	 * environment.
	 *
	 * @param hidden the number of neurons in the hidden layer
	 * @param sim the simulator to use
	 * @param popSize the population size to use for each cycle
	 * @param generations the number of cycles to iterate over
	 * @return a trained neural network
	 */
	static TwoLayerNetwork trainNetwork(int hidden, Simulator sim, int popSize, int generations) {
		TwoLayerNetwork[] population = new TwoLayerNetwork[popSize];

		for(int i = 0; i < population.length; i++) {
			population[i] = new TwoLayerNetwork(2, hidden, 1);
		}

		double rate;
		for(int gen = 1; gen <= generations; gen++) {
			for(TwoLayerNetwork net : population) {
				net.fitness = sim.calcFitness(net);
			}

			rate = baseAlpha / Math.sqrt(gen);
			population = repopulate(population, rate);
		}

		Arrays.sort(population, Comparator.comparingDouble(net -> net.fitness));
		return population[0];
	}

	/**
	 * Repopulates the given network. Assumes the fitness field stores the correct fitness value. 
	 * Resets the fitness field to zero for every remaining network. Fitness value for discarded
	 * networks are updated with undefined behavior.
	 *
	 * @param population the population to repopulate
	 * @param rate the mutation rate
	 * @return the repopulated group of networks
	 */
	static TwoLayerNetwork[] repopulate(TwoLayerNetwork[] population, double rate) {
		double maxFitness = 0;
		for(TwoLayerNetwork net : population) {
			maxFitness = Math.max(net.fitness, maxFitness);
		}
		double totalFitness = 0;
		for(TwoLayerNetwork net : population) {
			net.fitness = Math.exp(net.fitness - maxFitness);
			totalFitness += net.fitness;
		}

		TwoLayerNetwork[] out = new TwoLayerNetwork[population.length];
		double select;
		TwoLayerNetwork net = new TwoLayerNetwork();
		for(int i = 0; i < out.length; i++) {
			select = totalFitness * Math.random();
			for(TwoLayerNetwork n : population) {
				select -= n.fitness;
				if(select <= 0) {
					net = n;
					break;
				}
			}
			out[i] = net.mutate(rate);
		}
		return out;
	}

	/**
	 * Returns a mutated copy of this network. Uses the normal distribution with the given standard deviation. 
	 *
	 * @param std standard deviation
	 * @return a mutated network
	 */
	TwoLayerNetwork mutate(double std) {
		TwoLayerNetwork out = new TwoLayerNetwork(0, 0, 0);

		out.hiddenWeights = (Matrix) hiddenWeights.clone();
		out.outputWeights = (Matrix) outputWeights.clone();
		out.hiddenBiases = (Matrix) hiddenBiases.clone();
		out.outputBiases = (Matrix) outputBiases.clone();

		Random rng = new Random();

		for(int r = 0; r < out.hiddenWeights.getRowDimension(); r++) {
			for(int c = 0; c < out.hiddenWeights.getColumnDimension(); c++) {
				out.hiddenWeights.set(r, c, rng.nextGaussian(out.hiddenWeights.get(r, c), std));
			}
		}
		for(int r = 0; r < out.outputWeights.getRowDimension(); r++) {
			for(int c = 0; c < out.outputWeights.getColumnDimension(); c++) {
				out.outputWeights.set(r, c, rng.nextGaussian(out.outputWeights.get(r, c), std));
			}
		}
		for(int r = 0; r < out.hiddenBiases.getRowDimension(); r++) {
			for(int c = 0; c < out.hiddenBiases.getColumnDimension(); c++) {
				out.hiddenBiases.set(r, c, rng.nextGaussian(out.hiddenBiases.get(r, c), std));
			}
		}
		for(int r = 0; r < out.outputBiases.getRowDimension(); r++) {
			for(int c = 0; c < out.outputBiases.getColumnDimension(); c++) {
				out.outputBiases.set(r, c, rng.nextGaussian(out.outputBiases.get(r, c), std));
			}
		}

		return out;
	}

	// -------- Data management methods --------

	/**
	 * Provides a shuffled copy of the given data.
	 *
	 * @param data the data to shuffle
	 * @return a shuffled copy of the given data
	 */
	Feature[] shuffle(Feature[] data) {
		Feature[] out = new Feature[data.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = data[i];
		}

		Feature swap;
		int swapIndex;
		for(int i = data.length - 1; i > 0; i--) {
			swapIndex = (int) (Math.random() * i);
			swap = out[swapIndex];
			out[swapIndex] = out[i];
			out[i] = swap;
		}

		return out;
	}

	/**
	 * Truncates and shuffles the given data. Output will have an equal number of Features 
	 * corresponding to each label. Assumes all labels are either 0 or 1.
	 *
	 * @param data a list of features with labels 0 or 1
	 * @return a subset of the given data with an equal amount of each label
	 */
	Feature[] filter(Feature[] data) {
		data = shuffle(data);

		int oneCount = 0, zeroCount = 0;
		for(Feature f : data) {
			if(f.label == 1) {
				oneCount++;
			} else if(f.label == 0) {
				zeroCount++;
			}
		}

		oneCount = Math.min(oneCount, zeroCount);
		zeroCount = Math.min(oneCount, zeroCount);

		Feature[] out = new Feature[oneCount + zeroCount];
		int fill = 0, follow = 0;
		for(Feature f : data) {
			if(f.label == 0 && zeroCount > 0) {
				out[fill++] = f;
				zeroCount--;
			} else if(f.label == 1 && oneCount > 0) {
				out[fill++] = f;
				oneCount--;
			}
		}

		return shuffle(out);
	}

	/**
	 * Creates a Matrix of the same size as the given array. All values in the output array will be 1.
	 *
	 * @param o the Matrix to size to
	 * @return a Matrix of ones
	 */
	Matrix ones(Matrix o) {
		return ones(o.getRowDimension(), o.getColumnDimension());
	}

	/**
	 * Creates a Matrix with the given dimensions. All values in the output array will be 1.
	 *
	 * @param rows the desired number of rows
	 * @param cols the desired number of columns
	 * @return a Matrix of ones
	 */
	Matrix ones(int rows, int cols) {
		return new Matrix(rows, cols, 1);
	}

	/** 
	 * Performs a sigmoid operation on a singular number.
	 *
	 * @param in the input value
	 * @return the sigmoid of the given value
	 */
	double sigmoid(double in) {
		return 1 / (1 + Math.exp(-in));
	}

	/** 
	 * Performs a sigmoid operation on a Matrix.
	 *
	 * @param in the input Matrix
	 * @return a new Matrix with element-wise sigmoid on the given Matrix
	 */
	Matrix sigmoid(Matrix in) {
		Matrix out = new Matrix(in.getRowDimension(), in.getColumnDimension());
		for(int r = 0; r < out.getRowDimension(); r++) {
			for(int c = 0; c < out.getColumnDimension(); c++) {
				out.set(r, c, sigmoid(in.get(r, c)));
			}
		}
		return out;
	}

	// -------- Print methods --------

	/**
	 * Prints the hidden layer weights and biases to the given writer. Weights corresponding to the
	 * same input node are printed on the same line. All biases are printed on the last line. 
	 *
	 * @param out the writer to output to
	 */
	void printHidden(PrintWriter out) {
		for(int r = 0; r < hiddenWeights.getRowDimension(); r++) {
			for(int c = 0; c < hiddenWeights.getColumnDimension(); c++) {
				out.print(hiddenWeights.get(r, c));
				out.print(", ");
			}
			out.println();
		}
		for(int i = 0; i < hiddenBiases.getColumnDimension(); i++) {
			out.print(hiddenBiases.get(0, i));
			out.print(", ");
		}
		out.println();
	}

	/**
	 * Prints the output layer weights and biases to the given writer. Weights corresponding to the
	 * same output node are printed on the same line. Biases are printed at the end of the line 
	 * pertaining to the corresponding output node.
	 *
	 * @param out the writer to output to
	 */
	void printOutput(PrintWriter out) {
		for(int i = 0; i < outputWeights.getRowDimension(); i++) {
			out.print(outputWeights.get(i, 0));
			out.print(", ");
		}
		out.println(outputBiases.get(0, 0));
	}

}
