package _11_HashSet;

public class Node<E> {

    /*
     * hash 와 key 값은 변하지 않으므로
     * final 로 선언해준다.
     */
    final int hash;
    final E key;
    Node<E> next;

    public Node(int hash, E key, Node<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;
    }
}
