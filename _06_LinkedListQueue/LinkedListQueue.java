package _06_LinkedListQueue;

import Interface_form.List;
import Interface_form.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class LinkedListQueue<E> implements Queue<E>, Cloneable {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    @Override
    public boolean offer(E value) {

        Node<E> newNode = new Node<>(value);

        // 비어있을 경우
        if (size == 0) {
            head = newNode;
        }
        // 그 외 tail 의 다음 노드가 새 노드를 가리키도록 함
        else {
            tail.next = newNode;
        }
        // tail 이 가리키는 노드를 새 노드로 변경
        tail = newNode;
        size++;

        return true;
    }

    @Override
    public E poll() {

        if (size == 0) {
            return null;
        }

        // 삭제될 요소를 반환하기 위한 임시 변수
        E element = head.data;

        Node<E> nextNode = head.next;

        head.data = null;
        head.next = null;

        head = nextNode;
        size--;

        return element;
    }

    public E remove() {

        E element = poll();

        if (element == null) {
            throw new NoSuchElementException();
        }

        return element;
    }

    @Override
    public E peek() {

        if (size == 0) {
            return null;
        }

        return head.data;
    }

    public E element() {

        E element = peek();

        if (element == null) {
            throw new NoSuchElementException();
        }

        return element;
    }

    public int size() {

        return size;
    }

    public boolean isEmpty() {

        return size == 0;
    }

    public boolean contains(Object value) {

        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {

        for (Node<E> x = head; x != null; ) {
            Node<E> nextNode = x.next;
            x.data = null;
            x.next = null;
            x = nextNode;
        }
        size = 0;
        head = tail = null;
    }

    public Object[] toArray() {

        Object[] array = new Object[size];
        int idx = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            array[idx++] = x.data;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        if (a.length < size) {
            // Array.newInstance(컴포넌트 타입, 생성할 크기)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int i = 0;
        // shallow copy 를 위한 배열
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.data;
        }
        return a;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // super.clone()은 CloneNotSupportedException 예외를 처리해주어야 한다.

        @SuppressWarnings("unchecked")
        LinkedListQueue<E> clone = (LinkedListQueue<E>) super.clone();
        clone.head = null;
        clone.tail = null;
        clone.size = 0;

        // 내부까지 복사되는 것이 아니기 때문에 내부 데이터들을 모두 복사한다.
        for (Node<E> x = head; x != null; x = x.next) {
            clone.offer(x.data);
        }

        return clone;
    }

    public void sort() {

        /**
         * Comparator 를 넘겨주지 않는 경우 해당 객체의 Comparable 에
         * 구현된 정렬 방식을 확인하고
         * 구현되어 있지 않으면 cannot be cast to class java.lang.Comparable
         * 에러가 발생한다
         * 구현되어 있을 경우 null 을 파라미터로 넘기면
         * Arrays.sort() 가 객체의 compareTo 메소드에 정의된 방식대로 정렬한다.
         */
        sort(null);
    }

    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {

        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);

        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            x.data = (E) a[i++];
        }
    }

}






















