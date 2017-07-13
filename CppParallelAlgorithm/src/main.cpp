#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <deque>
#include <stack>
#include <unistd.h>
#include <omp.h>
#include <mpi.h>
using namespace std;

#define YES '1'
#define NO '0'

static class Tags {
  public:
	static const int work = 1;
	static const int finished = 2;
	static const int done = 3;
} TAG;

/**
  * Graph representation
  */
class Graph {
  private:
	vector<string> incidence;
  public:
	Graph(string in) {
		ifstream f(in.c_str(), ios::in|ofstream::binary);
		string line;

		// cannot open file
		if (f.fail() || !f.good()) 
			throw runtime_error("Cant read from input file.");

		if (getline(f,line)) {
			while (getline(f,line)) {
				incidence.push_back(line);
			}
		}
		f.close();
	}

	bool hasEdge(int a, int b) const {
		if (a > getSize() || b > getSize()) return false;
		return incidence[a][b] == YES;
	}

	int getSize() const {
		return incidence.size();
	}
};

/**
  * Representation of a configuration or its part
  */
class Problem {
  private:
	size_t size; 	// number of nodes
	int aLeft; 		// number of nodes to select (from undecided nodes)
	string conf; 	// "0" and "1" code of decided nodes
  public:
	Problem(size_t size, int aLeft) : 
		size(size),
		aLeft(aLeft),
		conf("")
	{}

	Problem(size_t size, int aLeft, string conf) : 
		size(size),
		aLeft(aLeft),
		conf(conf)
	{}

	Problem(size_t s, int a, bool* in, int n) {
		size = s;
		aLeft = a;
		conf = "";

		// when a slave node recieves a part of the problem, parse and count
		for (int i = 0; i < n; i++) {
			conf += (in[i]) ? YES : NO;
			if (in[i]) aLeft--;
		}

		delete in;
	}

	// returns extended selection of nodes
	Problem decideNext(char x) const {
		return 
			(x == YES) ? 
				Problem(size, aLeft-1, conf + x) : 
				Problem(size, aLeft, conf + x);
	}

	// could this selection be possibly valid
	bool isGenerating() const {
		return 
			aLeft >= 0 && 
			(size_t)aLeft <= (size - conf.size());
	}

	// is this a valid selection
	bool isValid() const {
		return 
			aLeft == 0 && 
			conf.size() == size;
	}

	int getSize() const {
		return conf.size();
	}

	int getMaxSize() const {
		return size;
	}

	string getConf() const {
		return conf;
	}

	bool* toArray() {
		bool* result = new bool[conf.size()];

		for (size_t i = 0; i < conf.size(); i++) {
			result[i] = (conf[i] == YES);
		}

		return result;
	}

	friend ostream& operator<<(ostream& os, const Problem& x) {
		return os << x.conf;
	}
};

/**
  * Slave manager class
  */
class SlaveManager {
  private:
	int num_slaves;
	int working;
	int* results;
	MPI_Request* reqs;
	MPI_Status* stats;
	int* array_of_indices;

  public:
	SlaveManager(int ns) {
		num_slaves = ns;
		working = 0;
		results = new int[num_slaves];
		reqs = new MPI_Request[num_slaves];
		stats = new MPI_Status[num_slaves];
		array_of_indices = new int[num_slaves];
	}

	~SlaveManager() {
		delete[] results;
		delete[] reqs;
		delete[] stats;
		delete[] array_of_indices;
	}

	// master sends work to a slave
	void sendWork(Problem x, int reciever) {
		bool* buf = x.toArray();
		MPI_Send(buf, x.getSize(), MPI_C_BOOL, reciever, TAG.work, MPI_COMM_WORLD);
		delete[] buf;

		working++;
	}

	void sendEnd() {
		int empty = 0;
		for (int i = 1; i <= num_slaves; i++) MPI_Send(&empty, 1, MPI_INT, i, TAG.finished, MPI_COMM_WORLD);
	}

