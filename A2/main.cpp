#include <cfloat>
#include <cmath>
#include <iostream>
#include <ostream>
#include <string>
#include <vector>

#include "part_1.h"
#include "part_2.h"

int main() {

	std::cout << std::endl;

	std::vector<std::vector<double>> two_series = read_data("data/less.csv");
	std::cout << "Two series read. Length = " << two_series.size() << std::endl;
	show_vectors("output/two_series.txt", 15, &two_series);

	std::cout << std::endl;

	std::vector<std::vector<double>> two_model = parametric_model(two_series);
	std::cout << "Two model calculated. Length = " << two_model.size() << std::endl;
	show_vectors("output/two_models.txt", 4, &two_model);

	std::cout << std::endl;

	std::vector<std::vector<double>> series = read_data("data/data.csv");
	std::cout << "Full series read. Length = " << series.size() << std::endl;
	show_vectors("output/full_data.txt", 4, &series);
	std::vector<std::vector<double>> model = parametric_model(series);
	std::cout << "Full model calculated. Length = " << model.size() << std::endl;
	normalize(&model, 5);
	std::cout << "Full model normalized" << std::endl;
	show_vectors("output/parametric_models.txt", 4, &model);

	std::cout << std::endl;

	int k = 6;

	std::cout << "Performing single linkage" << std::endl;
	std::vector<std::vector<double>> single_dist_matr = calc_dist_matr(model);
	std::cout << "Distance matrix calculated. Size = " << single_dist_matr.size() << std::endl;
	std::vector<std::vector<int>> single_linkage_groups = k_single_linkage(k, single_dist_matr);
	std::cout << "Single linkage groups calculated. Group count = " << single_linkage_groups.size() << std::endl;
	std::vector<int> single_linkage_out = to_digits(single_linkage_groups);
	std::cout << "Single linkage groups converted to digits. Length = " << single_linkage_out.size() << std::endl;
	show_vectors("output/single_linkage.txt", single_linkage_out);

	std::cout << std::endl;

	std::cout << "Performing complete linkage" << std::endl;
	std::vector<std::vector<double>> complete_dist_matr = calc_dist_matr(model);
	std::cout << "Distance matrix calculated. Size = " << complete_dist_matr.size() << std::endl;
	std::vector<std::vector<int>> complete_linkage_groups = k_complete_linkage(k, complete_dist_matr);
	std::cout << "Complete linkage groups calculated. Group count = " << complete_linkage_groups.size() << std::endl;
	std::vector<int> complete_linkage_out = to_digits(complete_linkage_groups);
	std::cout << "Complete linkage groups converted to digits. Length = " << complete_linkage_out.size() << std::endl;
	show_vectors("output/complete_linkage.txt", complete_linkage_out);

	std::cout << std::endl;

	std::cout << "Performing k-means clustering" << std::endl;
	std::vector<std::vector<double>> centers = k_means_clustering(k, model);
	std::cout << "Centers calculated. Length = " << centers.size() << std::endl;
	show_vectors("output/centers.txt", 4, &centers);
	std::vector<std::vector<int>> assignments = cluster_points(model, centers);
	std::vector<int> k_means_grouping = to_digits(assignments);
	std::cout << "Grouping converted to digits." << std::endl;
	double distortion = calc_distortion(centers, model);
	std::cout << "Distortion calculated. D = " << distortion << std::endl;
	show_vectors("output/k_means_groups.txt", k_means_grouping);

	return 0;
}
