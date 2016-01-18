/**
 * Author: Edrei Chua
 * Created on: 01/17/2016
 *
 * BlindMazeProblem for Maze World
 *
 * Credit: Stub provided by Balcom, Norvig and Russell Textbook on AI, piazza
 *
 * Dependencies: refer to README
 */
package assignment_mazeworld;

import java.awt.*;
import java.util.*;
import java.util.List;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class BlindMazeProblem extends InformedSearchProblem {

    private static int actions[][] = {Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST};
    private static int xoriginal,yoriginal, mostEast, mostWest, mostSouth,mostNorth;
    private Maze maze;
    private static double maxdim;

    public BlindMazeProblem(Maze m, int x, int y) {
        startNode = new BlindMazeNode(new boolean[m.height][m.width], x, y, 0, m.height*m.width, "");
        maze = m;
        xoriginal = x;
        yoriginal = y;
        mostEast = m.width - 1;
        mostWest = 0;
        mostNorth = m.height - 1;
        mostSouth = 0;
        maxdim = Math.max(m.width,m.height);
    }



    // node class used by searches.  Searches themselves are implemented
    //  in SearchProblem.
    public class BlindMazeNode implements SearchNode {

        // location of the agent in the maze
        protected boolean[][] state;

        // how far the current node is from the start.  Not strictly required
        //  for uninformed search, but useful information for debugging,
        //  and for comparing paths
        private double cost;
        private int numPossible,x,y;
        private String dir;

        public BlindMazeNode(boolean[][] state, int x, int y, double c, int num, String dir) {
            this.state = state;
            this.x = x;
            this.y = y;
            this.dir = dir;
            cost = c;
            numPossible = num;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }


        public ArrayList<SearchNode> getSuccessors() {

            ArrayList<SearchNode> successors = new ArrayList<SearchNode>();

            for (int[] action: actions) {

                int xNew = x + action[0];
                int yNew = y + action[1];

                if(maze.isLegal(xNew, yNew)) {

                    boolean[][] prevstate = deepCopy(state);
                    int xdiff = Math.abs(xNew - xoriginal);
                    int ydiff = Math.abs(yNew - yoriginal);
                    int decrement = 0;
                    SearchNode succ;

                    if(Arrays.equals(action,Maze.EAST)){

                        // previous location can't be at the eastern most state
                        if(xNew - xoriginal > 0 ) // only make changes if we are not backtracking
                            decrement = nullifyCol(mostEast - xdiff + 1, prevstate);

                        succ = new BlindMazeNode(prevstate,xNew, yNew, getCost() + 1.0,numPossible-decrement, "E");

                    }else if(Arrays.equals(action,Maze.WEST)){

                        // previous location can't be at the western most state
                        if(xNew - xoriginal < 0 ) // only make changes if we are not backtracking
                            decrement = nullifyCol(xdiff - 1,prevstate);

                        succ = new BlindMazeNode(prevstate,xNew, yNew, getCost() + 1.0,numPossible-decrement, "W");

                    }else if(Arrays.equals(action,Maze.NORTH)){

                        // previous location can't be at the northern most state
                        if(yNew - yoriginal > 0 ) // only make changes if we are not backtracking
                            decrement = nullifyRow(mostNorth - ydiff + 1, prevstate);

                        succ = new BlindMazeNode(prevstate,xNew, yNew, getCost() + 1.0,numPossible-decrement, "N");

                    }else{

                        // previous location can't be at the southern most state
                        if(yNew - yoriginal < 0 ) // only make changes if we are not backtracking
                            decrement = nullifyRow(mostSouth+ydiff-1,prevstate);

                        succ = new BlindMazeNode(prevstate,xNew, yNew, getCost() + 1.0,numPossible-decrement,"S");

                    }
                    successors.add(succ);
                }
            }
            return successors;
        }

        private boolean[][] deepCopy(boolean[][] array){
            boolean[][] result = new boolean[array.length][array[0].length];

            for(int r=0; r<array.length; r++){
                for(int c=0; c<array[0].length; c++){
                    result[r][c] = array[r][c];
                }
            }
            return result;
        }

        private int nullifyRow(int r, boolean[][] state){
            int count = 0;
            for(int i=0; i<state[0].length; i++) {
                if(!state[r][i]) {
                    state[r][i] = true;
                    count++;
                }
            }
            return count;
        }

        private int nullifyCol(int c, boolean[][] state){
            int count = 0;
            //System.out.println("c is "+c);
            for(int i=0; i<state.length; i++) {
                if (!state[i][c]) {
                    state[i][c] = true;
                    count++;
                }
            }
            return count;
        }

        @Override
        public boolean goalTest() {
            boolean flag = false;
            for(int r=0; r<state.length;r++){
                for(int c=0; c<state[0].length; c++){
                    if(!state[r][c]){
                        if(flag) return false;
                        flag = true;
                    }
                }
            }
            return true;
        }


        // an equality test is required so that visited sets in searches
        // can check for containment of states
        @Override

        public boolean equals(Object other) {
            return Arrays.equals(state, ((BlindMazeNode) other).state) && ((BlindMazeNode) other).getCost() == getCost();
        }

        public int hashCode() {
            return Arrays.hashCode(state);
        }

        @Override
        /**
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("Position: "+"("+x+","+y+") ");
            str.append("Maze state ");
            for(int r = 0; r<state.length;r++) {
                for (int c = 0; c < state[0].length; c++) {
                    if (!state[r][c]) {
                        str.append("("+c+","+r+")");
                    }
                }
            }
            str.append(" depth "+getCost());
            str.append(" move: "+dir);
            return str.toString();
        }*/

        public String toString() {
            if(dir==""){
                return "    Start -> ";
            }else{
                return dir+" -> ";
            }
        }

        @Override
        public double getCost() {
            return cost;
        }


        @Override
        public double heuristic() {
            return numPossible/maxdim;
        }

        @Override
        public int compareTo(SearchNode o) {

            if(priority() > o.priority())
                return 1;
            else if (priority() < o.priority())
                return -1;
            else{ // tie breaker
                if(heuristic() > o.heuristic())
                    return 1;
                else if(heuristic() < o.heuristic())
                    return -1;
                else
                    return 0;
            }
            //return (int) Math.signum(priority() - o.priority());
        }

        @Override
        public double priority() {
            return heuristic() + getCost();
        }

    }
    /**
    // Use for testing
    public static void main(String[] args){
        Maze maze = Maze.readFromFile("tricky.maz");
        BlindMazeProblem mazeProblem = new BlindMazeProblem(maze, 2, 0);


        List<SearchNode> bfsPath = mazeProblem.breadthFirstSearch();
        for(SearchNode i:bfsPath){
            System.out.print(i.toString());
        }
        System.out.print(" End\n ");

        System.out.println("BFS:  ");
        System.out.println("    Number of turns: "+bfsPath.size());
        mazeProblem.printStats();

        List<SearchNode> astarPath = mazeProblem.astarSearch();
        for(SearchNode i:astarPath){
            System.out.print(i.toString());
        }
        System.out.print(" End\n ");

        System.out.println("A*:  ");
        System.out.println("    Number of turns: "+astarPath.size());
        mazeProblem.printStats();

    }*/


}
