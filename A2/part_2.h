#include <utility>
#include <string>
#include <vector>

double calc_distortion(std::vector<std::vector<double>>&, std::vector<std::vector<double>>&);

std::vector<std::vector<double>> k_means_clustering(int, std::vector<std::vector<double>>&);

int find_closest_center(std::vector<double>&, std::vector<std::vector<double>>&);

std::vector<std::vector<int>> cluster_points(std::vector<std::vector<double>>&, std::vector<std::vector<double>>&);

std::vector<double> find_center(std::vector<int>&, std::vector<std::vector<double>>&);

std::pair<int, int> find_closest(std::vector<std::vector<double>>&);

void complete_linkage(std::pair<int, int>, std::vector<std::vector<double>>&);

std::vector<std::vector<int>> k_complete_linkage(int, std::vector<std::vector<double>>&);

void single_linkage(std::pair<int, int>, std::vector<std::vector<double>>&);

std::vector<std::vector<int>> k_single_linkage(int, std::vector<std::vector<double>>&);

std::vector<int> to_digits(std::vector<std::vector<int>>&);

void show_vectors(std::string, std::vector<int>&);

double calc_dist(std::vector<double>&, std::vector<double>&);

std::vector<std::vector<double>> calc_dist_matr(std::vector<std::vector<double>>&);

