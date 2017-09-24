package mazeGenerator;

import java.util.*;

import maze.Maze;
import maze.Cell;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	private final int PICK_RANDOM = 0;
	private final int PICK_NEWEST = 1;
	private double threshold = 0.1;
	
	@Override
	public void generateMaze(Maze maze) {
		//Pick a random cell to start the algorithm from
		Cell startCell = pickRandomCell(maze);
		//Generate Z and frontier lists
		ArrayList<Cell> zList = new ArrayList();
		ArrayList<Cell> fList = new ArrayList();
		//Stores the visited status of each cell
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];
		//Add the first cell to the zList
		zList.add(startCell);

		//Iterate until zList is empty
		do {
			//Cell B is selected from the zList using a random method
			Cell b = null;

			//Determine which method will be used to pick cell B
			int pickMethod = determinePickMethod(threshold);

			//If the pick method picks the most recent value
			if(pickMethod == PICK_NEWEST)
				//Get the last item in the zList
				b = zList.get(zList.size() - 1);
			//If the pick method picks a random value
			else 
				//Pick a random value in the zList
				b = getRandElem(zList);

			//Get the row and column indices of the current cell
			int cellRow = b.r;
			int cellCol = b.c;

			//If the maze is a hex maze
			if(maze.type == Maze.HEX)
				//Decrement column to be in the 0 to C-1  range
				cellCol -= (cellRow + 1) / 2;

			//Set the current cell as visited
			visited[cellRow][cellCol] = true;

			//Get the unvisited neighbours of the current cell
			ArrayList<Cell> unvisitedNeighbours = getUnvisitedNeighbours(maze, b, visited);

			//If the cell has unvisited neighbours
			if(unvisitedNeighbours.size() != 0) {
				//Pick a random unvisited neighbour
				Cell randNeighbour = getRandElem(unvisitedNeighbours);
				//Carve a path from b to its random unvisited neighbour
				carvePath(maze, b, randNeighbour);
				//Add the neighbour to the Z list
				zList.add(randNeighbour);

				//Get the row and column indices of the random neighbour
				cellRow = randNeighbour.r;
				cellCol = randNeighbour.c;

				//If the maze is a hex maze
				if(maze.type == Maze.HEX)
					//Decrement column to be in the 0 to C-1  range
					cellCol -= (cellRow + 1) / 2;

				//Set the random neighbour as visited
				visited[cellRow][cellCol] = true;
			}else {
				//If B has no unvisited neighbours, remove it from the zList
				zList.remove(b);
			}

		}while(zList.size() != 0);
	}

	private int determinePickMethod(double threshold) {
		//Create a random number generator
		Random rnd = new Random();
		//If the random number is above the threshold, pick the newest item,
		//Otherwise pick a random item
		return rnd.nextDouble() > threshold ? PICK_NEWEST : PICK_RANDOM;
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
}
