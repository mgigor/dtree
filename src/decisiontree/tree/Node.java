package decisiontree.tree;

public class Node {

	private String name;
	private String value;
	private Node node;
	
	public Node(String name, String value, Node node) {
		this.name = name;
		this.value = value;
		this.node = node;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public Node getNode() {
		return node;
	}

}
