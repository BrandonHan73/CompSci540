
#include <cfloat>
#include <cmath>
#include <fstream>
#include <iomanip>
#include <ios>
#include <sstream>
#include <string>
#include <vector>

#include <Eigen/Dense>

#include "pca.h"

void read_mnist_dataset(std::string file, Eigen::MatrixXd &mat, std::vector<int> &labels) {
	
	std::ifstream input(file);
	mat = Eigen::MatrixXd(10000, 784);
	labels = std::vector<int>(10000);

	std::string line, temp;
	std::stringstream str;

	for(int r = 0; r < 10000; ++r) {
		std::getline(input, line);
		str = std::stringstream(line);

		std::getline(str, temp, ',');
		labels[r] = std::stod(temp);

		for(int c = 0; c < 784; ++c) {
			std::getline(str, temp, ',');
			mat(r, c) = std::stod(temp);
		}
	}

	input.close();

}

void read_image_dataset(std::string file, Eigen::MatrixXd &mat) {
	
	std::ifstream input(file);
	mat = Eigen::MatrixXd(784, 100);

	std::string line, temp;
	std::stringstream str;

	for(int c = 0; c < 100; ++c) {
		std::getline(input, line);
		str = std::stringstream(line);

		std::getline(str, temp, ',');

		for(int r = 0; r < 784; ++r) {
			std::getline(str, temp, ',');
			mat(r, c) = std::stod(temp);
		}
	}

	input.close();

}

Eigen::VectorXd subtract_mean(Eigen::MatrixXd &data) {

	Eigen::VectorXd mean(data.cols());
	for(int r = 0; r < data.rows(); ++r) {
		mean += data.row(r);
	}
	mean /= data.rows();

	for(int r = 0; r < data.rows(); ++r) {
		data.row(r) -= mean;
	}

	return mean;
}

void subtract_mean(Eigen::MatrixXd &data, const Eigen::VectorXd &mean) {
	for(int c = 0; c < data.cols(); ++c) {
		data.col(c) -= mean;
	}
}

void add_mean(Eigen::MatrixXd &data, const Eigen::VectorXd &mean) {
	for(int c = 0; c < data.cols(); ++c) {
		data.col(c) += mean;
	}
}

void print_to_file(std::string file, int precision, const Eigen::MatrixXd &data) {
	std::ofstream out("output/" + file);

	out << std::fixed << std::setprecision(precision);

	for(int c = 0; c < data.cols(); ++c) {
		for(int r = 0; r < data.rows(); ++r) {
			out << data(r, c);
			if(r != data.rows() - 1) {
				out << ",";
			}
		}
		if(c != data.cols() - 1) {
			out << "\n";
		}
	}

	out.close();
}

void print_to_file(std::string file, int precision, const Eigen::MatrixXd &data, const std::vector<int> &index) {
	std::ofstream out("output/" + file);

	out << std::fixed << std::setprecision(precision);

	int c;
	for(int i = 0; i < index.size(); ++i) {
		c = index[i];
		for(int r = 0; r < data.rows(); ++r) {
			out << data(r, c);
			if(r != data.rows() - 1) {
				out << ",";
			}
		}
		if(i != index.size() - 1) {
			out << "\n";
		}
	}

	out.close();
}

Eigen::MatrixXd extract_k_eigenvectors(int k, const Eigen::MatrixXd &data) {
	int dimensions = data.rows();

	Eigen::MatrixXd ret(dimensions, k);
	for(int r = 0; r < dimensions; ++r) {
		for(int c = 0; c < k; ++c) {
			ret(r, c) = data(r, data.cols() - 1 - c);
		}
	}

	return ret;
}

Eigen::MatrixXd axis_vectors(int k, int dimensions) {
	Eigen::MatrixXd ret(dimensions, k);
	ret.fill(0);
	for(int i = 0; i < k && i < dimensions; ++i) {
		ret(i, i) = 1;
	}
	return ret;
}

Eigen::MatrixXd reconstruct(const Eigen::MatrixXd &projections, const Eigen::MatrixXd &axis) {
	int images = projections.cols();

	int dimensions = axis.rows();

	int axis_count = projections.rows();

	Eigen::MatrixXd ret(dimensions, images);
	Eigen::VectorXd image;
	for(int i = 0; i < images; ++i) {
		image = Eigen::VectorXd(dimensions);

		for(int a = 0; a < axis_count; ++a) {
			image += projections(a, i) * axis.col(a);
		}

		ret.col(i) = image;
	}

	return ret;

}

void normalize_columns(Eigen::MatrixXd &vectors) {
	for(int v = 0; v < vectors.cols(); ++v) {
		vectors.col(v).normalize();

		if(vectors.col(v).sum() < 0) {
			vectors.col(v) *= -1;
		}
	}
}

void round_values(Eigen::MatrixXd &data) {
	for(int r = 0; r < data.rows(); ++r) {
		for(int c = 0; c < data.cols(); ++c) {
			data(r, c) = std::round(data(r, c) * 10000) / (double) 10000;
		}
	}
}

std::vector<int> closest_datapoint(const Eigen::MatrixXd &images, const Eigen::MatrixXd &dataset) {
	std::vector<int> ret(images.cols());

	double best, temp;
	for(int i = 0; i < ret.size(); ++i) {

		best = DBL_MAX;
		for(int d = 0; d < dataset.cols(); ++d) {
			temp = (dataset.col(d) - images.col(i)).norm();
			if(best > temp) {
				ret[i] = d;
				best = temp;
			}
		}

	}

	return ret;
}

Eigen::MatrixXd extract_using_index(const Eigen::MatrixXd &data, const std::vector<int> &index) {
	Eigen::MatrixXd ret(data.rows(), index.size());

	for(int i = 0; i < index.size(); ++i) {
		ret.col(i) = data.col(index[i]);
	}

	return ret;
}

