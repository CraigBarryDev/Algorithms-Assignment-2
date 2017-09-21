package mazeSolver;

import maze.Cell;
import maze.Maze;
import maze.Wall;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {

	private int cellsExplored = 0;
	private int travelDir = 0;
	private Cell cCell = null;
	private boolean isSolved = false;
	
	@Override
	public void solveMaze(Maze maze) {
		//Reset the number of cells explored
		cellsExplored = 0;
		//Reset whether the maze was solved
		isSolved = false;
		//Stores the current cell (starting at the entrance cell)
		cCell = maze.entrance;
		//Stores the current direction (starting north)
		travelDir = Maze.NORTH;

		//Loop until the exit is reached
		while(cCell != maze.exit) {
			//While the traveling direction can't go straight

			if(canMoveLeft(cCell, travelDir)) {
				System.out.println("Turned Left");
				travelDir = turnLeft(travelDir);
			}else if(canMoveStraight(cCell, travelDir)) {
				System.out.println("Went Straight");
				//Do nothing
			}else if(canMoveRight(cCell, travelDir)) {
				System.out.println("Turned Right");
				travelDir = turnRight(travelDir);
			}else {
				travelDir = turnAround(travelDir);
				System.out.println("Turned Around");
			}

			// while(!canMoveStraight(cCell, travelDir) && !canMoveLeft(cCell, travelDir))
			// 	//Turn left
			// 	travelDir = turnLeft(travelDir);

			//Now that you can move straight, move straight
			cCell = travelStraight(maze, cCell, travelDir);

			//Increment the number of cells explored
			cellsExplored++;

			maze.drawFtPrt(cCell);
		}

		//Set the maze to solved
		isSolved = true;

	} // end of solveMaze()

	private int turnLeft(int cDir) {
		//Increment the direction
		cDir += 1;
		//If the direction is at the max
		if(cDir == Maze.NUM_DIR)
			//Return the first direction
			cDir = 0;

		//Return the direction after turing left
		return cDir;
	}

	private int turnRight(int cDir) {
		//Decrement the direction
		cDir -= 1;
		//If the direction is invalid
		if(cDir == -1)
			//Return the correct direction
			cDir = Maze.NUM_DIR - 1;

		//Return the direction after turning right
		return cDir;
	}

	private int turnAround(int cDir) {
		return Maze.oppoDir[cDir];
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

	private boolean canMoveLeft(Cell c, int dir) {
		dir = turnLeft(dir);
		return !c.wall[dir].present && c.neigh[dir] != null;
	}

	private boolean canMoveRight(Cell c, int dir) {
		dir = turnRight(dir);
		return !c.wall[dir].present && c.neigh[dir] != null;
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
