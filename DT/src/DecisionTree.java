import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class DecisionTree {

	static String finalClassLabel = "";
	static int nodeNo = 0;
	static List<Integer> classData = null;
	static List<Integer> leafNodes = new ArrayList<Integer>();
	static double depthSum = 0.0;
	//static 	List<String> initialAttributesLists = new ArrayList<String>();

	

	public static void loadData(String path, Node node) {

		Map<String, List<Integer>> dataSet = new LinkedHashMap<String, List<Integer>>();
		List<String> attributes = null;

		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			String[] attribute = sc.nextLine().split("\t");
			attributes = Arrays.asList(attribute);
			finalClassLabel = attribute[attribute.length - 1];
			for (int i = 0; i < attribute.length; i++) {
				List<Integer> record = new ArrayList<Integer>();
				dataSet.put(attribute[i], record);
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] dataLine = line.split("\t");
				for (int i = 0; i < dataLine.length; i++) {
					List<Integer> recordList = dataSet.get(attribute[i]);
					recordList.add(Integer.parseInt(dataLine[i]));
				}

			}
			//initialAttributesLists=attributes;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		/*
		 * for (Map.Entry<String, List<Integer>> entry : dataSet.entrySet()) {
		 * System.out.println(entry.getKey()+" : "+entry.getValue()); }
		 */
		node.setAttributes(attributes);
		node.setDataSet(dataSet);
		node.setNodeNo(++nodeNo);
	}
	
	public static void buildTree(Node node) {
		


		Map<String, List<Integer>> dataSet = node.getDataSet();
		List<String> attributes = new ArrayList(dataSet.keySet());
		attributes.remove(finalClassLabel);
		List<Integer> classValues = dataSet.get(finalClassLabel);

		if (!classValues.contains(1)) {
			node.setLeafClass(0);
			node.setLeafNode(true);
			//node.setDepth(node.getParent().getDepth()+1);
			return;
		} else if (!classValues.contains(0)) {
			node.setLeafClass(1);
			node.setLeafNode(true);
			//node.setDepth(node.getParent().getDepth()+1);
			return;
		} else if (attributes.size() == 1) {
			// node.setLeafClass(1);
			String attributeName = attributes.get(0);
			node.setData(attributeName);
			node.setLeafNode(false);
			List<Integer> values = dataSet.get(attributeName);
			int pp = 0, pn = 0, np = 0, nn = 0;
			for (int i = 0; i < values.size(); i++) {
				if (values.get(i) == 1 && classValues.get(i) == 1)
					pp++;
				else if (values.get(i) == 1 && classValues.get(i) == 0)
					pn++;
				else if (values.get(i) == 0 && classValues.get(i) == 1)
					np++;
				else if (values.get(i) == 0 && classValues.get(i) == 0)
					nn++;
			}
			if (pp > pn) {
				Node rightNode = new Node();
				rightNode.setNodeNo(++nodeNo);
				rightNode.setLeafClass(1);
				rightNode.setLeafNode(true);
				rightNode.setParent(node);
				rightNode.setDepth(node.getDepth()+1);
				node.setRight(rightNode);
				node.setPathTaken(1);
			} else {
				Node rightNode = new Node();
				rightNode.setNodeNo(++nodeNo);
				rightNode.setLeafClass(0);
				rightNode.setLeafNode(true);
				rightNode.setParent(node);
				rightNode.setDepth(node.getDepth()+1);
				node.setRight(rightNode);
				node.setPathTaken(1);
			}
			if (np > nn) {
				Node leftNode = new Node();
				leftNode.setNodeNo(++nodeNo);
				leftNode.setLeafClass(1);
				leftNode.setLeafNode(true);
				leftNode.setParent(node);
				leftNode.setDepth(node.getDepth()+1);
				node.setLeft(leftNode);
				node.setPathTaken(0);
			} else {
				Node leftNode = new Node();
				leftNode.setNodeNo(++nodeNo);
				leftNode.setLeafClass(0);
				leftNode.setLeafNode(true);
				leftNode.setParent(node);
				leftNode.setDepth(node.getDepth()+1);
				node.setLeft(leftNode);
				node.setPathTaken(0);
			}

			return;
		} else {

			String bestAttribute = getInformationGain(node);
			node.setData(bestAttribute);
			if(node.getParent()==null)
				node.setDepth(0);
			//else
			//	node.setDepth(node.getParent().getDepth()+1);
			
			List<Map<String, List<Integer>>> splittedData = splitData(node);

			Node leftChild = new Node();
			leftChild.setNodeNo(++nodeNo);
			leftChild.setParent(node);
			leftChild.setDataSet(splittedData.get(0));
			leftChild.setAttributes(new ArrayList<String>(leftChild.getDataSet().keySet()));
			leftChild.setPathTaken(0);
			leftChild.setDepth(node.getDepth()+1);

			Node rightChild = new Node();
			rightChild.setNodeNo(++nodeNo);
			rightChild.setParent(node);
			rightChild.setDataSet(splittedData.get(1));
			rightChild.setAttributes(new ArrayList<String>(rightChild.getDataSet().keySet()));
			rightChild.setPathTaken(1);
			rightChild.setDepth(node.getDepth()+1);


			node.setLeft(leftChild);
			node.setRight(rightChild);
			buildTree(leftChild);
			buildTree(rightChild);

		}

	
	}
	
	public static void buildRandomTree(Node node) {

		
		Map<String, List<Integer>> dataSet = node.getDataSet();
		List<String> attributes = new ArrayList(dataSet.keySet());
		attributes.remove(finalClassLabel);
		List<Integer> classValues = dataSet.get(finalClassLabel);
		if(classValues!=null)
		{
		if (!classValues.contains(1)) {
			node.setLeafClass(0);
			node.setLeafNode(true);
			return;
		} else if (!classValues.contains(0)) {
			node.setLeafClass(1);
			node.setLeafNode(true);
			return;
		} else if (attributes.size() == 1) {
			// node.setLeafClass(1);
			String attributeName = attributes.get(0);
			node.setData(attributeName);
			node.setLeafNode(false);
			List<Integer> values = dataSet.get(attributeName);
			int pp = 0, pn = 0, np = 0, nn = 0;
			for (int i = 0; i < values.size(); i++) {
				if (values.get(i) == 1 && classValues.get(i) == 1)
					pp++;
				else if (values.get(i) == 1 && classValues.get(i) == 0)
					pn++;
				else if (values.get(i) == 0 && classValues.get(i) == 1)
					np++;
				else if (values.get(i) == 0 && classValues.get(i) == 0)
					nn++;
			}
			if (pp > pn) {
				Node rightNode = new Node();
				rightNode.setNodeNo(++nodeNo);
				rightNode.setLeafClass(1);
				rightNode.setLeafNode(true);
				rightNode.setParent(node);
				rightNode.setDepth(node.getDepth()+1);
				node.setRight(rightNode);
				node.setPathTaken(1);
			} else {
				Node rightNode = new Node();
				rightNode.setNodeNo(++nodeNo);
				rightNode.setLeafClass(0);
				rightNode.setLeafNode(true);
				rightNode.setParent(node);
				rightNode.setDepth(node.getDepth()+1);
				node.setRight(rightNode);
				node.setPathTaken(1);
			}
			if (np > nn) {
				Node leftNode = new Node();
				leftNode.setNodeNo(++nodeNo);
				leftNode.setLeafClass(1);
				leftNode.setLeafNode(true);
				leftNode.setParent(node);
				leftNode.setDepth(node.getDepth()+1);
				node.setLeft(leftNode);
				node.setPathTaken(0);
			} else {
				Node leftNode = new Node();
				leftNode.setNodeNo(++nodeNo);
				leftNode.setLeafClass(0);
				leftNode.setLeafNode(true);
				leftNode.setParent(node);
				leftNode.setDepth(node.getDepth()+1);
				node.setLeft(leftNode);
				node.setPathTaken(0);
			}

			return;
		} else {
			
			
			int randomNodeIndex = getRandomNodeNumber(attributes.size()-1);
			String bestAttribute = attributes.get(randomNodeIndex);
			//System.out.println("Random Attribute " +bestAttribute);
			node.setData(bestAttribute);
			if(node.getParent()==null)
				node.setDepth(0);

			List<Map<String, List<Integer>>> splittedData = splitData(node);

			Node leftChild = new Node();
			leftChild.setNodeNo(++nodeNo);
			leftChild.setParent(node);
			leftChild.setDataSet(splittedData.get(0));
			leftChild.setAttributes(new ArrayList<String>(leftChild.getDataSet().keySet()));
			leftChild.setPathTaken(0);
			leftChild.setDepth(node.getDepth()+1);

			Node rightChild = new Node();
			rightChild.setNodeNo(++nodeNo);
			rightChild.setParent(node);
			rightChild.setDataSet(splittedData.get(1));
			rightChild.setAttributes(new ArrayList<String>(rightChild.getDataSet().keySet()));
			rightChild.setPathTaken(1);
			rightChild.setDepth(node.getDepth()+1);

			node.setLeft(leftChild);
			node.setRight(rightChild);
			buildRandomTree(leftChild);
			buildRandomTree(rightChild);

		}
		}

	}

	public static String getInformationGain(Node node) {
		Map<String, Double> informationGain = new LinkedHashMap<String, Double>();
		Map<String, Double> entropy = new LinkedHashMap<String, Double>();

		Map<String, List<Integer>> dataSet = node.getDataSet();
		// List<String> attributes=node.getAttributes();

		List<String> attributes = new ArrayList(dataSet.keySet());

		// Calculation of Class Entropy
		int noOfAttributes = attributes.size();
		/*
		 * int classIndex = noOfAttributes - 1;
		 * 
		 * String classLabel = attributes.get(classIndex);
		 */
		List<Integer> classValues = dataSet.get(finalClassLabel);

		Double positiveCount = 0.0, negativeCount = 0.0;
		Double n = 0.0;
		Double p = 0.0;
		Double classEntropy = 0.0;

		for (int i = 0; i < classValues.size(); i++)
			positiveCount = positiveCount + classValues.get(i);

		negativeCount = classValues.size() - positiveCount;

		n = negativeCount / classValues.size();
		p = positiveCount / classValues.size();
		classEntropy = -n * log2(n) - p * log2(p);

		Double neg = 0.0;

		// entropy.put(classLabel, classEntropy);
		// attributes.remove(classLabel);

		// Calculating Individual Entropy
		for (int i = 0; i < attributes.size(); i++) {

			// negative outcomes.
			double negative_negative = 0.0;
			double negative_positive = 0.0;
			double totalN = 0.0;
			String attribute = attributes.get(i);
			// changed............................
			if (!attribute.equals(finalClassLabel)) {
				List<Integer> attributeValues = dataSet.get(attribute);

				for (int j = 0; j < attributeValues.size(); j++) {
					// Considering only negative (0) data values
					if (attributeValues.get(j) == 0) {
						totalN++;
						if (classValues.get(j) == 0)
							negative_negative++;
						else
							negative_positive++;
					}
				}

				if (!(totalN == 0.0)) {
					n = negative_negative / totalN;
					p = negative_positive / totalN;
				}
				neg = -n * log2(n) - p * log2(p);

				// positive outcomes
				double positive_negative = 0.0;
				double positive_positive = 0.0;
				double totalP = 0.0;

				for (int j = 0; j < attributeValues.size(); j++) {
					// Considering only positive (1) data values
					if (attributeValues.get(j) == 1) {
						totalP++;
						if (classValues.get(j) == 0)
							positive_negative++;
						else
							positive_positive++;
					}
				}

				Double pn = 0.0;
				Double pp = 0.0;
				if (!(totalP == 0)) {
					pn = positive_negative / totalP;
					pp = positive_positive / totalP;
				}
				Double pos = -pn * log2(pn) - pp * log2(pp);
				Double totalp = totalP / (totalN + totalP);
				Double totaln = totalN / (totalN + totalP);
				entropy.put(attribute, totaln * neg + totalp * pos);

				Double informationGainValue = classEntropy - entropy.get(attribute);
				informationGain.put(attribute, informationGainValue);
			}

		}
		//System.out.println("Class Entropy " + classEntropy);
		//System.out.println("Entropy  " + entropy);
		//System.out.println("Information Gain " + informationGain);

		String bestAttribute = "";
		Double MAX = 0.0;
		for (int i = 0; i < informationGain.size(); i++) {

			String attribute = attributes.get(i);
			if (!attribute.equals(finalClassLabel)) {
				if (MAX <= informationGain.get(attribute)) {
					MAX = informationGain.get(attribute);
					bestAttribute = attribute;
				}
			}
		}

		node.setInformationGain(MAX);
		return bestAttribute;
	}

	public static List<Map<String, List<Integer>>> splitData(Node node) {

		String bestAttribute = node.getData();
		Map<String, List<Integer>> leftDataSet = new HashMap<String, List<Integer>>();
		Map<String, List<Integer>> rightDataSet = new HashMap<String, List<Integer>>();

		//System.out.println("SPLITTING ON " + bestAttribute);

		Map<String, List<Integer>> dataSet = node.getDataSet();
		List<String> attributes = new ArrayList(dataSet.keySet());

		List<String> newAttributeList = new ArrayList<String>();
		for (int i = 0; i < attributes.size(); i++) {

			String currentAttribute = attributes.get(i);
			if (currentAttribute != bestAttribute) {
				List<Integer> leftRecord = new ArrayList<Integer>();
				List<Integer> rightRecord = new ArrayList<Integer>();
				leftDataSet.put(currentAttribute, leftRecord);
				rightDataSet.put(currentAttribute, rightRecord);
				newAttributeList.add(currentAttribute);
			}
		}

		for (int i = 0; i < dataSet.get(bestAttribute).size(); i++) {

			List<Integer> bestAttributeValues = dataSet.get(bestAttribute);
			if (bestAttributeValues.get(i) == 0) {
				for (int j = 0; j < newAttributeList.size(); j++) {

					String attributeValue = newAttributeList.get(j);
					List<Integer> recordList = leftDataSet.get(attributeValue);
					Integer value = dataSet.get(attributeValue).get(i);
					recordList.add(value);
				}
			}
			// For Right Child (Splitting node = 1)
			else if (bestAttributeValues.get(i) == 1) {
				for (int j = 0; j < newAttributeList.size(); j++) {

					String attributeValue = newAttributeList.get(j);
					List<Integer> recordList = rightDataSet.get(attributeValue);
					Integer value = dataSet.get(attributeValue).get(i);
					recordList.add(value);
				}
			}
		}

		List<Map<String, List<Integer>>> splittedData = new ArrayList<Map<String, List<Integer>>>();
		splittedData.add(leftDataSet);
		splittedData.add(rightDataSet);
		return splittedData;
	}

	public static Double log2(Double number) {
		if (number.equals(Double.parseDouble("0.0")))
			return 0.0;
		Double result = (Math.log(number) / Math.log(2));
		// System.out.println(result);
		if (result.isNaN())
			System.out.println("-----------------------NAN-------------------------");
		return result;

	}

	public static void testTree(Node node, Results result, String path, String type) {
		double correctCount = 0.0;
		List<Map<String, Integer>> recordList = new ArrayList<Map<String, Integer>>();
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			String[] attribute = sc.nextLine().split("\t");
			String classLabel = attribute[attribute.length - 1];
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] dataLine = line.split("\t");
				Map<String, Integer> testRecords = new LinkedHashMap<String, Integer>();
				for (int i = 0; i < dataLine.length; i++) {
					testRecords.put(attribute[i], Integer.parseInt(dataLine[i]));
				}
				recordList.add(testRecords);
			}

			for (Map<String, Integer> testRecords : recordList) {

				int actualValue = (int) testRecords.get(classLabel);
				// System.out.print("Actual Value"+actualValue);
				int predictedValue = classify(node, testRecords);
				if (actualValue == predictedValue) {
					correctCount++;
				}
			}
			double totalSize = recordList.size();
			// System.out.println("correct count"+correctCount);
			double accuracy = (correctCount) / totalSize;
			// System.out.println(accuracy);
			int leafCount = getLeafCount(node);
			result.setNumberOfLeafNodes(leafCount);
			result.setNumberOfNodes(getNodeCount(node));
			getNodesCount(node, result);
			if (type.equals("train")) {
				result.setTrainingInstance(recordList.size());
				result.setTrainingAccuracy(accuracy);
				result.setTrainingAttributes(attribute.length - 1);
			} else if (type.equals("test")) {
				result.setTestingInstances(recordList.size());
				result.setTestingAccuracy(accuracy);
				result.setTestingAttributes(attribute.length - 1);
			}
			
			//Calculating average depth.
			depthSum=0.0;
			calculateAverageDepth(node);
			double averageDepth = depthSum / leafCount;
			result.setAverageDepth(averageDepth);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}
	private static void calculateAverageDepth(Node node) {
		if(node!=null)
		{
		calculateAverageDepth(node.getLeft());
		if(node.getLeafNode())
		{
			depthSum = depthSum+node.getDepth();
		}
		calculateAverageDepth(node.getRight());
		}
	}

	public static void getNodesCount(Node node, Results result) {
		result.setNumberOfLeafNodes(getLeafCount(node));
		result.setNumberOfNodes(getNodeCount(node));
	}

	public static int getNodeCount(Node node) {
			if(node!=null)
				return (getNodeCount(node.getLeft())+getNodeCount(node.getRight())+1);
			else
				return 0;
	}

	public static int getLeafCount(Node node) {
		if (node == null)
			return 0;
		else if (node.getLeft() == null && node.getRight() == null)
			return 1;
		else
			return getLeafCount(node.getLeft()) + getLeafCount(node.getRight());
	}

	public static int classify(Node node, Map<String, Integer> testData) {

		if (node.getLeafNode() != true) {
			String attribute = node.data;
			int value = (int) testData.get(attribute);
			if (value == 0) {
				node = node.left;
			} else if (value == 1) {
				node = node.right;
			}
			return classify(node, testData);

		} else {
			// System.out.println("Prediction "+node.leafClass);
			return node.leafClass;
		}

	}

	public static void printTreeX(Node node, int depth) {
		if (node != null) {
			String treeLevelPrefix = "";
			int i = 0;
			while (i < depth) {
				i++;
				treeLevelPrefix = treeLevelPrefix + "| ";
			}
			if (!node.getLeafNode()) {
				System.out.print("\n" + treeLevelPrefix + node.getData() + "= 0");
			} else {
				System.out.print(" : " + node.getLeafClass());
			}

			printTreeX(node.getLeft(), ++depth);
			if (!node.getLeafNode()) {
				System.out.print("\n" + treeLevelPrefix + node.getData() + "= 1");
			}
			printTreeX(node.getRight(), ++depth);
		}
	}

	public static List<Node> getChildNodes(Node node) {
		List<Node> nodeList = new ArrayList<Node>();

		Node leftNode = node.left;
		Node rightNode = node.right;
		nodeList.add(leftNode);
		nodeList.add(rightNode);

		return nodeList;
	}

	public static void printAnalysis(Results result, String text) {

		System.out.println();
		System.out.println("-------------------------------------------------------");
		System.out.println(text);
		System.out.println("-------------------------------------------------------");
		System.out.println("No. of Training Instances=" + result.getTrainingInstance());
		System.out.println("No. of Training attributes=" + result.getTrainingAttributes());
		System.out.println("No. of Nodes in Tree=" + result.getNumberOfNodes());
		System.out.println("No. of Leaf Nodes=" + result.getNumberOfLeafNodes());
		System.out.println("Average Depth = " + result.getAverageDepth() );
		System.out.println("Training Accuracy=" + result.getTrainingAccuracy()*100+" %");
		System.out.println(" ");
		System.out.println("No. of Testing Instances=" + result.getTestingInstances());
		System.out.println("No. of Testing attributes=" + result.getTestingAttributes());
		System.out.println("Testing Accuracy=" + result.getTestingAccuracy()*100+" %");
	}
	

	public static int getRandomNodeNumber(int max)
	{
		int min = 0;
		Random random = new Random();
		int randomNo = random.nextInt(max - min + 1) + min;
		return randomNo;
	}

	public static TreeSet <Integer> getPruningNodes(double pruningFactor, Node node, Results result) {

		// Root node cannot be pruned so starting from 2
		int min = 2;
		int max = nodeNo;
		//System.out.println("NodeNumber " + nodeNo);
		int nodesToPrune = (int) (pruningFactor * max);
		//System.out.println("NodesToprune " + nodesToPrune);
		Random random = new Random();
		getLeafNodes(node);
		
		
		TreeSet <Integer> pruningNodes = new TreeSet<Integer>();
		if(nodesToPrune < result.getNumberOfNodes()-result.getNumberOfLeafNodes()-1)
		{
			while (pruningNodes.size() < nodesToPrune) {
				int randomNo = random.nextInt(max - min + 1) + min;
				if (!pruningNodes.contains(randomNo) /*&& !leafNodes.contains(randomNo)*/)
					pruningNodes.add(randomNo);
			}
		}
		else 
		{
			System.out.println("Insufficent Nodes in tree");
		}
		return (TreeSet)pruningNodes.descendingSet();
	}

	public static void getLeafNodes(Node node) {
		if (node != null) {
			getLeafNodes(node.getLeft());
			if (node.getLeafNode())
				leafNodes.add(node.getNodeNo());
			getLeafNodes(node.getRight());
		}
	}

	public static void pruneTree(Node node, TreeSet<Integer> pruneNodes) {

		if (node != null) {
			
			//if child node has to be pruned, prune parent.
			pruneTree(node.getLeft(), pruneNodes);
			if (pruneNodes.contains(node.getNodeNo()))
			{
				if(node.getLeafNode())
				{
					Node parent = node.getParent();
					Map<String, List<Integer>> dataSet = parent.getDataSet();
					List<Integer> classValues = dataSet.get(finalClassLabel);
					int neg = 0, pos = 0;
					for (int i = 0; i < classValues.size(); i++) {
						if (classValues.get(i) == 0)
							neg++;
						else
							pos++;
					}
					if (pos > neg)
						parent.setLeafClass(1);
					else
						parent.setLeafClass(0);
					//System.out.println("Pruned"+parent.getNodeNo());
					parent.setRight(null);
					parent.setLeft(null);
					parent.setLeafNode(true);
					
				}
				else
				{
					Map<String, List<Integer>> dataSet = node.getDataSet();
					List<Integer> classValues = dataSet.get(finalClassLabel);
					int neg = 0, pos = 0;
					for (int i = 0; i < classValues.size(); i++) {
						if (classValues.get(i) == 0)
							neg++;
						else
							pos++;
					}
					if (pos > neg)
						node.setLeafClass(1);
					else
						node.setLeafClass(0);
					//System.out.println("Pruned"+node.getNodeNo());
					node.setRight(null);
					node.setLeft(null);
					node.setLeafNode(true);
				}
			}
			pruneTree(node.getRight(), pruneNodes);
		}
		
	}
}
