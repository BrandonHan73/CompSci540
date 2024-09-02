
#include <iostream>
#include <vector>

#include "pca.h"

#include <Eigen/Dense>

int main() {

	std::cout << std::endl;

	// Data loading
	std::cout << "Loading data" << std::endl;

	// Given value for k
	int k = 25;

	// Mnist dataset
	std::cout << "Mnist dataset" << std::endl;

	Eigen::MatrixXd mnist_images; // N rows, d columns
	std::vector<int> mnist_labels; // N items
	read_mnist_dataset("data/mnist_train.csv", mnist_images, mnist_labels);
	Eigen::VectorXd mean = subtract_mean(mnist_images);
	
	// Test images
	std::cout << "Test images" << std::endl;

	Eigen::MatrixXd images; // d rows, n columns
	read_image_dataset("data/test.txt", images);

	std::cout << std::endl;

	// Question #1
	std::cout << "Question #1" << std::endl;

	Eigen::MatrixXd axis = axis_vectors(k, 784); // d rows, k columns
	std::cout << "Generated axis" << std::endl;
	print_to_file("1_axis.txt", 4, axis);

	std::cout << std::endl;

	// Question #2
	std::cout << "Question #2" << std::endl;

	Eigen::MatrixXd axis_projections = axis.transpose() * (images / 255); // k rows, n columns
	std::cout << "Created projections" << std::endl;
	print_to_file("2_axis_projections.txt", 4, axis_projections);

	std::cout << std::endl;

	// Question #3
	std::cout << "Question #3" << std::endl;

	Eigen::MatrixXd axis_reconstructions = axis * axis_projections; // d rows, n columns
	std::cout << "Reconstructed images" << std::endl;
	print_to_file("3_axis_reconstructions.txt", 4, axis_reconstructions);

	std::cout << std::endl;

	// Question #4
	std::cout << "Question #4" << std::endl;
	
	Eigen::MatrixXd covariance = mnist_images.transpose() * mnist_images; // d rows, d columns
	std::cout << "Created covariance matrix" << std::endl;
	Eigen::SelfAdjointEigenSolver<Eigen::MatrixXd> solver(covariance);
	std::cout << "Calculated eigenvectors" << std::endl;
	Eigen::MatrixXd principle_components = extract_k_eigenvectors(k, solver.eigenvectors()); // d rows, k columns
	std::cout << "Extracted k=" << k << " eigenvectors" << std::endl;
	normalize_columns(principle_components);
	std::cout << "Normalized eigenvectors" << std::endl;
	print_to_file("4_principle_components.txt", 4, principle_components);

	std::cout << std::endl;

	// Question #5
	std::cout << "Question #5" << std::endl;
	
	Eigen::MatrixXd pc_projections = principle_components.transpose() * (images / 255); // k rows, n columns
	std::cout << "Created projections" << std::endl;
	print_to_file("5_pc_projections.txt", 4, pc_projections);

	std::cout << std::endl;

	// Question #6
	std::cout << "Question #6" << std::endl;

	Eigen::MatrixXd pc_reconstructions = principle_components * pc_projections; // d rows, n columns
	std::cout << "Reconstructing images" << std::endl;
	print_to_file("6_pc_reconstructions.txt", 4, pc_reconstructions);

	std::cout << std::endl;

	// Question #7
	std::cout << "Question #7" << std::endl;

	Eigen::MatrixXd mnist_projections = principle_components.transpose() * (mnist_images.transpose() / 255); // k rows, N columns
	std::cout << "Created projections for training set" << std::endl;
	std::vector<int> close_index = closest_datapoint(pc_projections, mnist_projections); // n items
	std::cout << "Performed 1NN" << std::endl;
	Eigen::MatrixXd close_projections = extract_using_index(mnist_projections, close_index); // k rows, n columns
	std::cout << "Extracted projections from training set" << std::endl;
	print_to_file("7_closest_datapoints.txt", 4, close_projections);

	std::cout << std::endl;

	// Question #8
	std::cout << "Question #8" << std::endl;

	print_to_file("8_closest_images.txt", 4, mnist_images.transpose(), close_index);

	std::cout << std::endl;

	// Question #9
	std::cout << "Question #9" << std::endl;

	Eigen::MatrixXd close_reconstructions = principle_components * close_projections; // d rows, n columns
	std::cout << "Reconstructed images from training set projections" << std::endl;
	print_to_file("9_closest_reconstructions.txt", 4, close_reconstructions);

	std::cout << std::endl;

}

