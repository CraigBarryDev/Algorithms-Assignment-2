CC:=javac -cp  *.java
JAVA:=java
FLAGS:=-cp
BIN:=.:mazeSolver/SampleSolver.jar
SRC:=*.java

all:
	$(CC) $(FLAGS) $(BIN) $(SRC)

hex_prim_wallfollow:
	make all
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_prim_wallfollow.para y

norm_prim_wallfollow:
	make all
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_prim_wallfollow.para y


