package _02_SinglyLinkedList;

import Interface_form.List;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class SLinkedList<E> implements List<E>, Cloneable{

    private Node<E> head;   //노드의 첫 부분
    private Node<E> tail;   //노드의 끝 부분
    private int size;   //요소 개수

    // 생성자
    public SLinkedList() {

        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private Node<E> search(int index) {

        // 범위 밖(잘못된 위치)일 경우 예외처리
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> x = head;   // head 가 가리키는 노드부터 시작

        for (int i = 0; i < index; i++) {
            x = x.next;
        }
        return x;
    }

    public void addFirst(E value) {

        Node<E> newNode = new Node<E>(value);   // 새 노드 생성
        newNode.next = head;    // 새 노드의 다음 노드를 head 노드로
        head = newNode;     // head 가 가리키는 노드를 새 노드로 변경
        size++;

        // 데이터가 없을 때 새 노드는 head 인 것과 동시에 tail 이다.
        if (head.next == null) {
            tail = head;
        }
    }
    @Override
    public boolean add(E value) {

        addLast(value);
        return true;
    }

    public void addLast(E value) {

        Node<E> newNode = new Node<E>(value);

        if (size == 0) {    // 처음 넣는 노드일 경우 addFirst 로 추가
            addFirst(value);
            return;
        }

        // tail 의 다음 노드가 새 노드를 가르키도록 하고 tail 을 새 노드로 변경
        tail.next = newNode;
        tail = newNode;
        size++;
    }


    public void add(int index, E value) {

        // 잘못된 인덱스를 참조할 경우 예외 발생
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        // 추가하려는 index 가 가장 앞인 경우
        if (index == 0) {
            addFirst(value);
            return;
        }
        // 추가하려는 index 가 마지막 위치인 경우
        if (index == size) {
            addLast(value);
            return;
        }

        Node<E> newNode = new Node<E>(value);
        // 추가하려는 위치의 전 노드
        Node<E> prev_Node = search(index-1);
        // 추가하려는 위치의 노드
        Node<E> next_Node = prev_Node.next;

        // 이전 노드의 next 를 끊고 새 노드에 연결
        prev_Node.next = null;
        prev_Node.next = newNode;
        newNode.next = next_Node;
        size++;
    }

    public E remove() {

        Node<E> headNode = head;

        if (headNode == null) {
            throw new NoSuchElementException();
        }

        // 삭제된 노드를 반환하기 위한 임시 변수
        E element = headNode.data;

        // head 의 다음 노드
        Node<E> nextNode = head.next;

        // head 노드의 데이터들을 모두 삭제
        head = null;
        head.next = null;

        // head 가 다음 노드를 가리키도록 업데이트
        head = nextNode;
        size--;

        // 삭제된 요소가 마지막 요소일 경우 tail 도 삭제
        if (size == 0) {
            tail = null;
        }
        return element;
    }

    @Override
    public E remove(int index) {

        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return remove();
        }

        Node<E> preNode = search(index-1);
        Node<E> targetNode = preNode.next;
        Node<E> nextNode = targetNode.next;

        E element = targetNode.data;

        targetNode = null;
        targetNode.next = null;

        preNode.next = nextNode;

        // 삭제한 노드가 마지막 노드면 tail 을 preNode 로 변경
        if (preNode.next == null) {
            tail = preNode;
        }
        size--;
        return element;
    }

    @Override
    public boolean remove(Object value) {

        Node<E> preNode = head;
        Node<E> findNode = head;

        while (findNode != null) {
            if (findNode.data.equals(value)) {
                break;
            }
            preNode = findNode;
            findNode = findNode.next;
        }

        // 찾으려는 요소가 없을 경우
        if (findNode == null) {
            return false;
        }

        // 삭제하려는 노드가 head 일 경우
        if (findNode.equals(head)) {
            remove();
            return true;
        }
        else {
            // 이전 노드의 링크를 삭제하려는 노드의 다음 노드로 연결
            preNode.next = findNode.next;

            // 삭제한 노드가 마지막 노드인 경우
            if (preNode.next == null) {
                tail = preNode;
            }
            findNode.data = null;
            findNode.next = null;
            size--;
            return true;
        }
    }

    @Override
    public E get(int index) {

        return search(index).data;
    }

    @Override
    public void set(int index, E value) {

        Node<E> replaceNode = search(index);
        replaceNode.data = null;
        replaceNode.data = value;
    }

    @Override
    public int indexOf(Object value) {

        Node<E> x = head;
        int index = 0;

        while (x != null) {
            if (value.equals(x.data)) {
                return index;
            }
            x = x.next;
            index++;
        }

        // 찾으려는 요소가 없을 경우
        return -1;
    }

    // 요소 존재성 판별
    @Override
    public boolean contains(Object value) {

        return indexOf(value) >=0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {

        return head==null;
    }

    @Override
    public void clear() {

        Node<E> x = head;

        while (x != null) {
            Node<E> nextNode = x.next;
            x.data = null;
            x.next = null;
            x = nextNode;
        }
        head = tail = null;
        size = 0;
    }

    public Object clone() throws CloneNotSupportedException {

        @SuppressWarnings("unchecked")
        SLinkedList<? super E> clone = (SLinkedList<? super E>) super.clone();

        clone.head = null;
        clone.tail = null;
        clone.size = 0;

        Node<E> x = head;
        while (x != null) {
            clone.addLast(x.data);
            x = x.next;
        }

        return clone;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            array[index++] = (E) x.data;
        }
        return array;
    }

    @SuppressWarnings("uncheced")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Array.newInstance (컴포넌트 타입, 생성할 크기)
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.data;
        }
        return a;
    }

    public void sort() {
        /**
         *  Comparator 를 념겨주지 않는 경우 해당 객체의 Comparable 에 구현된
         *  정렬 방식을 사용한다.
         *  만약 구현되어 있지 않으면 cannot be cast to class java.lang.Comparable
         *  에러가 발생한다.
         *  만약 구현되어 있을 경우 null 을 파라미터로 넘기면
         *  Array.sort()가 객체의 compareTo 메소드에 정의된 방식대로 정렬한다.
         */
        sort(null);
    }

    public void sort(Comparator<? super E> c) {

        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);

        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            x.data = (E) a[i];
        }
    }
}
