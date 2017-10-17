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

	private final int ENTRANCE = 0;
	private final int EXIT = 1;

	@Override
	public void solveMaze(Maze maze) {
		//Initialize solving state
		isSolved = false;
		cellsExplored = 0;
		int turn = 0;
		
		//The current cell is the start of the maze
		Cell cCellFEntrance = maze.entrance;
		Cell cCellFExit = maze.exit;
		//Stores the current cell of whichever DFS is currently executing
		Cell cCell = null;

		//Stores the current path from the starting cell
		LinkedList<Cell> entrancePath = new LinkedList();
		LinkedList<Cell> exitPath = new LinkedList();
		//Stores the cells that have been visited
		boolean entranceVisited[][] = new boolean[maze.sizeR][maze.sizeC];
		boolean exitVisited[][] = new boolean[maze.sizeR][maze.sizeC];

		do {
			//Do 1 step in the DFS search of both steps
			cCellFEntrance = dfsStep(maze, cCellFEntrance, entrancePath, entranceVisited);
			// cCellFExit = dfsStep(maze, cCellFExit, exitPath, exitVisited);

			//Get the row and column indices of the current cell
			int cellRow = cCellFExit.r;
			int cellCol = cCellFExit.c;

			//If the maze is a hex maze
			if(maze.type == Maze.HEX)
				//Decrement column to be in the 0 to C-1  range
				cellCol -= (cellRow + 1) / 2;

			if(entranceVisited[cellRow][cellCol]) isSolved = true;

			//Get the row and column indices of the current cell
			cellRow = cCellFEntrance.r;
			cellCol = cCellFEntrance.c;

			//If the maze is a hex maze
			if(maze.type == Maze.HEX)
				//Decrement column to be in the 0 to C-1  range
				cellCol -= (cellRow + 1) / 2;

			if(exitVisited[cellRow][cellCol]) isSolved = true;
			if(cCellFEntrance == maze.exit) isSolved = true;
			if(cCellFExit == maze.entrance) isSolved = true;
		}while(!isSolved);

	} // end of solveMaze()

	//Performs a step in the DFS search and returns the current Cell after the step has completed
	//cCell, path and visitedCells may be modified in the process
	private Cell dfsStep(Maze maze, Cell cCell, LinkedList<Cell> path, boolean[][] visitedCells) {
		//If this cell is a tunnel, go through the tunnel
		if(cCell.tunnelTo != null) {
			//Go through the tunnel and update state accordingly
			cCell = goThroughTunnel(maze, cCell, path, visitedCells);
		}

		//Get the row and column indices of the current cell
		int cellRow = cCell.r;
		int cellCol = cCell.c;

		//If the maze is a hex maze
		if(maze.type == Maze.HEX)
			//Decrement column to be in the 0 to C-1  range
			cellCol -= (cellRow + 1) / 2;

		if(!visitedCells[cellRow][cellCol]) {
			//Increment the number of cells explored
			cellsExplored++;
		}
		//Set the current cell as visited
		visitedCells[cellRow][cellCol] = true;

		//Get the next possible cells to visit
		ArrayList<Cell> possibleNeighbours = getAccesibleUnvisitedNeighbours(maze, cCell, visitedCells);

		//Draw to visualize that a cell has been visited
		maze.drawFtPrt(cCell);	

		//If there are unvisited neighbours at this cell
		if(possibleNeighbours.size() != 0) {
			//Add the current cell the current path
			path.add(cCell);
			//Pick a random neighbour and move to it
			cCell = getRandElem(possibleNeighbours);
		}else {
			//Get the last cell in the path
			Cell pathTail = path.getLast();
			//Only remove from the path if it is actually in the path, or if it is a tunnel
			//cell (otherwise you can get stuck in a infinite loop of moving through the same tunnel)
			if(pathTail == cCell || cCell.tunnelTo != null){
				//Remove the current cell from the path
				path.removeLast();
			}

			//If there is still items left in the path
			if(path.size() != 0){
				//The current cell is now the previous item (the new tail of the path)
				cCell = path.getLast();
			}
		}

		return cCell;
	}

	private Cell goThroughTunnel(Maze maze, Cell cell, LinkedList<Cell> path, boolean[][] visitedCells) {
		//Get the row and column indices of the current cell
		int cellRow = cell.r;
		int cellCol = cell.c;

		//If the maze is a hex maze
		if(maze.type == Maze.HEX)
			//Decrement column to be in the 0 to C-1  range
			cellCol -= (cellRow + 1) / 2;

		if(visitedCells[cellRow][cellCol]) {
			//Increment the number of cells explored
			cellsExplored++;
		}else {
			//Set the current cell as visited
			visitedCells[cellRow][cellCol] = true;
		}

		//Draw to visualize that a cell has been visited
		maze.drawFtPrt(cell);	

		//Go through the tunnel
		cell = cell.tunnelTo;

		//Return the cell at the exit of the tunnel
		return cell;
	}

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
