package _08_LinkedListDeque;

class Node<E> {

    E data;
    Node<E> next,prev;

    Node(E data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
