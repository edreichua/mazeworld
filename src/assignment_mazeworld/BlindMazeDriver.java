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

public class BlindMazeDriver extends Application {

    Maze maze;

    // instance variables used for graphical display
    private static final int PIXELS_PER_SQUARE = 32;
    MazeView mazeView;
    List<AnimationPath> animationPathList;

    // some basic initialization of the graphics; needs to be done before
    //  runSearches, so that the mazeView is available
    private void initMazeView() {
        maze = Maze.readFromFile("tricky.maz"); // empty.maz, simple.maz, rearrange.maz, tricky.maz

        animationPathList = new ArrayList<AnimationPath>();
        // build the board
        mazeView = new MazeView(maze, PIXELS_PER_SQUARE);

    }

    // assumes maze and mazeView instance variables are already available
    private void runSearches() {


         /**System.out.println("Blind Robot Maze: Empty Maze");
         BlindMazeProblem mazeProblem = new BlindMazeProblem(maze, 2, 0);*/


         /**System.out.println("Blind Robot Maze: Simple Maze");
         BlindMazeProblem mazeProblem = new BlindMazeProblem(maze, 2, 0);*/


         /**System.out.println("Blind Robot Maze: Rearrange Maze");
         BlindMazeProblem mazeProblem = new BlindMazeProblem(maze, 0, 0);*/

         System.out.println("Blind Robot Maze: Tricky Maze");
         BlindMazeProblem mazeProblem = new BlindMazeProblem(maze, 2, 0);





         /**List<SearchNode> bfsPath = mazeProblem.breadthFirstSearch();
         animationPathList.add(new AnimationPath(mazeView, bfsPath));
         System.out.println("BFS:  ");
         for(SearchNode i:bfsPath){
            System.out.print(i.toString());
         }
         System.out.print(" End\n");
         System.out.println("    Number of turns: "+bfsPath.size());
         mazeProblem.printStats();*/


        /**List<SearchNode> dfsPath = mazeProblem.depthFirstPathCheckingSearch(5000);
         animationPathList.add(new AnimationPath(mazeView, dfsPath));
         System.out.println("BFS:  ");
         for(SearchNode i:dfsPath){
         System.out.print(i.toString());
         }
         System.out.print(" End\n");
         System.out.println("    Number of turns: "+dfsPath.size());
         mazeProblem.printStats();*/

        List<SearchNode> astarPath = mazeProblem.astarSearch();
        animationPathList.add(new AnimationPath(mazeView, astarPath));
        System.out.println("A*:  ");
        for(SearchNode i:astarPath){
            System.out.print(i.toString());
        }
        System.out.print(" End\n");
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
        private Node piece;
        private List<SearchNode> searchPath;
        private int currentMove = 0;

        private int lastPosX, lastPosY;

        boolean animationDone = true;

        public AnimationPath(MazeView mazeView, List<SearchNode> path) {
            searchPath = path;
            BlindMazeProblem.BlindMazeNode firstNode = (BlindMazeProblem.BlindMazeNode) searchPath.get(0);
            piece = mazeView.addPiece(firstNode.getX(),firstNode.getY());
            lastPosX = firstNode.getX();
            lastPosY = firstNode.getY();

        }

        // try to do the next step of the animation. Do nothing if
        // the mazeView is not ready for another step.
        public void doNextMove() {

            // animationDone is an instance variable that is updated
            //  using a callback triggered when the current animation
            //  is complete
            if (currentMove < searchPath.size() && animationDone) {
                BlindMazeProblem.BlindMazeNode mazeNode = (BlindMazeProblem.BlindMazeNode) searchPath
                        .get(currentMove);
                int dx = mazeNode.getX() - lastPosX;
                int dy = mazeNode.getY() - lastPosY;
                // System.out.println("animating " + dx + " " + dy);
                animateMove(piece, dx, dy);
                lastPosX = mazeNode.getX();
                lastPosY = mazeNode.getY();
                mazeView.updateColor(mazeNode.state);
                mazeView.addFootPrint(lastPosX,lastPosY,mazeView.colors[0]);
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