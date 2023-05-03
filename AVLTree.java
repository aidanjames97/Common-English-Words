import java.util.*;

// An AVL tree implementation
public class AVLTree<Key extends Comparable<Key>, Value> {
    private Node root;  // root of the AVL tree

    // initializing linked lists
    List<Map.Entry<String, Integer>> listValues = new LinkedList<>();

    // A node of the AVL tree
    private class Node {
        private Key key;           // key of the node
        private Value value;       // value of the node
        private Node left, right;  // left and right subtrees of the node
        private int height;        // height of the node

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    // Returns the height of the node (or 0 if node is null)
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Returns the balance factor of the node
    private int balanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    // Rotates the node to the left
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return y;
    }

    // Rotates the node to the right
    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return y;
    }

    // Getter for tree root
    public Node getRoot() {
        return root;
    }

    // Getter for node left
    public Node getLeft(Node n) {
        return n.left;
    }

    // Getter for ndoe right
    public Node getRight(Node n) {
        return n.right;
    }

    // Balances the subtree rooted at the given node
    private Node balance(Node node) {
        if (node == null) {
            return null;
        }
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        else if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }
        else {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
        return node;
    }

    // Returns the value associated with the given key
    public Value get(Key key) {
        Node node = get(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    // Used to search if a node in tree and value in null
    public boolean find(Key key) {
        boolean found = false;
        found = find(root, key);
        return found;
    }

    // returns false if node not found or null, true if found in tree
    private boolean find(Node node, Key key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return find(node.left, key);
        }
        else if (cmp > 0) {
            return find(node.right, key);
        }
        else {
            return true;
        }
    }

    // Returns the node associated with the given key
    private Node get(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        }
        else if (cmp > 0) {
            return get(node.right, key);
        }
        else {
            return node;
        }
    }

    // Inserts the key-value pair into the AVL tree
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    // Inserts the key-value pair into the subtree rooted at the given node
    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            return new Node(key,value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        node = balance(node);
        return node;
    }

    // Helper inorder traversal function
    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            // appending nodes string and int values to lists
            listValues.add(Map.entry((String)node.key, (Integer)node.value));
            inOrderTraversal(node.right);
        }
    }

    // Call this method from main:
    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    // Call this method to print inorder traversal
    public void inOrderPrint() {
        inOrderPrint(root);
    }

    private void inOrderPrint(Node node) {
        if (node != null) {
            inOrderPrint(node.left);
            // printing
            System.out.println(node.key + " " + node.value);
            inOrderPrint(node.right);
        }
    }

    // Getter for the lists
    public List<Map.Entry<String, Integer>> getList() {
        return listValues;
    }

    public static void main(String[] args) {
        // testing

        // key string, value int
        AVLTree<String, Integer> tree = new AVLTree<>();

        tree.put("apple", 1);
        tree.put("banana", 2);
        tree.put("orange", 3);
        tree.put("trees", 4);

        tree.put("flower", 1);
        tree.put("weed", 2);
        tree.put("grass", 3);

        System.out.println("Inorder traversal:");
        tree.inOrderTraversal();
        System.out.println("Get value at apple: " + tree.get("apple"));

        System.out.println("--- Next test ---");
    }
}