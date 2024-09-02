
run: compile/main.out
	./compile/main.out

init:
	mkdir -p data
	mkdir -p compile
	mkdir -p output
	mkdir -p lib
	wget -P data https://pjreddie.com/media/files/mnist_train.csv
	wget -P lib https://gitlab.com/libeigen/eigen/-/archive/3.4.0/eigen-3.4.0.tar.gz
	tar -C lib -xf lib/eigen-3.4.0.tar.gz
	mv test.txt data/

clean: 
	rm output/*
	rm compile/*

reset:
	mv data/test.txt .
	rm -rf data/ compile/ output/ lib/

compile/main.out: compile/main.o compile/pca.o
	g++ -I lib/eigen-3.4.0 -o compile/main.out -g compile/main.o compile/pca.o

compile/main.o: main.cpp
	g++ -I lib/eigen-3.4.0 -o compile/main.o -g -c main.cpp

compile/pca.o: pca.cpp
	g++ -I lib/eigen-3.4.0 -o compile/pca.o -g -c pca.cpp

