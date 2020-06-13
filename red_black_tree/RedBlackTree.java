/*necessary imports*/
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of generic (left-leaning) Red-Black-Binary-Search-Tree
 * @author Shubham Panchal (http://shubhampanchal.herokuapp.com)
 * */
public class RedBlackTree<T extends Comparable<T>>{
    // define colors for edges
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    // node of the rbt
    private Node root;

    // declaration of node
    private class Node{
        // each node has data to be stored
        private T key;
        // each node has pointer to its left and right child
        private Node left, right;
        // each node has size of tree rooted at this node
        private int size;
        // each node has a color of edge connecting to its parent
        private boolean color;

        // constructor for class Node
        public Node(T key, boolean color, int size){
            // update the fields
            this.key = key;
            this.color = color;
            this.size = size;
            this.left = this.right = null;
        }
    }

    // constructor for RBT
    public RedBlackTree(){

    }


    /**
     * @return the size of RBT
     * */
    public int size(){
        // size of tree is equal to the size of its root
        return size(root);
    }

    /*
    * @return true if RBT is empty
    * */
    public boolean isEmpty(){
        // tree is empty is root is null
        return this.root == null;
    }

    /**
     * @return the height of RBT if not empty; -1 otherwise
     * */
    public int height(){
        // height of tree is height of root
        return height(this.root);
    }

    /**
     * @return max element in RBT
     * @throws java.util.NoSuchElementException if tree is empty
     * */
    public T max() throws NoSuchElementException{
        // check if tree is empty
        if (isEmpty()) throw new NoSuchElementException("RBT is empty");
        // max of tree is max of root
        return max(this.root).key;
    }

    /**
     * @return min element in RBT
     * @throws java.util.NoSuchElementException if tree is empty
     * */
    public T min() throws NoSuchElementException{
        // check if tree is empty
        if (isEmpty()) throw new NoSuchElementException("RBT is empty");
        // min of tree is min of root
        return min(this.root).key;
    }

    /*
    * inserts the data into RBT - overwrites the pre-existing data
    * */
    public void put(T key){
        // insert key in root
        this.root = put(this.root, key);
        // change color of root to black
        this.root.color = BLACK;
    }

    /**
     * removes the given key from RBT
     * */
    public void remove(T key){
        // remove this key from root
        this.root = remove(this.root, key);
    }

    /**
     * removes the minimum from RBT
     * */
    public void removeMin(){
        // remove min key from root
        this.root = removeMin(this.root);
    }

    /**
     * removes the maximum from RBT
     * */
    public void removeMax(){
        // remove max key from root
        this.root = removeMax(this.root);
    }

    /**
     * @return the in-order traversal of tree as iterable
     * */
    public Iterable<T> keys(){
        // check if tree is empty
        if (isEmpty())  return new LinkedList<>();
        // return keys in range min to max
        return keys(min(), max());
    }

    /**
     * @return the keys in range [low...high] in in-order fashion
     * @throws IllegalArgumentException if either start or end is null
     * */
    private Iterable<T> keys(T low, T high) throws IllegalArgumentException{
        // test for any illegal argument
        if (low == null) throw new IllegalArgumentException("start cannot be null");
        if (high == null) throw new IllegalArgumentException("end cannot be null");
        // store keys in queue
        Queue<T> queue = new LinkedList<>();
        // store keys in queue
        keys(this.root, queue, low, high);
        // return the keys
        return queue;
    }


    /* ******************************* HELPER METHODS **********************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        return x == null ? 0 : x.size;
    }

    // standard recursive height computation as for BST
    private int height(Node x){
        return x == null ? -1 : 1 + Math.max(height(x.left), height(x.right));
    }

    // fetches the min node in tree
    private Node min(Node x){
        return x.left == null ? x : min(x.left);
    }
    // fetches the max node in tree
    private Node max(Node x){
        return x.right == null ? x : max(x.right);
    }

    // overwrites the pre-existing data (if already exist)
    private Node put(Node h, T key){
        // standard bst insertion
        if (h == null)  return new Node(key, RED, 1);
        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left  = put(h.left,  key);
        else if (cmp > 0) h.right = put(h.right, key);
        else              h.key = key;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

    // removes key from tree rooted at h
    private Node remove(Node h, T key){
        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.right = removeMin(h.right);
            }
            else h.right = remove(h.right, key);
        }
        return balance(h);
    }

    // removes the minimum from tree rooted at h
    private Node removeMin(Node h){
        if (h.left == null)
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = removeMin(h.left);
        return balance(h);
    }

    // removes the max from tree rooted at h
    private Node removeMax(Node h){
        if (isRed(h.left))
            h = rotateRight(h);
        if (h.right == null)
            return null;
        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);
        h.right = removeMax(h.right);
        return balance(h);
    }

    // adds the keys between low and high in tree rooted at x
    private void keys(Node x, Queue<T> queue, T low, T high){
        if (x == null)  return;
        int cmplow = low.compareTo(x.key);
        int cmphigh = high.compareTo(x.key);
        if (cmplow < 0) keys(x.left, queue, low, high);
        if (cmplow <= 0 && cmphigh >= 0)    queue.add(x.key);
        if (cmphigh >= 0) keys(x.right, queue, low, high);
    }


    // tester method
    public static void main(String[] args){
        // create a new RBT
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        // create an instance of Random class
        Random random = new Random();
        // insert 10 random integers
        System.out.println("Insertion order: ");
        for (int i = 0; i < 10; ++i){
            int key = random.nextInt(1000);
            rbt.put(key);
            System.out.print(key + " ");
        }
        System.out.println();
        // print the properties of rbt
        System.out.println("Size: " + rbt.size());
        System.out.println("isEmpty: " + rbt.isEmpty());
        System.out.println("Height: " + rbt.height());
        System.out.println("Min: " + rbt.min());
        System.out.println("Max: " + rbt.max());

        // print status of tree
        System.out.print("Keys: ");
        for (Integer key : rbt.keys())
            System.out.print(key + " ");
        System.out.println();

        // get the keys in range [0...100]
        Iterable<Integer> keys = rbt.keys(0, 100);
        System.out.print("Keys in range [0...100]: ");
        for (Integer key : keys)
            System.out.print(key + " ");
        System.out.println();

        // remove the max and min key
        rbt.removeMax();
        rbt.removeMin();
        // print final status of tree
        System.out.println("After removing max. and min. : ");
        for (Integer key : rbt.keys())
            System.out.print(key + " ");
        System.out.println();
    }

}