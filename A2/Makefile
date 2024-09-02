
main: compile/main.out
	./compile/main.out

init:
	mkdir compile

compile/main.out: compile/main.o compile/part_1.o compile/part_2.o
	g++ compile/main.o compile/part_1.o compile/part_2.o -g -o compile/main.out

compile/main.o: main.cpp
	g++ main.cpp -g -c -o compile/main.o

compile/part_1.o: part_1.cpp
	g++ part_1.cpp -g -c -o compile/part_1.o

compile/part_2.o: part_2.cpp
	g++ part_2.cpp -g -c -o compile/part_2.o

