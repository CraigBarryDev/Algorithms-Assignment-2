package mazeGenerator;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.Random;
import java.util.ArrayList;

public class ModifiedPrimsGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//Generate Z and frontier lists
		ArrayList<Cell> zList = new ArrayList();
		ArrayList<Cell> fList = new ArrayList();
		//Create a random number generator
		Random rnd = new Random();
		//Get a randomly generated starting cell
		Cell startCell = pickRandomCell(maze);
		//Add the initial cell to the Z list
		zList.add(startCell);
		
		//Iterate through the neighbours of the starting cell
		for(int i = 0; i < Maze.NUM_DIR; i++) {
			//If the neighbour cell exists
			if(startCell.neigh[i] != null) {
				//Add the neighbour cell to the frontier list
				fList.add(startCell.neigh[i]);
			}
		}
		
		//If the z list doesn't encompass the entire list
		while(fList.size() != 0) {
			//Get a random cell from the frontier list
			Cell c = fList.get(rnd.nextInt(fList.size()));
			//Remove the cell from the frontier list
			fList.remove(c);

			//Start at a random index in the z list
			int index = rnd.nextInt(zList.size());
			//Stores the B cell
			Cell b = null;
			//Iterate until B equals something
			while(b == null) {
				//If cell is adjacent to c, use this cell
				if(isAdjacent(maze, zList.get(index),c))
					b = zList.get(index);

				//Increment the index
				index++;

				//If the index is at the end of the list
				if(index == zList.size())
					//Go back to the start of the list
					index = 0;
			}

			//Carve path betwen b and c
			carvePath(maze, c, b);

			//Add the item to the Z list
			zList.add(c);

			//Iterate through the neighbours of the c cell
			for(int i = 0; i < Maze.NUM_DIR; i++) {
				//If the neighbour cell exists
				if(c.neigh[i] != null) {
					//If this cell isn't already in the frontier list
					if(!zList.contains(c.neigh[i]) && !fList.contains(c.neigh[i])) {
						//Add the neighbour cell to the frontier list
						fList.add(c.neigh[i]);
					}
				}
			}
		}
		
	} // end of generateMaze()

	private Cell pickRandomCell(Maze m) {
		//Create a random number generator
		Random rnd = new Random();
		//Calculate a random initial starting cell
		int startRow = (int)(rnd.nextDouble() * (double)m.sizeR);
		int startCol = (int)(rnd.nextDouble() * (double)m.sizeC);

		//If the maze is a hex maze
		if(m.type == Maze.HEX)
			//Increment column to be in the (C + 1)/2 + C - 1 range
			startCol += (startRow + 1) / 2;

		//Return the starting cell from the list of maze cells
		return m.map[startRow][startCol];
	}

	//Creates a path between two adjacent cells c1 and c2
	private void carvePath(Maze m, Cell c1, Cell c2) {
		//Get the difference in column and row numbers
		int cDiff = c2.c - c1.c;
		int rDiff = c2.r - c1.r;
		//Direction initially set to -1 as to detect an error
		int dir = -1;

		//Iterate through the cell's neighbours
		for(int i = 0; i < Maze.NUM_DIR; i++) {
			//If delta values equal the row and column differences, set the direction to i
			if(rDiff == Maze.deltaR[i] && cDiff == Maze.deltaC[i]) {
				dir = i;
			}
		}
		
		//Set the wall to present from c1
		m.map[c1.r][c1.c].wall[dir].present = false;
		//Set the same wall present from c2
		m.map[c2.r][c2.c].wall[Maze.oppoDir[dir]].present = false;
	}

	private boolean isAdjacent(Maze m, Cell c1, Cell c2) {
		boolean isRectAdj = false;
		boolean isHexAdj = false;

		//If c2 is a NORTH, SOUTH, WEST or EAST neighbour of c1.
		//Then c1 is a rectangularly adjacent of c2
		if(c1.r + 1 == c2.r && c1.c == c2.c ||
			c1.r - 1 == c2.r && c1.c == c2.c ||
			c1.c + 1 == c2.c && c1.r == c2.r ||
			c1.c - 1 == c2.c && c1.r == c2.r) {
			isRectAdj = true;
		}

		//c1 will be hexagonally adjacent to c2 if the following
		//is true (it will automatically be hexagonally adjacent
		//if it was also rectanguarly adjacent)
		if(c1.c - 1 == c2.c && c1.r - 1 == c2.r ||
			c1.c + 1 == c2.c && c1.r + 1 == c2.r || isRectAdj) {
			isHexAdj = true;
		}

		//If the maze is a hexagonal maze, return if it is hexagonally adjacent
		if(m.type == Maze.HEX)
			return isHexAdj;

		//If the maze is not hexagonal, return if it is rectanguarly adjacent
		return isRectAdj;
	}

} // end of class ModifiedPrimsGenerator
