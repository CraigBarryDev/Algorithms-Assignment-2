CC:=javac -cp  *.java
JAVA:=java
FLAGS:=-cp
BIN:=.:mazeSolver/SampleSolver.jar
SRC:=*.java

all:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_backtrack_wallfollow.para n
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_growtree_wallfollow.para n
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_prim_wallfollow.para n
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_backtrack_wallfollow.para n
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_growtree_wallfollow.para n
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_prim_wallfollow.para n

compile:
	$(CC) $(FLAGS) $(BIN) $(SRC)

hex_backtrack_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_backtrack_wallfollow.para y

hex_growtree_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_growtree_wallfollow.para y

hex_prim_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester hex_prim_wallfollow.para y

norm_backtrack_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_backtrack_wallfollow.para y

norm_growtree_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_growtree_wallfollow.para y

norm_prim_backtrack:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_prim_backtrack.para y

norm_prim_wallfollow:
	make compile
	$(JAVA) $(FLAGS) $(BIN) MazeTester norm_prim_wallfollow.para y


