public class LinkedList<T> {

    private Node<T> head;
    private int size;

    // Constructor to initialize an empty linked list
    public LinkedList() {
        head = null;
        size = 0;
    }

    // Node class to represent elements in the linked list
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Add an element to the end of the linked list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Get the size of the linked list
    public int size() {
        return size;
    }

    // Check if the linked list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove the first occurrence of a given element
    public boolean remove(T data) {
        if (isEmpty()) {
            return false;
        }

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Print the linked list
    public void printList() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        list.printList(); // Output: 1 -> 2 -> 3 -> 4 -> null

        list.remove(3);
        list.printList(); // Output: 1 -> 2 -> 4 -> null
    }
}
