package mazeGenerator;

import java.util.*;

import maze.Maze;
import maze.Cell;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//Pick a random cell to start the algorithm from
		Cell startCell = pickRandomCell(maze);
		//Stores the current cell
		Cell cCell = startCell;
		//Stores the current path from the starting cell
		LinkedList<Cell> path = new LinkedList();
		//Stores the cells that have been visited
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];
		//Stores whether the last movement was going through a tunnel
		boolean tunneled = false;

		//Iterate until the path is empty
		do {
			//Get the row and column indices of the current cell
			int cellRow = cCell.r;
			int cellCol = cCell.c;

			//If the maze is a hex maze
			if(maze.type == Maze.HEX)
				//Decrement column to be in the 0 to C-1  range
				cellCol -= (cellRow + 1) / 2;

			//Set the current cell as visited
			visited[cellRow][cellCol] = true;
			//Get the unvisited neighbours of the current cell
			ArrayList<Cell> unvisitedNeighbours = getUnvisitedNeighbours(maze, cCell, visited);

			//If the current cell has a tunnel on it (and the last step wasn't moving,
			//through a tunnel, as to not get the function in an infinite loop of moving
			//through the same tunnel)
			if(cCell.tunnelTo != null && !tunneled) {
				//Move through the tunnel
				cCell = cCell.tunnelTo;
				//Set the tunneled flag to stop an infinite loop
				tunneled = true;
			}
			//If there are unvisited neighbours at this cell
			else if(unvisitedNeighbours.size() != 0) {
				//Add the current cell the current path
				path.add(cCell);
				//Pick a random unvisited neighbour
				Cell nextCell = getRandElem(unvisitedNeighbours);
				//Carve a path between the current cell and the next cell
				carvePath(maze, cCell, nextCell);
				//The next cell will now become the current cell
				cCell = nextCell;
				//Unset the tunneled flag
				tunneled = false;
			}else {
				//Remove the current cell from the path
				path.removeLast();

				//If there is still items left in the path
				if(path.size() != 0)
					//The current cell is now the previous item (the new tail of the path)
					cCell = path.getLast();

				//Unset the tunneled flag
				tunneled = false;
			}

		}while(path.size() != 0);
		
	}

	private ArrayList<Cell> getUnvisitedNeighbours(Maze m, Cell c, boolean[][] visitedCells) {
		//Stores the unvisited neighbours
		ArrayList<Cell> unvisitedNeighbours = new ArrayList();

		//Iterate through the neighbouring cells
		for(int i = 0; i < Maze.NUM_DIR; i++) {
			//If the neighbour cell exists
			if(c.neigh[i] != null) {
				//Get the row and column indices of the neighbour cell
				int cellRow = c.neigh[i].r;
				int cellCol = c.neigh[i].c;

				//If the maze is a hex maze
				if(m.type == Maze.HEX)
					//Decrement column to be in the 0 to C-1  range
					cellCol -= (cellRow + 1) / 2;

				//If the cell is unvisited
				if(!visitedCells[cellRow][cellCol]) {
					//Add to the list of unvisited neighbours
					unvisitedNeighbours.add(c.neigh[i]);
				}
			}
		}

		//Return the list on unvisited neighbours
		return unvisitedNeighbours;
	}

	private <Type> Type getRandElem(ArrayList<Type> arr) {
		//Create a random number generator
		Random rnd = new Random();
		//Get a random index of the array
		int index = rnd.nextInt(arr.size());
		//Return the element at the given random index
		return arr.get(index);
	}

	//Picks a random cell
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

} // end of class RecursiveBacktrackerGenerator
