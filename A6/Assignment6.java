
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Assignment6 {

	public static void main(String[] args) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
		out.println("##a: 6");
		out.println("##id: bhan48");

		// Question 1 (Part 1)
		out.println("##1:");

		String[] mazeLines = MazeNode.getLines("maze.txt");
		MazeNode[][] maze = MazeNode.readMaze(mazeLines);

		MazeNode.printMaze(out, maze);

		// Question 2 (Part 1)
		out.println("##2:");

		MazeNode.printSuccessors(out, maze);

		// Question 3 (Part 1)
		out.println("##3:");

		MazeNode start = maze[0][ maze[0].length / 2 ];
		MazeNode end = maze[ maze.length - 1 ][ maze[0].length / 2 ];

		Set<MazeNode> bfsVisited = new HashSet<>();
		MazeNode[] bfsPath = Search.bfs(start, end, bfsVisited);

		String solutionActionSequence = Search.actionSequence(bfsPath);
		out.println(solutionActionSequence);

		// Question 4 (Part 1)
		out.println("##4:");

		MazeNode.printMaze(out, maze, bfsPath);

		// Question 5 (Part 1)
		out.println("##5:");

		Search.printVisited(out, maze, bfsVisited);

		// Question 6 (Part 1)
		out.println("##6:");

		Set<MazeNode> dfsVisited = new HashSet<>();
		MazeNode[] dfsPath = Search.dfs(start, end, dfsVisited);

		Search.printVisited(out, maze, dfsVisited);

		// Question 7 (Part 2)
		out.println("##7:");

		double[][] hManhattan = Search.hManhattan(end, maze);
		Search.printEstimatedHeuristics(out, hManhattan);

		// Question 8 (Part 2)
		out.println("##8:");

		Set<MazeNode> aStarManhattanVisited = new HashSet<>();
		MazeNode[] aStarManhattanPath = Search.aStar(start, end, aStarManhattanVisited, hManhattan);

		Search.printVisited(out, maze, aStarManhattanVisited);

		// Question 9 (Part 2)
		out.println("##9:");

		double[][] hEuclidean = Search.hManhattan(end, maze);

		Set<MazeNode> aStarEuclideanVisited = new HashSet<>();
		MazeNode[] aStarEuclideanPath = Search.aStar(start, end, aStarEuclideanVisited, hEuclidean);

		Search.printVisited(out, maze, aStarManhattanVisited);

		// Question 10
		out.println("##10: true");

		// Question 11
		out.println("##11: :)");

		out.close();
	}


}

