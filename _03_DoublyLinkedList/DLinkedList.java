package _03_DoublyLinkedList;

import Interface_form.List;

import java.util.NoSuchElementException;

public class DLinkedList<E> implements List<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node<E> search(int index) {

        // 범위 밖 일 경우 예외 처리
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // 뒤에서 부터 검색
        if (index+1 > size / 2) {
            Node<E> x = tail;
            for (int i = size-1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }

        // 앞에서 부터 검색
        else {
            Node<E> x = head;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        }
    }

    public void addFirst(E value) {

        Node<E> newNode = new Node<E>(value);
        newNode.next = head;

        /**
         *  head 가 null 일 경우 head.prev 는 잘못된 참조가 되므로
         *  head 가 null 이 아닐 경우에만 기존 head 노드에 prev 변수가
         *  새 노드를 가리키도록 함
         */

        if (head != null) {
            head.prev = newNode;
        }

        head = newNode;
        size++;

        /**
         * 데이터가 새 노드밖에 없는 경우
         * tail = head 다.
         */
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

        if (size == 0) {
            addFirst(value);
            return;
        }

        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;
    }

    @Override
    public void add(int index, E value) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addFirst(value);
            return;
        }

        if (index == size) {
            addLast(value);
            return;
        }

        Node<E> newNode = new Node<E>(value);
        Node<E> preNode = search(index-1);
        Node<E> nextNode = preNode.next;

        preNode.next = null;
        newNode.prev = null;

        preNode.next = newNode;
        nextNode.prev = newNode;

        newNode.next = nextNode;
        newNode.prev = preNode;

        size++;
    }

    public E remove() {

        Node<E> headNode = head;

        if (headNode == null) {
            throw new NoSuchElementException();
        }

        // 삭제된 노드 반환하기 위한 임시 변수
        E element = headNode.data;

        Node<E> nextNode = headNode.next;

        // head 노드의 데이터들을 모두 삭제
        head.data = null;
        head.next = null;

        /**
         *  nextNode 가 null 이 아닐 경우에만
         *  prev 변수를 null 로 업데이트 해주어야 한다.
         *  이유는 nextNode 가 null 경우에 데이터가
         *  아무 것도 없던 상태였으므로 nextNode.prev 는
         *  잘못된 참조가 된다
         */
        if (nextNode != null) {
            nextNode.prev = null;
        }

        head = nextNode;
        size--;

        /**
         *  삭제된 요소가 유일한 요소였을 경우
         *  그 요소는 head 이자 tail 이므로
         *  삭제되면 tail 도 null 로 변환
         */
        if (size == 0) {
            tail = null;
        }

        return element;
    }

    @Override
    public E remove(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // head 노드의 삭제 분기가 있으므로 preNode 는 항상 존재한다
        if (index == 0) {
            return remove();
        }

        Node<E> preNode = search(index-1);
        Node<E> removedNode = preNode.next;
        Node<E> nextNode = removedNode.next;

        E element = removedNode.data;

        preNode.next = null;

        removedNode.data = null;
        removedNode.next = null;
        removedNode.prev = null;

        /**
         *  삭제하려는 index == size 인 경우
         *  nextNode 는 null 이기 때문에 확인 후 접근한다.
         */
        if (nextNode != null) {
            nextNode.prev = null;

            nextNode.prev = preNode;
            preNode.next = nextNode;
        }
        /**
         *  nextNode 가 null 이면 removedNode 가 tail 이므로
         *  preNode 가 tail 이 된다
         */
        else {
            tail = preNode;
        }

        size--;

        return element;
    }

    @Override
    public boolean remove(Object value) {

        Node<E> x = head;
        Node<E> preNode = head;

        // value 를 data 로 갖는 노드 찾기
        for (; x != null; x = x.next) {
            if (value.equals(x)) {
                break;
            }
            preNode = x;
        }

        // 요소가 없을 경우
        if (x == null) {
            return false;
        }

        // 삭제하려는 노드가 head 일 경우 remove()로 삭제
        if (x.equals(head)) {
            remove();
            return true;
        }
        // remove(int index) 와 같은 메커니즘
        else {
            Node<E> nextNode = x.next;

            preNode.next = null;
            x.data = null;
            x.next = null;
            x.prev = null;

            if (nextNode != null) {
                nextNode.prev = null;

                nextNode.prev = preNode;
                preNode.next = nextNode;
            }
            else {
                tail = preNode;
            }

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

        int index = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.data)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int LastIndexOf(Object value) {

        int index = size;
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (value.equals(x.data)) {
                return index;
            }
            index--;
        }
        return -1;
    }

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

        return size == 0;
    }

    @Override
    public void clear() {

        for (Node<E> x = head; x != null;) {
            Node<E> nextNode = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = nextNode;
        }
        head = tail = null;
        size = 0;
    }


}


















