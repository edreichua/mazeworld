README for Mazeworld

Author: Edrei Chua
Created on: 01/17/2016

*********** DIRECTORY STRUCTURE ***********

There are a few important files in this directory:

Report (directory for report)
    mazeworld.pdf (detailed documentation of the code)
    mazeworld.tex (tex file)
src (directory for source code)
    > SimpleMazeDriver.java
    > MultiMazeDriver.java
    > BlindMazeDriver.java
    > SimpleMazeProblem.java
    > MultiMazeProblem.java
    > BlindMazeProblem.java
    > InformedSearchProblem.java
    > SearchProblem.java
    > Maze.java
    > MazeView.java

simple.maz
empty.maz
rearrange.maz
tricky.maz
large.maz

*********** HOW TO START THE DEFAULT PROGRAM ***********

To start the program, compile all the .java files.

To run the default Simple robot program, run SimpleMazeDriver (the default maze used is simple.maz, with A*, DFS and BFS)
To run the default Multi robot program, run MultiMazeDriver (the default maze used is tricky.maz, with 3 robots, with A*)
To run the default Blind robot program, run BlindMazeDriver (the default maze used is tricky.maz, with A*)

*********** ADDITIONAL FUNCTIONALITY ***********

CHANGE MAZE MAP

Note: when changing maze map to large.txt, change PIXELS_PER_SQUARE = 32 to PIXELS_PER_SQUARE = 16 in order to fit the
maze onto the screen (line 34 of SimpleMazeDriver.java or MultiMazeDriver.java or BlindMazeDriver.java)

> Simple robot
   To change the maze map for the simple robot program, change line 41 of SimpleMazeDriver.java to the desired text file
   (maze = Maze.readFromFile("simple.maz");// empty.maz, simple.maz, rearrange.maz, tricky.maz, large.maz).

   Also, comment and uncomment the relevant maze problem declaration in the runSearches method of SimpleMazeDriver.java.
   (the relevant lines are 52 - 65)

> Multi robot
   To change the maze map for the Multi robot program, change line 41 of MultiMazeDriver.java to the desired text file
   (maze = Maze.readFromFile("tricky.maz"); // empty.maz, simple.maz, rearrange.maz, tricky.maz, large.maz).

   Also, comment and uncomment the relevant maze problem declaration in the runSearches method of MultiMazeDriver.java.
   (the relevant lines are 52 - 72)

> Blind robot
   To change the maze map for the Blind robot program, change line 41 of BlindMazeDriver.java to the desired text file
   (maze = Maze.readFromFile("tricky.maz"); // empty.maz, simple.maz, rearrange.maz, tricky.maz, large.maz).

   Also, comment and uncomment the relevant maze problem declaration in the runSearches method of BlindMazeDriver.java.
   (the relevant lines are 52 - 69)

CHANGE NUMBER OF ROBOTS

> Multi robot
    To change the number of robots for the Multi robot program, change the relevant parameter declaration in the runSearches
    method of MultiMazeDriver.java (the relevant lines are 52 - 69)

    Note that the parameters robotStart and robotGoal takes the following form

    int[] robotStart = {xs_1,ys_1,xs_2,ys_2, ..., xs_n,ys_n};
    int[] robotGoal = {xg_1,yg_1,xg_2,yg_2,...,xg_n,yg_n};

    where n is the number of robots,
          xs_i is the starting x-coordinate of the i robot, ys_i is the starting y-coordinate of the i robot
          xg_i is the goal x-coordinate of the i robot, yg_i is the goal y-coordinate of the i robot


CHANGE THE TYPE OF SEARCH

> To change the type of search (A*, DFS, BFS etc), comment and uncomment the relevant search declaration in the runSearches
    method. The relevant lines for each file is given below:

    SimpleMazeDriver.java: line 68 - 85
    MultiMazeDriver.java: line 77 - 96
    BlindMazeDriver.java: line 72 - 101