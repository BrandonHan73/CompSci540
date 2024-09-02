#include <algorithm>
#include <cfloat>
#include <cmath>
#include <exception>
#include <iomanip>
#include <iostream>
#include <fstream>
#include <ostream>
#include <sstream>
#include <string>
#include <vector>

#include "part_1.h"

void normalize(std::vector<std::vector<double>>* data, int cols) {

	double min, max;
	for(int c = 0; c < cols; c++) {
		
		min = DBL_MAX;
		max = DBL_MIN;
		for(std::vector<double> &d : *data) {
			min = std::min(min, d[c]);
			max = std::max(max, d[c]);
		}

		if(min == max) {
			--min;
			++max;
		}
		for(std::vector<double> &d : *data) {
			double save = d[c];
			d[c] = (d[c] - min) / (max - min);

			if(std::isnan(d[c])) {
				std::cout << "NaN detected. Min = " << min << ", max = " << max << ", data = " << save << std::endl;
			}
		}

	}

}

std::vector<std::vector<double>> parametric_model(std::vector<std::vector<double>> data) {

	std::vector<std::vector<double>> ret;
	for(std::vector<double> &s : data) {
		ret.push_back(parametric_model(s));
	}
	return ret;

}

std::vector<std::vector<double>> read_data(std::string file) {

	std::ifstream data(file);

	std::string line;

	std::vector<std::vector<double>> series;
	std::vector<double> temp;
	while(std::getline(data, line)) {
		temp = process_data(line);
		if(temp.size() >= 63) {
			series.push_back(temp);
		}
	}

	data.close();

	return series;

}

std::vector<double> parametric_model(std::vector<double> data) {
	std::vector<double> ret;

	const int T = data.size();

	// Mean
	ret.push_back(0);
	for(double d : data) {
		ret[0] += d;
	}
	ret[0] /= T;

	// Standard deviation
	ret.push_back(0);
	for(double d : data) {
		ret[1] += std::pow(d - ret[0], 2);
	}
	ret[1] = std::sqrt( ret[1] / (T - 1) );

	// Median to be done at the end
	ret.push_back(0);

	// Linear trend coefficient
	ret.push_back(0);
	double t = (T + 1) / (double) 2;
	double denom = 0;
	for(int i = 0; i < T; ++i) {
		ret[3] += (data[i] - ret[0]) * (i + 1 - t);
		denom += std::pow(i + 1 - t, 2);
	}
	ret[3] /= denom;
	if(std::isnan(ret[3])) {
		std::cout << "NaN detected. Denom = " << denom << ", t = " << t << ", T = " << T << std::endl;
	}

	// Auto-correlation of the data
	ret.push_back(0);
	for(int i = 1; i < T; ++i) {
		ret[4] += (data[i] - ret[0]) * (data[i - 1] - ret[0]);
	}
	ret[4] /= (T - 1) * std::pow(ret[1], 2);

	// Median
	std::nth_element(data.begin(), data.begin() + T / 2, data.end());
	std::nth_element(data.begin(), data.begin() + (T - 1) / 2, data.end());
	ret[2] = ( data[(T - 1) / 2] + data[T / 2] ) / 2;

	return ret;
}

void show_vectors(std::string file, std::vector<std::vector<int>>* data) {
	std::ofstream out(file);

	for(std::vector<int> &vec : *data) {

		for(int &d : vec) {
			out << d << ",";
		}
		out << std::endl;

	}

	out.close();
}

void show_vectors(std::string file, int precision, std::vector<std::vector<double>>* data) {
	std::ofstream out(file);
	out << std::setprecision(precision);

	for(std::vector<double> &vec : *data) {

		for(double &d : vec) {
			out << d << ",";
		}
		out << std::endl;

	}

	out.close();
}

std::vector<double> process_data(std::string dat) {

	std::stringstream stream(dat);
	std::vector<double> output;

	std::string temp;
	double val;
	while(std::getline(stream, temp, ',')) {
		try {

			temp = temp.substr(temp.find("\"") + 1);
			temp = temp.substr(0, temp.find("\""));

			val = std::stod(temp);

			if(!std::isnan(val) && !std::isinf(val)) {
				output.push_back(std::stod(temp));
			}

		} catch(std::exception) {}
	}

	return output;

}

