CXX = g++
CXXFLAGS = -Wall -pedantic -g -std=c++11 -fopenmp
LIBS =
file = main

run: compile
	./${file} ${args}

compile: ${file}

clear:
	rm ${file} 2> /dev/null

recompile: clear compile

%: %.c
	$(CXX) $(CXXFLAGS) -o $@ $< $(LIBS)

compileMPI:
	mpiCC $(CXXFLAGS) -O3 -o ${file} ${file}.cpp $(LIBS) -lm
	
mpicpp:
	mpic++ $(CXXFLAGS) -o ${file} ${file}.cpp $(LIBS) -lm
