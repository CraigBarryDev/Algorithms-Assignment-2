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

	private final int LEFT = 1;
	private final int RIGHT = -1;
	
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

			if(canMoveLeft(maze, cCell, travelDir)) {
				System.out.println("Turned Left");
				travelDir = turn(maze, travelDir, LEFT);
			}else if(canMoveStraight(cCell, travelDir)) {
				System.out.println("Went Straight");
				//Do nothing
			}else if(canMoveRight(maze, cCell, travelDir)) {
				System.out.println("Turned Right");
				travelDir = turn(maze, travelDir, RIGHT);
			}else {
				travelDir = turnAround(travelDir);
				System.out.println("Turned Around");
			}

			//Now that you can move straight, move straight
			cCell = travelStraight(maze, cCell, travelDir);

			//Increment the number of cells explored
			cellsExplored++;

			maze.drawFtPrt(cCell);
		}

		//Set the maze to solved
		isSolved = true;

	} // end of solveMaze()

	// private int turnLeft(Maze m, int cDir) {
	// 	//Increment the direction
	// 	cDir += 1;
	// 	//If the direction is at the max
	// 	if(cDir == Maze.NUM_DIR)
	// 		//Return the first direction
	// 		cDir = 0;

	// 	//Northeast doesn't exist in non-hexagonal maze, use next index
	// 	if(m.type != Maze.HEX && cDir == Maze.NORTHEAST)
	// 		cDir += 1;

	// 	//Southeast doesn't exist in non-hexagonal maze, use next index
	// 	if(m.type != Maze.HEX && cDir == Maze.SOUTHWEST)
	// 		cDir += 1;

	// 	//Return the direction after turing left
	// 	return cDir;
	// }

	// private int turnRight(Maze m, int cDir) {
	// 	//Decrement the direction
	// 	cDir -= 1;
	// 	//If the direction is invalid
	// 	if(cDir == -1)
	// 		//Return the correct direction
	// 		cDir = Maze.NUM_DIR - 1;

	// 	//Northeast doesn't exist in non-hexagonal maze, use next index
	// 	if(m.type == Maze.NORMAL && cDir == Maze.NORTHEAST)
	// 		cDir -= 1;

	// 	//Southeast doesn't exist in non-hexagonal maze, use next index
	// 	if(m.type == Maze.NORMAL && cDir == Maze.SOUTHWEST)
	// 		cDir -= 1;

	// 	//Return the direction after turning right
	// 	return cDir;
	// }

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

	private boolean canMoveLeft(Maze m, Cell c, int dir) {
		dir = turn(m, dir, LEFT);
		return !c.wall[dir].present && c.neigh[dir] != null;
	}

	private boolean canMoveRight(Maze m, Cell c, int dir) {
		dir = turn(m, dir, RIGHT);
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
