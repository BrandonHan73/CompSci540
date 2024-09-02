
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MazeNode {

	List<MazeNode> neighbors;
	int row, col;
	String successor;

	MazeNode(int row, int col) {
		this.row = row;
		this.col = col;

		neighbors = new ArrayList<>();
		successor = "";
	}

	static void printMaze(PrintWriter out, MazeNode[][] maze) {
		printMaze(out, maze, new ArrayList<>());
	}

	static void printMaze(PrintWriter out, MazeNode[][] maze, MazeNode[] mark) {
		printMaze(out, maze, Arrays.asList(mark));
	}

	static void printMaze(PrintWriter out, MazeNode[][] maze, Collection<MazeNode> mark) {
		for(MazeNode[] row : maze) {
			for(MazeNode n : row) {
				out.print('+');
				out.print(n.successor.contains("U") ? "  " : "--");
			}
			out.println('+');
			for(MazeNode n : row) {
				out.print(n.successor.contains("L") ? ' ' : '|');
				out.print(mark.contains(n) ? "@@" : "  ");
			}
			out.println(row[row.length - 1].successor.contains("R") ? ' ' : '|');
		}
		for(MazeNode n : maze[maze.length - 1]) {
			out.print('+');
			out.print(n.successor.contains("D") ? "  " : "--");
		}
		out.println('+');
	}

	static void printSuccessors(PrintWriter out, MazeNode[][] maze) {
		for(MazeNode[] r : maze) {
			for(MazeNode c : r) {
				out.print(c.successor + ',');
			}
			out.println();
		}
	}

	static String[] getLines(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));

		List<String> input = new ArrayList<>();
		String in;
		while((in = br.readLine()) != null) {
			input.add(in);
		}

		String[] maze = new String[input.size()];
		maze = input.toArray(maze);
		return maze;
	}

	static MazeNode[][] readMaze(String[] maze) {
		int height = maze.length / 2;
		int width = maze[0].length() / 3;
		MazeNode[][] out = new MazeNode[height][width];
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				out[r][c] = new MazeNode(r, c);
			}
		}

		char up, down, left, right;
		int row, col;
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				row = 2 * r + 1;
				col = 3 * c + 1;

				up = maze[row - 1].charAt(col);
				down = maze[row + 1].charAt(col);
				left = maze[row].charAt(col - 1);
				right = maze[row].charAt(col + 2);

				if(up == ' ') {
					out[r][c].successor += 'U';
					if(r > 0) {
						out[r][c].neighbors.add(out[r - 1][c]);
					}
				}

				if(down == ' ') {
					out[r][c].successor += 'D';
					if(r < height - 1) {
						out[r][c].neighbors.add(out[r + 1][c]);
					}
				}

				if(left == ' ') {
					out[r][c].successor += 'L';
					if(c > 0) {
						out[r][c].neighbors.add(out[r][c - 1]);
					}
				}

				if(right == ' ') {
					out[r][c].successor += 'R';
					if(c < width - 1) {
						out[r][c].neighbors.add(out[r][c + 1]);
					}
				}
			}
		}

		return out;
	}

}

