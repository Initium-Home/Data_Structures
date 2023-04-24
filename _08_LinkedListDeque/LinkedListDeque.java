package _08_LinkedListDeque;

import Interface_form.Queue;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

public class LinkedListDeque<E> implements Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedListDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean offerFirst(E value) {

        Node<E> newNode = new Node<>(value);
        newNode.next = head;

        /*
         * head 가 null 이 아닐 경우에만 기존 head 노드의 prev 변수가
         * 새 노드를 가리키도록 한다.
         */
        if (head != null) {
            head.prev = newNode;
        }

        head = newNode;
        size++;

        /*
         * 데이터가 새 노드밖에 없는 경우
         * 새 노드는 head 이자 tail 이다.
         */
        if (head.next == null) {
            tail = head;
        }
        return true;
    }

    @Override
    public boolean offer(E value) {

        return offerLast(value);
    }

    public boolean offerLast(E value) {

        // 데이터가 없을 경우 offerFirst() 실행
        if (size == 0) {
            return offerFirst(value);
        }

        Node<E> newNode = new Node<>(value);

        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;

        return true;
    }

    @Override
    public E poll() {

        return pollFirst();
    }

    public E pollFirst() {

        if (size == 0) {
            return null;
        }

        E element = head.data;

        Node<E> nextNode = head.next;

        // head 가 가리키는 데이터 삭제
        head.data = null;
        head.next = null;

        // 삭제하기 전 데이터가 두 개 이상 있을 경우
        if (nextNode != null) {
            nextNode.prev = null;
        }
        head = null;
        head = nextNode;
        size--;

        // 삭제 된 요소가 유일한 요소일 경우
        if (size == 0) {
            tail = null;
        }
        return element;
    }

    public E remove() {

        return pollFirst();
    }

    public E removeFirst() {

        E element = poll();

        if (element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }

    public E pollLast() {

        if (size == 0) {
            return null;
        }

        E element = tail.data;

        Node<E> prevNode = tail.prev;

        // tail 이 가리키는 데이터 삭제
        tail.data = null;
        tail.prev = null;

        // 삭제하기 전 데이터가 두개 이상 있을 경우
        if (prevNode != null) {
            prevNode.next = null;
        }
        tail = null;
        tail = prevNode;
        size--;

        // 삭제 된 요소가 유일한 요소일 경우
        if (size == 0) {
            head = null;
        }
        return element;
    }

    public E removeLast() {

        E element = pollLast();

        if (element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }

    @Override
    public E peek() {

        return peekFirst();
    }

    public E peekFirst() {

        if (size == 0) {
            return null;
        }
        return head.data;
    }

    public E peekLast() {

        if (size == 0) {
            return null;
        }
        return tail.data;
    }

    public E element() {

        return getFirst();
    }

    public E getFirst() {

        if (size == 0) {
            return null;
        }
        return head.data;
    }

    public E getLast() {

        if (size == 0) {
            return null;
        }
        return tail.data;
    }

    public int size() {

        return size;
    }

    public boolean isEmpty() {

        return size == 0;
    }

    public boolean contains(Object value) {

        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.data)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {

        for (Node<E> x = head; x != null ;) {
            Node<E> next = x.next;

            x.data = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        size = 0;
        head = tail = null;
    }

    public Object[] toArray() {

        Object[] array = new Object[size];
        int idx = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            array[idx++] = (E) x.data;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        if (a.length < size) {
            // Array.newInstance(컴포넌트 타입, 생성할 크기)
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.data;
        }
        return a;
    }

    public Object clone() throws CloneNotSupportedException {

        @SuppressWarnings("unchecked")
        LinkedListDeque<E> clone = (LinkedListDeque<E>) super.clone();
        clone.head = null;
        clone.tail = null;
        clone.size = 0;

        // 내부까지 복사하기 위해 내부 테이터들을 모두 복사
        for (Node<E> x = head; x != null; x = x.next) {
            clone.offerLast(x.data);
        }
        return clone;
    }
}