	void recieve(int &res, MPI_Status &status, const int source = MPI_ANY_SOURCE, const int tag = MPI_ANY_TAG) {
		MPI_Recv(&res, 1, MPI_INT, source, tag, MPI_COMM_WORLD, &status);
		working--;
	}

	void recieveEnd() {
		for (int i = 1; i <= num_slaves; i++) {
			recieve(results[0], stats[0]);
			// cout << "[ master ] recieved :: source: " << setfill('0') << setw(2) << stats[0].MPI_SOURCE << " :: end" << endl;
		}
	}

	void Irecieve(const int source, const int tag = MPI_ANY_TAG) {
		MPI_Irecv(&results[source-1], 1, MPI_INT, source, tag, MPI_COMM_WORLD, &reqs[source-1]);
	}

	void serve(deque<Problem> &problemQueue, int &res) {

		int oCount;
		MPI_Testsome(num_slaves, reqs, &oCount, array_of_indices, stats);

		if (oCount != MPI_UNDEFINED)
			working -= oCount;

		// serve all slaves which have finished their job
		for (int i = 0; i < oCount; i++) {
			int src = stats[i].MPI_SOURCE;
			// cout << "[ master ] recieved :: source: " << setfill('0') << setw(2) << src << " :: " << results[src-1] << ", job's done" << endl;

			if (results[src-1] < res) res = results[src-1];

			if (!problemQueue.empty()) {
				sendWork(problemQueue.front(), src);
				problemQueue.pop_front();

				Irecieve(src);
			}
		}

	}

	int Working() const {
		return working;
	}

};

/**
  * Computation manager class
  */
class Solver {
  protected:
	Graph* g;
	int a;
	int p;

	int getMaxBW() {
		return g->getSize() * g->getSize();
	}

	// iterative BFS
	deque<Problem> generate(Problem x, size_t p) {
		deque<Problem> conf;
		conf.push_back(x);

		while(conf.size() < p && !conf.front().isValid()) {
			int levelSize = conf.size();

			// generating equal-depth tree nodes
			for (int n = 0; n < levelSize; n++) {
				Problem tmp = conf.front();
				conf.pop_front();
				Problem add1 = tmp.decideNext(YES);
				Problem add0 = tmp.decideNext(NO);
				if (add1.isGenerating()) conf.push_back(add1);
				if (add0.isGenerating()) conf.push_back(add0);
			}
		}

		return conf;
	}

	int solveSingle(Problem x) {

		if (!x.isGenerating()) 
			return getMaxBW();

		if (x.isValid())
			return compute(x);

		return min(solveSingle(x.decideNext(YES)), solveSingle(x.decideNext(NO)));
	}

  public:
	Solver(Graph* g, int a, int p) {
		this->g = g;
		this->a = a;
		this->p = p;
	}

	~Solver() {}

	int compute(Problem x) const {
		int counter = 0;
		string conf = x.getConf();

		for (int i = 0; i < x.getSize(); i++) {
			if (conf[i] == YES) {
				for (int n = 0; n < x.getSize(); n++) {
					if (conf[n] == NO && g->hasEdge(i,n)) counter++;
				}
			}
		}

		return counter;
	}

