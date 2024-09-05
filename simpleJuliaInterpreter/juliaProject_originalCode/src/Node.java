import java.util.*;
class Node {
    public String data;
    private String token;
    private Node parent;
    private List<Node> children;

    public Node(String data) {
        this.data = data;
        this.token = null;
        this.children = new ArrayList<>();
    }


    public void setToken(String token) {
        this.token = token;
    }

    public void addChild(Node node) {
        Node child = node;
        child.setParent(this);
        children.add(child);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }



}