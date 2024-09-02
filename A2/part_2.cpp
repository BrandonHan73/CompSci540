#include <algorithm>
#include <cfloat>
#include <cmath>
#include <fstream>
#include <iostream>
#include <ostream>
#include <utility>
#include <vector>

#include "part_2.h"

std::vector<std::vector<double>> k_means_clustering(int k, std::vector<std::vector<double>> &data) {

	std::vector<std::vector<double>> centers;
	for(int i = 0; i < k; ++i) {
		centers.push_back(data[i]);
	}

	std::vector<std::vector<double>> temp;
	std::vector<std::vector<int>> assign;
	while(true) {
		assign = cluster_points(data, centers);

		temp.clear();
		for(std::vector<int> &a : assign) {
			temp.push_back(find_center(a, data));
		}
		
		if(temp == centers) {
			break;
		}

		centers = temp;
	}

	return centers;

}

double calc_distortion(std::vector<std::vector<double>> &centers, std::vector<std::vector<double>> &data) {
	double dist = 0;

	for(std::vector<double> &p : data) {
		dist += std::pow(calc_dist(p, centers[find_closest_center(p, centers)]), 2);
	}

	return dist;

}

int find_closest_center(std::vector<double> &point, std::vector<std::vector<double>> &centers) {

	double dist = DBL_MAX;
	int center = 0;

	double temp;
	for(int i = 0; i < centers.size(); ++i) {

		temp = calc_dist(point, centers[i]);
		if(temp < dist) {
			dist = temp;
			center = i;
		}

	}
	return center;

}

std::vector<std::vector<int>> cluster_points(std::vector<std::vector<double>> &points, std::vector<std::vector<double>> &centers) {

	std::vector<std::vector<int>> assign(centers.size());

	for(int i = 0; i < points.size(); ++i) {
		assign[ find_closest_center(points[i], centers) ].push_back(i);
	}

	return assign;
}

std::vector<double> find_center(std::vector<int> &index, std::vector<std::vector<double>> &data) {
	std::vector<double> center;
	for(int i : index) {
		while(center.size() < data[i].size()) {
			center.push_back(0);
		}

		for(int d = 0; d < data[i].size(); ++d) {
			center[d] += data[i][d];
		}
	}
	for(double &d : center) {
		d /= index.size();
	}

	return center;
}

void complete_linkage(std::pair<int, int> loc, std::vector<std::vector<double>> &data) {

	int N = data.size();

	int fir = std::min(loc.first, loc.second);
	int sec = std::max(loc.first, loc.second);

	std::vector<double> last;
	data.erase(data.begin() + sec);
	data.erase(data.begin() + fir);

	for(int r = 0; r < N - 2; ++r) {
		last.push_back(std::max(data[r][loc.first], data[r][loc.second]));
		data[r].erase(data[r].begin() + sec);
		data[r].erase(data[r].begin() + fir);
		data[r].push_back(last[r]);
	}

	last.push_back(0);
	data.push_back(last);

}

void single_linkage(std::pair<int, int> loc, std::vector<std::vector<double>> &data) {

	int N = data.size();

	int fir = std::min(loc.first, loc.second);
	int sec = std::max(loc.first, loc.second);

	std::vector<double> last;
	data.erase(data.begin() + sec);
	data.erase(data.begin() + fir);

	for(int r = 0; r < N - 2; ++r) {
		last.push_back(std::min(data[r][loc.first], data[r][loc.second]));
		data[r].erase(data[r].begin() + sec);
		data[r].erase(data[r].begin() + fir);
		data[r].push_back(last[r]);
	}

	last.push_back(0);
	data.push_back(last);

}

std::vector<std::vector<int>> k_complete_linkage(int k, std::vector<std::vector<double>> &data) {

	int N = data.size();

	std::vector<std::vector<int>> groups;
	for(int i = 0; i < N; ++i) {
		groups.push_back({i});
	}

	std::vector<int> combine;
	std::pair<int, int> pair;
	while(data.size() > k) {

		pair = find_closest(data);

		combine = groups[pair.first];
		for(int i : groups[pair.second]) {
			combine.push_back(i);
		}

		groups.erase(groups.begin() + pair.second);
		groups.erase(groups.begin() + pair.first);
		groups.push_back(combine);

		complete_linkage(pair, data);

	}

	return groups;

}

std::vector<std::vector<int>> k_single_linkage(int k, std::vector<std::vector<double>> &data) {

	int N = data.size();

	std::vector<std::vector<int>> groups;
	for(int i = 0; i < N; ++i) {
		groups.push_back({i});
	}

	std::vector<int> combine;
	std::pair<int, int> pair;
	while(data.size() > k) {

		pair = find_closest(data);

		combine = groups[pair.first];
		for(int i : groups[pair.second]) {
			combine.push_back(i);
		}

		groups.erase(groups.begin() + pair.second);
		groups.erase(groups.begin() + pair.first);
		groups.push_back(combine);

		single_linkage(pair, data);

	}

	return groups;

}

void show_vectors(std::string file, std::vector<int> &data) {
	std::ofstream out(file);

	for(int &d : data) {
		out << d << ",";
	}
	out << std::endl;

	out.close();
}

std::vector<int> to_digits(std::vector<std::vector<int>>& data) {

	int max = 0;
	for(int i = 0; i < data.size(); ++i) {
		for(int d : data[i]) {
			max = std::max(max, d);
		}
	}

	std::vector<int> ret(max + 1);
	for(int i = 0; i < data.size(); ++i) {
		for(int d : data[i]) {
			ret[d] = i;
		}
	}

	return ret;

}

std::pair<int, int> find_closest(std::vector<std::vector<double>> &dist_matr) {
	std::pair<int, int> ret;
	double min = DBL_MAX;

	for(int i = 0; i < dist_matr.size(); ++i) {
		for(int j = 0; j < dist_matr[i].size(); ++j) {
			if(i == j) continue;

			if(min > dist_matr[i][j]) {
				min = dist_matr[i][j];
				ret = {std::min(i, j), std::max(i, j)};
			}
		}
	}

	return ret;

}

std::vector<std::vector<double>> calc_dist_matr(std::vector<std::vector<double>> &data) {
	std::vector<std::vector<double>> dist;

	std::vector<double> c;
	for(int col = 0; col < data.size(); ++col) {
		c.clear();
		for(std::vector<double> &d : data) {
			c.push_back(calc_dist(data[col], d));
		}
		dist.push_back(c);
	}

	return dist;
}

double calc_dist(std::vector<double> &a, std::vector<double> &b) {
	double dist = 0;

	for(int i = 0; i < a.size() && i < b.size(); ++i) {
		dist += std::pow(a[i] - b[i], 2);
	}

	return std::sqrt(dist);

}

