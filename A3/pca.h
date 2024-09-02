
#include <string>
#include <vector>

#include <Eigen/Dense>

void read_mnist_dataset(std::string, Eigen::MatrixXd&, std::vector<int>&);

void read_image_dataset(std::string, Eigen::MatrixXd&);

Eigen::VectorXd subtract_mean(Eigen::MatrixXd&);

void subtract_mean(Eigen::MatrixXd&, const Eigen::VectorXd&);

void add_mean(Eigen::MatrixXd&, const Eigen::VectorXd&);

void print_to_file(std::string, int, const Eigen::MatrixXd&);

void print_to_file(std::string, int, const Eigen::MatrixXd&, const std::vector<int>&);

Eigen::MatrixXd extract_k_eigenvectors(int, const Eigen::MatrixXd&);

Eigen::MatrixXd axis_vectors(int, int);

Eigen::MatrixXd reconstruct(const Eigen::MatrixXd&, const Eigen::MatrixXd&);

void normalize_columns(Eigen::MatrixXd&);

Eigen::MatrixXd project(const Eigen::MatrixXd&, const Eigen::MatrixXd&);

void round_values(Eigen::MatrixXd&);

std::vector<int> closest_datapoint(const Eigen::MatrixXd&, const Eigen::MatrixXd&);

Eigen::MatrixXd extract_using_index(const Eigen::MatrixXd&, const std::vector<int>&);