	void solve(int multiplier1 = 8, int multiplier2 = 4, int servingInterval = 50, int servingInterval2 = 1000) {

		int rank;
		MPI_Comm_rank(MPI_COMM_WORLD, &rank);
		MPI_Status status;

		if (rank == 0) {
			// im master

			int num_procs;
			MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

			int minPoolSize = multiplier1 * num_procs;
			int vysledek = getMaxBW();
			Problem s0 = Problem(g->getSize(), a);
			SlaveManager SM = SlaveManager(num_procs-1);

			// cout << "[ master ] is ready for action" << endl;

			deque<Problem> problemQueue = generate(s0, minPoolSize);

			// cout << "[ master ] sending jobs (" << problemQueue.size() << ")" << endl;

			// send problems to everyone
			for (int i = 1; i < num_procs && !problemQueue.empty(); i++) {
				SM.sendWork(problemQueue.front(), i);
				problemQueue.pop_front();

				SM.Irecieve(i);
			}

			int servingCounter = 0;

			// computation while
			while (true) {

				if (problemQueue.size() < (size_t)num_procs) {
					// serve last jobs 
					// (master doesnt compute last jobs, because it doesnt use thread paralelization)
					SM.serve(problemQueue, vysledek);

					if (problemQueue.empty() && servingCounter != -1) {
						// cout << "[ master ] info :: job queue is empty" << endl;

						SM.sendEnd();
						servingCounter = -1;
					}

					// all slaves have done their job
					// even if there are last few problems left, working is both decreased and increased in serve method
					if (SM.Working() == 0) break;

					usleep(servingInterval2);
				}
				else {
					// master is counting and once a while serving slaves
					stack<Problem> conf;
					conf.push(problemQueue.front());
					problemQueue.pop_front();
					int bw = getMaxBW();

					while(!conf.empty()) {

						// serving
						servingCounter++;
						if (servingCounter > servingInterval) {
							servingCounter = 0;
							SM.serve(problemQueue, vysledek);
						}

						// computing
						Problem tmp = conf.top();
						conf.pop();
						if (tmp.isValid()) {
							bw = min(bw, compute(tmp));
						}
						else {
							Problem add1 = tmp.decideNext(YES);
							Problem add0 = tmp.decideNext(NO);
							if (add1.isGenerating()) conf.push(add1);
							if (add0.isGenerating()) conf.push(add0);
						}
					}
					// cout << "[ master ] info :: " << bw << " job's done" << endl;

					if (bw < vysledek) vysledek = bw;
				}
			}

			// recieve end tags
			SM.recieveEnd();

			// cout << "\nfinal result: " << vysledek << endl;

		}
		else {
			// im a slave
			// cout << "[slave " << setfill('0') << setw(2) << rank << "] is ready for action" << endl;

			while (true) {
				int recSize;
				bool* buf = new bool[g->getSize()];

				MPI_Recv(buf, g->getSize(), MPI_C_BOOL, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
				MPI_Get_count(&status, MPI_C_BOOL, &recSize);

				if (status.MPI_TAG == TAG.finished) {
					MPI_Send(buf, 1, MPI_C_BOOL, 0, TAG.done, MPI_COMM_WORLD);
					break;
				}

				Problem w = Problem(g->getSize(), a, buf, recSize);

				int minPoolSize = multiplier2 * p;
				int min_res = getMaxBW();
				size_t i;
				int tmp;

				deque<Problem> poolBase = generate(w, minPoolSize);

				#pragma omp parallel for num_threads(p) default(shared) private(i, tmp) schedule(dynamic)
					for (i = 0; i < poolBase.size(); i++) {
						tmp = solveSingle(poolBase[i]);
						if (tmp < min_res) {
							#pragma omp critical
								if (tmp < min_res) min_res = tmp;
						}
					}

				MPI_Send(&min_res, 1, MPI_INT, 0, TAG.work, MPI_COMM_WORLD);
			}

		}
	}
};


int main(int c, char *arg[]) {

	int provided, required = MPI_THREAD_FUNNELED;
	MPI_Init_thread(&c, &arg, required, &provided);

	if (provided < required)
		throw runtime_error("MPI library does not provide required threading support.");

	int a, p;
	string in;
	Graph* g;

	double starttime, endtime;
	int num_procs, rank;
	MPI_Comm_size(MPI_COMM_WORLD, &num_procs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	a = 5;
	p = 1;

	if (c > 2) {
		in = arg[1];
		a = atoi(arg[2]);
	}
	if (c > 3) p = atoi(arg[3]);

	starttime = MPI_Wtime();

	try {
		g = new Graph(in);
	}
	catch (runtime_error const &ex) {
		if (rank == 0) cout << ex.what() << endl;
		MPI_Finalize();
		return 0;
	}

	Solver s = Solver(g, a, p);
	s.solve(8, 4, 100);

	endtime = MPI_Wtime();

	if (rank == 0) cout << num_procs << " " << p << " " << (endtime - starttime) << endl;

	delete g;
	MPI_Finalize();
	return 0;

}