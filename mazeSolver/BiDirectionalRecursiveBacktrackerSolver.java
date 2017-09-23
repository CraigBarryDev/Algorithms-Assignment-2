package mazeSolver;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.*;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {

	private boolean isSolved = false;
	private int cellsExplored = 0;

	@Override
	public void solveMaze(Maze maze) {
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];
		isSolved = false;
		cellsExplored = 0;
		
		//The current cell is the start of the maze
		Cell cCell = maze.entrance;

		//Stores the current path from the starting cell
		LinkedList<Cell> path = new LinkedList();

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
			//Get the next possible cells to visit
			ArrayList<Cell> possibleNeighbours = getAccesibleUnvisitedNeighbours(maze, cCell, visited);

			//If there are unvisited neighbours at this cell
			if(possibleNeighbours.size() != 0) {
				//Add the current cell the current path
				path.add(cCell);
				//Pick a random neighbour and move to it
				cCell = getRandElem(possibleNeighbours);
				//Increment the number of cells explored
				cellsExplored++;
				//Draw to visualize that a cell has been visited
				maze.drawFtPrt(cCell);
			}else {
				//Remove the current cell from the path
				path.removeLast();

				//If there is still items left in the path
				if(path.size() != 0)
					//The current cell is now the previous item (the new tail of the path)
					cCell = path.getLast();
			}

		}while(cCell != maze.exit || cCell == null);

		System.out.println("MAZE FINISHED SOLVING");
		System.out.println("Cell: " + (cCell == null));

		isSolved = cCell != null;



	} // end of solveMaze()

	private ArrayList<Cell> getAccesibleUnvisitedNeighbours(Maze m, Cell c, boolean[][] visitedCells) {
		//Stores the unvisited and accessible neighbours
		ArrayList<Cell> neighbours = new ArrayList();

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
				
				//If there is no wall between the cells and it hasn't been visited
				if(!c.wall[i].present && !visitedCells[cellRow][cellCol])
					//Add it to the list of unvisited & accessible neighbours
					neighbours.add(c.neigh[i]);
			}
		}

		//Reutrn the list of unvisited and accessible neighbours
		return neighbours;
	}

	private <Type> Type getRandElem(ArrayList<Type> arr) {
		//Create a random number generator
		Random rnd = new Random();
		//Get a random index of the array
		int index = rnd.nextInt(arr.size());
		//Return the element at the given random index
		return arr.get(index);
	}


	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return cellsExplored;
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
