
class Tree {
    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public void printTree() {
        printNode(root, 0);
    }

    private void printNode(Node node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  ");
        }

        System.out.println(indent.toString() + node.data);

        for (Node child : node.getChildren()) {
            printNode(child, level + 1);

        }
    }
}
