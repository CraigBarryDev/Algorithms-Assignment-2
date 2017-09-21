CC:=javac -cp  *.java
JAVA:=java
FLAGS:=-cp
BIN:=.:mazeSolver/SampleSolver.jar
SRC:=*.java

all:
	$(CC) $(FLAGS) $(BIN) $(SRC)
	$(JAVA) $(FLAGS) $(BIN) MazeTester NormalMaze.para y


