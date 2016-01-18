/**
 * Author: Edrei Chua
 * Created on: 01/17/2016
 *
 * InformedSearchProblem for Maze World
 *
 * Credit: Stub provided by Balcom, Norvig and Russell Textbook on AI, piazza
 *
 * Dependencies: refer to README
 */
package assignment_mazeworld;

import java.util.*;

public class InformedSearchProblem extends SearchProblem {
	
	public List<SearchNode> astarSearch() {
		
		resetStats();

		// priority queue to keep track of nodes to visit
		PriorityQueue<SearchNode> frontier = new PriorityQueue<>();
		frontier.add(startNode);

		// map to store priority number
		HashMap<SearchNode,Double> frontierMap = new HashMap<>();
		frontierMap.put(startNode,startNode.priority());

		// map to store backchaining information
		HashMap<SearchNode, SearchNode> reachedFrom = new HashMap<>();
		reachedFrom.put(startNode, null);


		while(!frontier.isEmpty()){

			SearchNode currentNode = frontier.poll();

			if(currentNode.goalTest()){
				return backchain(currentNode,reachedFrom);
			}else if(frontierMap.containsKey(currentNode) && currentNode.priority()>frontierMap.get(currentNode)){
				continue; // discard the node
			}

			// update stats
			incrementNodeCount();
			updateMemory(frontier.size() + frontierMap.size() + reachedFrom.size());

			for(SearchNode child: currentNode.getSuccessors()){
				if(!frontierMap.containsKey(child) || child.priority() < frontierMap.get(child)){
					reachedFrom.put(child, currentNode);
					frontier.add(child);
					frontierMap.put(child, child.priority());
				}
			}

		}

		return null;
	}
	/**
	// Use for testing
	public static void main(String[] args){
		int sx = 0;
		int sy = 0;
		int gx = 6;
		int gy = 0;
		Maze maze = Maze.readFromFile("simple.maz");

		SimpleMazeProblem mazeProblem = new SimpleMazeProblem(maze, sx, sy, gx,
				gy);
		List<SearchNode> astarPath = mazeProblem.astarSearch();

		for(SearchNode node: astarPath){
			node.toString();
		}
		mazeProblem.printStats();
	}*/
	
	
}
