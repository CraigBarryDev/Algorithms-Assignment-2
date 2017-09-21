package mazeGenerator;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.Random;
import java.util.ArrayList;

public class ModifiedPrimsGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//Iterate through the rows of the maze
		for(int i = 0; i < maze.sizeR; i++) {
			//Iterate through the columns of the maze
			for(int j = 0; j < maze.sizeC; j++) {
				//Iterate through the walls of the cell
				for(int k = 0; k < Maze.NUM_DIR; k++) {
					//Create the wall objects for the cell
					maze.map[i][j].wall[k] = new Wall();
					//Set a wall on each side of the maze
					maze.map[i][j].wall[k].present = true;
				}
			}
		}

		switch(maze.type) {
			case Maze.NORMAL:
				generateNormalMaze(maze);
				break;
			case Maze.TUNNEL:
				generateTunnelMaze(maze);
				break;
			case Maze.HEX:
				generateHexMaze(maze);
				break;
		}
		
	} // end of generateMaze()

	private void generateNormalMaze(Maze maze) {
		//Generate Z and frontier lists
		ArrayList<Cell> zList = new ArrayList();
		ArrayList<Cell> fList = new ArrayList();
		//Create a random number generator
		Random rnd = new Random();
		//Calculate a random initial starting cell
		int startRow = (int)(rnd.nextDouble() * (double)maze.sizeR);
		int startCol = (int)(rnd.nextDouble() * (double)maze.sizeC);
		Cell startCell = maze.map[startRow][startCol];
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
				if(isRectangularAdjacent(zList.get(index),c))
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
	}

	//Creates a path between two adjacent cells c1 and c2
	private void carvePath(Maze m, Cell c1, Cell c2) {
		int cDiff = c2.c - c1.c;
		int rDiff = c2.r - c1.r;
		int dir = -1;
		for(int i = 0; i < Maze.NUM_DIR; i++) {
			if(rDiff == Maze.deltaR[i] && cDiff == Maze.deltaC[i]) {
				dir = i;
			}
		}

		m.map[c1.r][c1.c].wall[dir].present = false;
		m.map[c2.r][c2.c].wall[Maze.oppoDir[dir]].present = false;
	}

	private Cell[] getRectangularNeighbouringCells(Maze m, Cell c) {
		//Holds the neighbouring cells
		Cell cells[] = new Cell[Maze.NUM_DIR];

		//Iterate through all the cells and set them to nothing
		for(int i = 0; i < Maze.NUM_DIR; i++)
			cells[i] = null;
	
		//If the cell isn't on the southern edge, get the southern cell
		if(c.r != 0)
			cells[Maze.SOUTH] = m.map[c.r + m.deltaR[Maze.SOUTH]][c.c + m.deltaC[Maze.SOUTH]];

		//If the cell isn't opn the north edge, get the northern cell
		if(c.r != m.sizeR - 1)
			cells[Maze.NORTH] = m.map[c.r + m.deltaR[Maze.NORTH]][c.c + m.deltaC[Maze.NORTH]];

		//If the cell isn't on the eastern edge, get the eastern cell
		if(c.c != m.sizeC - 1)
			cells[Maze.EAST] = m.map[c.r + m.deltaR[Maze.EAST]][c.c + m.deltaC[Maze.EAST]];

		//If the cell isn't on the west edge, get the western cell
		if(c.c != 0)
			cells[Maze.WEST] = m.map[c.r + m.deltaR[Maze.WEST]][c.c + m.deltaC[Maze.WEST]];

		//Return the list of cells
		return cells;
	}

	private int getRndOfList(int[] items) {
		//Setup random number generator
		Random rnd = new Random();
		return items[rnd.nextInt(items.length)];
	}

	private boolean isRectangularAdjacent(Cell c1, Cell c2) {
		if(c1.r + 1 == c2.r && c1.c == c2.c ||
			c1.r - 1 == c2.r && c1.c == c2.c ||
			c1.c + 1 == c2.c && c1.r == c2.r ||
			c1.c - 1 == c2.c && c1.r == c2.r) {
			return true;
		}

		return false;
	}

	private void generateTunnelMaze(Maze maze) {

	}

	private void generateHexMaze(Maze maze) {

	}

} // end of class ModifiedPrimsGenerator
