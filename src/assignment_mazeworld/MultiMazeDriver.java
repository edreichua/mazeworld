/**
 * Author: Edrei Chua
 * Created on: 01/17/2016
 *
 * BlindMazeDriver for Maze World
 *
 * Credit: Stub provided by Balcom, Norvig and Russell Textbook on AI, piazza
 *
 * Dependencies: refer to README
 */
package assignment_mazeworld;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import assignment_mazeworld.SearchProblem.SearchNode;
import assignment_mazeworld.SimpleMazeProblem.SimpleMazeNode;

public class MultiMazeDriver extends Application {

    Maze maze;

    // instance variables used for graphical display
    private static final int PIXELS_PER_SQUARE = 32; // change constant to 16 for large maze
    MazeView mazeView;
    List<AnimationPath> animationPathList;

    // some basic initialization of the graphics; needs to be done before
    //  runSearches, so that the mazeView is available
    private void initMazeView() {
        maze = Maze.readFromFile("tricky.maz"); // empty.maz, simple.maz, rearrange.maz, tricky.maz, large.maz

        animationPathList = new ArrayList<AnimationPath>();
        // build the board
        mazeView = new MazeView(maze, PIXELS_PER_SQUARE);

    }

    // assumes maze and mazeView instance variables are already available
    private void runSearches() {

        /**System.out.println("Multi Robot Maze: 3 robots in Empty Maze");
        int[] robotStart = {0,0,0,2,5,5};
        int[] robotGoal = {6,1,6,0,6,2};*/

        /**System.out.println("Multi Robot Maze: 3 robots in Simple Maze");
        int[] robotStart = {0,0,0,2,6,6};
        int[] robotGoal = {6,1,6,0,6,2};*/


        /**System.out.println("Multi Robot Maze: 3 robots in Rearrange Maze");
        int[] robotStart = {0,0,0,1,0,2};
        int[] robotGoal = {4,1,4,2,4,0};*/

         System.out.println("Multi Robot Maze: 3 robots in Tricky Maze");
         int[] robotStart = {2,0,2,1,2,2};
         int[] robotGoal = {2,2,2,1,2,0};


        /**System.out.println("Multi Robot Maze: 2 robots in Large Maze");
        int[] robotStart = {7,32,27,24};
        int[] robotGoal = {27,24,7,32};*/

        int turn = 0;
        MultiMazeProblem mazeProblem = new MultiMazeProblem(maze, robotStart, robotGoal,turn);


        /**List<SearchNode> bfsPath = mazeProblem.breadthFirstSearch();
        animationPathList.add(new AnimationPath(mazeView, bfsPath));
        System.out.println("BFS:  ");
        System.out.println("    Number of turns: "+bfsPath.size());
        mazeProblem.printStats();*/


        /**List<SearchNode> dfsPath = mazeProblem
                .depthFirstPathCheckingSearch(5000);
        animationPathList.add(new AnimationPath(mazeView, dfsPath));
        System.out.println("DFS:  ");
        System.out.println("    Number of turns: "+dfsPath.size());
        mazeProblem.printStats();*/

        List<SearchNode> astarPath = mazeProblem.astarSearch();
        animationPathList.add(new AnimationPath(mazeView, astarPath));
        System.out.println("A*:  ");
        System.out.println("    Number of turns: " + astarPath.size());
        mazeProblem.printStats();

    }


    public static void main(String[] args) {
        launch(args);
    }

    // javafx setup of main view window for mazeworld
    @Override
    public void start(Stage primaryStage) {

        initMazeView();

        primaryStage.setTitle("CS 76 Mazeworld");

        // add everything to a root stackpane, and then to the main window
        StackPane root = new StackPane();
        root.getChildren().add(mazeView);
        primaryStage.setScene(new Scene(root));

        primaryStage.show();

        // do the real work of the driver; run search tests
        runSearches();

        // sets mazeworld's game loop (a javafx Timeline)
        Timeline timeline = new Timeline(1.0);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(.05), new GameHandler()));
        timeline.playFromStart();

    }

    // every frame, this method gets called and tries to do the next move
    //  for each animationPath.
    private class GameHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            // System.out.println("timer fired");
            for (AnimationPath animationPath : animationPathList) {
                // note:  animationPath.doNextMove() does nothing if the
                //  previous animation is not complete.  If previous is complete,
                //  then a new animation of a piece is started.
                animationPath.doNextMove();
            }
        }
    }

    // each animation path needs to keep track of some information:
    // the underlying search path, the "piece" object used for animation,
    // etc.
    private class AnimationPath {
        private Node[] piece;
        private List<SearchNode> searchPath;
        private int currentMove = 0;

        private int[][] lastPos;

        boolean animationDone = true;

        public AnimationPath(MazeView mazeView, List<SearchNode> path) {
            searchPath = path;
            MultiMazeProblem.MultiMazeNode firstNode = (MultiMazeProblem.MultiMazeNode) searchPath.get(0);
            int len = (firstNode.state.length - 1)/2;
            piece = new Node[len];
            lastPos = new int[len][2];
            for(int i=0; i<len; i++) {
                piece[i] = mazeView.addPiece(firstNode.getX(i), firstNode.getY(i));
                lastPos[i][0] = firstNode.getX(i);
                lastPos[i][1] = firstNode.getY(i);
            }
        }

        // try to do the next step of the animation. Do nothing if
        // the mazeView is not ready for another step.
        public void doNextMove() {

            // animationDone is an instance variable that is updated
            //  using a callback triggered when the current animation
            //  is complete
            if (currentMove < searchPath.size() && animationDone) {
                MultiMazeProblem.MultiMazeNode mazeNode = (MultiMazeProblem.MultiMazeNode) searchPath
                        .get(currentMove);
                int turn = mazeNode.getTurn() - 1;
                if(turn==-1) turn = mazeNode.getNum() - 1;
                int dx = mazeNode.getX(turn) - lastPos[turn][0];
                int dy = mazeNode.getY(turn) - lastPos[turn][1];
                // System.out.println("animating " + dx + " " + dy);
                animateMove(piece[turn], dx, dy);
                lastPos[turn][0] = mazeNode.getX(turn);
                lastPos[turn][1] = mazeNode.getY(turn);
                mazeView.addFootPrint(lastPos[turn][0],lastPos[turn][1],mazeView.colors[turn]);

                currentMove++;


            }

        }

        // move the piece n by dx, dy cells
        public void animateMove(Node n, int dx, int dy) {
            animationDone = false;
            TranslateTransition tt = new TranslateTransition(
                    Duration.millis(300), n);
            tt.setByX(PIXELS_PER_SQUARE * dx);
            tt.setByY(-PIXELS_PER_SQUARE * dy);
            // set a callback to trigger when animation is finished
            tt.setOnFinished(new AnimationFinished());

            tt.play();

        }

        // when the animation is finished, set an instance variable flag
        //  that is used to see if the path is ready for the next step in the
        //  animation
        private class AnimationFinished implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                animationDone = true;
            }
        }
    }
}