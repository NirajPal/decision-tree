
import java.util.TreeSet;

public class DT {
	@SuppressWarnings("unused")
	public static void main(String args[]) {

		System.out.println("Decision Tree");
		if (args.length == 3) {

			String trainingData = args[0];
			String testData = args[1];
			double pruningFactor = Double.parseDouble(args[2]);

			Node node1 = new Node();
			DecisionTree.loadData(trainingData, node1);

			// entropy calculation and information gain
			DecisionTree.buildTree(node1);
			DecisionTree.printTreeX(node1, 0);

			Results prePrunedResult = new Results();
			DecisionTree.testTree(node1, prePrunedResult, trainingData, "train");
			DecisionTree.testTree(node1, prePrunedResult, testData, "test");
			DecisionTree.printAnalysis(prePrunedResult, "ID3 Analysis");
			
			/*
			TreeSet<Integer> pruneNodes = DecisionTree.getPruningNodes(pruningFactor, node1, prePrunedResult);
			DecisionTree.pruneTree(node1, pruneNodes);

			Results postPrunedResult = new Results();
			DecisionTree.testTree(node1, postPrunedResult, trainingData, "train");
			DecisionTree.testTree(node1, postPrunedResult, testData, "test");
			DecisionTree.printAnalysis(postPrunedResult, "Post-Pruned Analysis");
			*/

			// Random selection of nodes.
			System.out.println("");
			System.out.println("Random generated tree.");
			Node node2 = new Node();
			DecisionTree.loadData(trainingData, node2);
			DecisionTree.buildRandomTree(node2);
			DecisionTree.printTreeX(node2, 0);
			
			Results randomSelectionResults = new Results();
			DecisionTree.testTree(node2, randomSelectionResults, trainingData, "train");
			DecisionTree.testTree(node2, randomSelectionResults, testData, "test");
			DecisionTree.printAnalysis(randomSelectionResults, "Random Node Selection Analysis");

		} else {
			System.out.println("Invalid Input Set");
		}

	}
}
