package mazeSolver;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.*;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {

	private int cellsExplored = 0;
	private boolean isSolved = false;

	private final int LEFT = 1;
	private final int RIGHT = -1;

	private class CheckPoint {
		public CheckPoint(int direction, Cell cell) {
			this.direction = direction;
			this.cell = cell;
		}

		public int direction;
		public Cell cell;
	}
	
	@Override
	public void solveMaze(Maze maze) {
		//Stores the current cell (starting at the entrance)
		Cell cCell = maze.entrance;
		//Stores the current direction (starting north)
		int travelDir = Maze.NORTH;
		//Reset the number of cells explored (set to 1 as last cell is ignored)
		cellsExplored = 1;
		//Reset whether the maze was solved
		isSolved = false;
		//Stores whether the last movement was going through a tunnel
		boolean tunneled = false;

		//Stores the entrance/exit directions of each tunnel
		HashMap<Cell,Integer> tunnelDirections = new HashMap();
		//Stores checkpoints (an optimization on the algorithm that reduces the need,
		//to retrace back after hitting a dead-end)
		Stack<CheckPoint> checkpoints = new Stack();
		//Stores which cells have been visited
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];


		//Stores the number of tunnels
		int nTunnels = 0;
		//Iterate through the cells in the maze
		for(int i = 0; i < maze.sizeR; i++) {
			for(int j = 0; j < maze.sizeC; j++) {
				//If the cell has a tunnel
				if(maze.map[i][convertCellCol(maze, i, j)].tunnelTo != null) 
					//Initially each tunnel direction is set to north
					tunnelDirections.put(maze.map[i][convertCellCol(maze, i, j)], Maze.NORTH);
			}
		}	

		int revisits = 0;
		//Loop until the exit is reached
		while(cCell != maze.exit) {
			//Add the current cell to the list of visited cells
			visited[cCell.r][convertCellCol(maze, cCell)] = true;
			

			//If the current cell has a tunnel on it (and the last step wasn't moving,
			//through a tunnel, as to not get the function in an infinite loop of moving
			//through the same tunnel)
			// if(cCell.tunnelTo != null && !tunneled) {
			// 	//Set the entrance tunnel's travel direction to the current travel direction,
			// 	//this will ensure we are traveling the same direction if we exit the tunnel
			// 	tunnelDirections.put(cCell, travelDir);
			// 	//Move through the tunnel
			// 	cCell = cCell.tunnelTo;
			// 	//Set the travel direction to the exit tunnel's travel direction, this will ensure
			// 	//If we go through the tunnel multiple times we check all directions rather than
			// 	//whatever direction we were travelling when we went through the entrance portal
			// 	travelDir = tunnelDirections.get(cCell);
			// 	//Draw to visualize that a cell has been visited
			// 	maze.drawFtPrt(cCell);
			// 	//Set the tunneled flag to stop an infinite loop
			// 	tunneled = true;
			// }else {
			// 	//Unset the tunneled flag
			// 	tunneled = false;
			// }

			//Get the possible travel directions
			ArrayList<Integer> travelDirections = getPossibleTravelDirections(maze, cCell, travelDir, visited);

			//If there is more than 1 direction that can be travlled
			if(travelDirections.size() != 0) {
				//Set the travel direction to the leftmost direction
				//(the leftmost will always be first in the list)
				travelDir = travelDirections.get(0);

				//Iterate through the remianing travel directions
				for(int i = 1; i < travelDirections.size(); i++) {
					//Gets the neighbouring cell in the travel direction
					Cell nc = cCell.neigh[travelDirections.get(i)];
					//Add them to the list of checkpoints in clockwise order
					checkpoints.push(new CheckPoint(travelDirections.get(i), nc));
				}

				//Travel the given travel direction
				cCell = travelStraight(maze, cCell, travelDir);
			}
			//If there are no directions that can be travelled from here
			else {
				//Get the most recent checkpoint
				CheckPoint cp = checkpoints.pop();
				//Revert the state to the checkpoint
				cCell = cp.cell;
				travelDir = cp.direction;
			}

			//Draw to visualize that a cell has been visited
			maze.drawFtPrt(cCell);				

			//Increment the number of cells explored
			cellsExplored++;
		}

		//Set the maze to solved
		isSolved = true;

	} // end of solveMaze()

	private int turn(Maze m, int cDir, int turnDir) {
		//turn in the given direction
		cDir += turnDir;
		//If the direction is at the max
		if(cDir >= Maze.NUM_DIR)
			//Return the first direction
			cDir = 0;

		//If the direction is below the min
		if(cDir < 0)
			//Return the last direction
			cDir = Maze.NUM_DIR - 1;

		//Northeast doesn't exist in non-hexagonal maze, use next index
		if(m.type != Maze.HEX && cDir == Maze.NORTHEAST)
			cDir += turnDir;

		//Southeast doesn't exist in non-hexagonal maze, use next index
		if(m.type != Maze.HEX && cDir == Maze.SOUTHWEST)
			cDir += turnDir;

		//Return the direction after turing left
		return cDir;
	}

	private int convertCellCol(Maze m, int baseRow, int baseCol) {
		//If the maze is a hex maze
		if(m.type == Maze.HEX)
			//Increment column to be in the 0 to C-1  range
			baseCol += (baseRow + 1) / 2;

		//Return the conveted column value
		return baseCol;
	}

	private int convertCellCol(Maze m, Cell c) {
		int col = c.c;
		//If the maze is a hex maze
		if(m.type == Maze.HEX)
			//Decrement column to be in the (R + 1) / 2 to C + (R + 1) / 2  range
			col -= (c.r + 1) / 2;

		//Return the conveted column value
		return col;
	}

	private Cell travelStraight(Maze m, Cell c, int dir) {
		//Calculate the final position after the movement
		Cell fCell = m.map[c.r + Maze.deltaR[dir]][c.c + Maze.deltaC[dir]];
		//Return the new position
		return fCell;
	}

	private boolean canMoveStraight(Cell c, int dir) {
		//If there is no wall in the current direction
		return !c.wall[dir].present;
	}

	private ArrayList<Integer> getPossibleTravelDirections(Maze m, Cell c, int dir, boolean[][] visited) {
		//Stores the possible travelDirections
		ArrayList<Integer> directions = new ArrayList();

		//Reverse the travel direction and turn right
		//This will give start the direction of the leftmost direction
		int cDir = turn(m, Maze.oppoDir[dir], RIGHT);

		//Iterate through all the directions by turning right
		//if we can travel this direction then it is the next travel direction
		while(cDir != Maze.oppoDir[dir]) {
			//If this is a possible way that can be travelled
			if(c.neigh[cDir] != null) {
				//If it is possible to move in this direction and the cell hasn't been visited
				if(canMoveStraight(c, cDir) &&
					!visited[c.neigh[cDir].r][convertCellCol(m, c.neigh[cDir])]) {

					//Add it to the list of possible directions
					directions.add(cDir);
				}
			}

			//Turn right
			cDir = turn(m, cDir, RIGHT);
		}
		
		//Return the list of possible travel directions starting from
		//the leftmost possible direction in clockwise order
		return directions;
	}
    
    
	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()
    
    
	@Override
	public int cellsExplored() {
		return cellsExplored;
	} // end of cellsExplored()

} // end of class WallFollowerSolver
