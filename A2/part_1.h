#include <string>
#include <vector>

std::vector<std::vector<double>> read_data(std::string file);

std::vector<double> process_data(std::string);

void show_vectors(std::string, int, std::vector<std::vector<double>>*);

void show_vectors(std::string, std::vector<std::vector<int>>*);

std::vector<double> parametric_model(std::vector<double>);

std::vector<std::vector<double>> parametric_model(std::vector<std::vector<double>>);

void normalize(std::vector<std::vector<double>>*, int);

