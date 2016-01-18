/**
 * Author: Edrei Chua
 * Created on: 01/17/2016
 *
 * MultiMazeProblem for Maze World
 *
 * Credit: Stub provided by Balcom, Norvig and Russell Textbook on AI, piazza
 *
 * Dependencies: refer to README
 */
package assignment_mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class MultiMazeProblem extends InformedSearchProblem {

    private static int actions[][] = {Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST, Maze.STAY};

    protected int robotNum;
    private int[] robotGoal;

    private Maze maze;

    public MultiMazeProblem(Maze m, int[] robotStart, int[] robotGoal, int turn) {

        int[] robotState = new int[robotStart.length+1];

        for(int i=0; i<robotStart.length; i++){
            robotState[i] = robotStart[i];
        }

        robotState[robotStart.length] = turn;

        startNode = new MultiMazeNode(robotState,0);
        this.robotGoal = robotGoal;
        this.robotNum = robotStart.length/2;
        maze = m;
    }

    // node class used by searches.  Searches themselves are implemented
    //  in SearchProblem.
    public class MultiMazeNode implements SearchNode {

        // location of the agent in the maze
        protected int[] state;

        // how far the current node is from the start.  Not strictly required
        //  for uninformed search, but useful information for debugging,
        //  and for comparing paths
        private double cost;

        public MultiMazeNode(int[] robotState, double c) {
            state = robotState;
            cost = c;
        }

        public int getX(int turn) {
            return state[2*turn];
        }

        public int getY(int turn) {
            return state[2*turn+1];
        }

        public int getTurn(){
            return state[state.length-1];
        }

        public int getNum(){
            return robotNum;
        }


        public ArrayList<SearchNode> getSuccessors() {

            ArrayList<SearchNode> successors = new ArrayList<SearchNode>();

            for (int[] action: actions) {
                int[] stateNew  = deepCopy(state);
                int turnIndex = state[state.length-1];

                stateNew[2*turnIndex] = state[2*turnIndex] + action[0];
                stateNew[2*turnIndex+1] = state[2*turnIndex+1] + action[1];
                stateNew[state.length-1] = (turnIndex+1)%robotNum;

                if(maze.isLegal(stateNew[2*turnIndex], stateNew[2*turnIndex+1]) &&
                        !robotCollide(stateNew, stateNew[2*turnIndex], stateNew[2*turnIndex+1])){

                    SearchNode succ = new MultiMazeNode(stateNew, getCost() + 1.0);
                    successors.add(succ);
                }
            }
            return successors;
        }

        /**
         * check that the robots do not collide into each other
         */

        private boolean robotCollide(int[] stateNew, int x, int y){
            boolean flag = false;
            for(int i = 0; i<robotNum; i++){
                if(stateNew[2*i] == x && stateNew[2*i+1] == y) {
                    if (flag) return true;
                    flag = true;
                }
            }
            return false;
        }

        private int[] deepCopy(int[] array){
            int[] result = new int[array.length];

            for(int r=0; r<array.length; r++){
                result[r] = array[r];
            }
            return result;
        }

        @Override
        public boolean goalTest() {
            for(int i=0; i<robotGoal.length; i++){
                if(state[i] != robotGoal[i])
                    return false;
            }
            return true;
        }


        // an equality test is required so that visited sets in searches
        // can check for containment of states
        @Override
        public boolean equals(Object other) {
            return Arrays.equals(state, ((MultiMazeNode) other).state);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(state);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("Maze state: ");
            for(int i=0; i<robotNum; i++){
                str.append("("+state[2*i]+","+state[2*i+1]+")");
            }
            str.append(" turn: "+state[state.length-1]);
            str.append(" depth: "+getCost());
            return str.toString();
        }

        @Override
        public double getCost() {
            return cost;
        }


        @Override
        public double heuristic() {
            // sum of manhattan distance:
            double dist = 0;
            for(int i = 0; i<robotGoal.length; i++){
                dist += Math.abs(robotGoal[i] - state[i]);
            }
            return dist;
        }

        @Override
        public int compareTo(SearchNode o) {
            //return (int) Math.signum(priority() - o.priority());
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
        }

        @Override
        public double priority() {
            return heuristic() + getCost();
        }

    }
    /**
    // Use for testing
    public static void main(String[] args){
        Maze maze = Maze.readFromFile("simple.maz");
        int[] robotStart = {0,0,0,1};
        int[] robotGoal = {6,6,6,1};
        int turn = 0;

        MultiMazeProblem mazeProblem = new MultiMazeProblem(maze, robotStart, robotGoal,turn);
        List<SearchNode> bfsPath = mazeProblem.breadthFirstSearch();

    }*/

}
