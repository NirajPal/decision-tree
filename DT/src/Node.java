

import java.util.List;
import java.util.Map;

public class Node {
	
	
	String data;
	Node left;
	Node right;
	Node parent;
	Map<String,List<Integer>> dataSet;
	int leafClass;
	Double informationGain;
	List<String> attributes;
	Boolean leafNode;
	int pathTaken;
	int nodeNo;
	int depth;
	
	public Node()
	{
		this.data="";
		this.left=null;
		this.right=null;
		this.leafClass=-1;
		this.leafNode=false;
		this.pathTaken = -1;
		this.nodeNo = -1;
		parent = null;
		this.depth = -1;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public Map<String, List<Integer>> getDataSet() {
		return dataSet;
	}
	public void setDataSet(Map<String, List<Integer>> dataSet) {
		this.dataSet = dataSet;
	}
	public int getLeafClass() {
		return leafClass;
	}
	public void setLeafClass(int leafClass) {
		this.leafClass = leafClass;
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public Double getInformationGain() {
		return informationGain;
	}
	public void setInformationGain(Double informationGain) {
		this.informationGain = informationGain;
	}
	public Boolean getLeafNode() {
		return leafNode;
	}
	public void setLeafNode(Boolean leafNode) {
		this.leafNode = leafNode;
	}
	public int getPathTaken() {
		return pathTaken;
	}
	public void setPathTaken(int pathTaken) {
		this.pathTaken = pathTaken;
	}
	public int getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(int nodeNo) {
		this.nodeNo = nodeNo;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	

}
