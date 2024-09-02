
import java.io.PrintWriter;

import java.lang.StringBuilder;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Search {

	static class SearchNode {
		MazeNode node;
		SearchNode prev;
		int depth;

		SearchNode(MazeNode node, SearchNode prev) {
			this(node, prev, 0);
		}

		SearchNode(MazeNode node, SearchNode prev, int depth) {
			this.node = node;
			this.prev = prev;
			this.depth = depth;
		}

		int getDepth() {
			return depth;
		}
	}

	static void printVisited(PrintWriter out, MazeNode[][] maze, Collection<MazeNode> visited) {
		for(int r = 0; r < maze.length; r++) {
			for(int c = 0; c < maze[r].length; c++) {
				out.print(visited.contains(maze[r][c]) ? '1' : '0');
				out.print(", ");
			}
			out.println();
		}
	}

	static void printEstimatedHeuristics(PrintWriter out, double[][] h) {
		for(double[] row : h) {
			for(double val : row) {
				out.print(val);
				out.print(", ");
			}
			out.println();
		}
	}

	static String actionSequence(MazeNode[] path) {
		StringBuilder out = new StringBuilder();
		MazeNode prev, next;
		for(int i = 1; i < path.length; i++) {
			prev = path[i - 1];
			next = path[i];

			if(prev.row < next.row) {
				out.append('D');
			} else if(prev.row > next.row) {
				out.append('U');
			} else if(prev.col < next.col) {
				out.append('R');
			} else if(prev.col > next.col) {
				out.append('L');
			}
		}

		return out.toString();
	}

	static double[][] hManhattan(MazeNode end, MazeNode[][] maze) {
		double[][] out = new double[maze.length][maze[0].length];
		for(int r = 0; r < maze.length; r++) {
			for(int c = 0; c < maze[r].length; c++) {
				out[r][c] = 
					Math.abs(end.row - maze[r][c].row) 
					+ Math.abs(end.col - maze[r][c].col);
			}
		}
		return out;
	}

	static double[][] hEuclidean(MazeNode end, MazeNode[][] maze) {
		double[][] out = new double[maze.length][maze[0].length];
		for(int r = 0; r < maze.length; r++) {
			for(int c = 0; c < maze[r].length; c++) {
				out[r][c] = Math.sqrt(
					Math.pow(end.row - maze[r][c].row, 2) 
					+ Math.pow(end.col - maze[r][c].col, 2)
				);
			}
		}
		return out;
	}

	static MazeNode[] bfs(MazeNode start, MazeNode end, Set<MazeNode> visited) {
		Queue<SearchNode> pq = new LinkedList<>();

		pq.add(new SearchNode(start, null));

		SearchNode s = null;
		while(!pq.isEmpty()) {
			s = pq.poll();

			if(!visited.contains(s.node)) {

				visited.add(s.node);
				for(MazeNode n : s.node.neighbors) {
					pq.add(new SearchNode(n, s));
				}

				if(s.node == end) {
					break;
				}

			}
		}

		List<MazeNode> list = new LinkedList<>();
		while(s != null) {
			list.add(0, s.node);
			s = s.prev;
		}

		return list.toArray(new MazeNode[list.size()]);
	}

	static MazeNode[] dfs(MazeNode start, MazeNode end, Set<MazeNode> visited) {
		Stack<SearchNode> stack = new Stack<>();

		stack.push(new SearchNode(start, null));

		SearchNode s = null;
		while(!stack.empty()) {
			s = stack.pop();

			if(!visited.contains(s.node)) {

				visited.add(s.node);
				for(MazeNode n : s.node.neighbors) {
					stack.push(new SearchNode(n, s));
				}

				if(s.node == end) {
					break;
				}

			}
		}

		List<MazeNode> list = new LinkedList<>();
		while(s != null) {
			list.add(0, s.node);
			s = s.prev;
		}

		return list.toArray(new MazeNode[list.size()]);
	}

	static MazeNode[] aStar(MazeNode start, MazeNode end, Set<MazeNode> visited, double[][] heuristic) {
		PriorityQueue<SearchNode> stack = new PriorityQueue<>(Comparator.comparingDouble(n -> n.depth + heuristic[n.node.row][n.node.col]));

		stack.add(new SearchNode(start, null));

		SearchNode s = null;
		while(!stack.isEmpty()) {
			s = stack.poll();

			if(!visited.contains(s.node)) {

				visited.add(s.node);
				for(MazeNode n : s.node.neighbors) {
					stack.add(new SearchNode(n, s));
				}

				if(s.node == end) {
					break;
				}

			}
		}

		List<MazeNode> list = new LinkedList<>();
		while(s != null) {
			list.add(0, s.node);
			s = s.prev;
		}

		return list.toArray(new MazeNode[list.size()]);
	}

}

