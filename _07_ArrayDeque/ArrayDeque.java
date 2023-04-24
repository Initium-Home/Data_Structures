package _07_ArrayDeque;

import Interface_form.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class ArrayDeque<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 64; // 최소 용적 크기

    private Object[] array;
    private int size;

    private int front;  // 시작 index
    private int rear;   // 마지막 요소의 index

    //생성자 1 (초기 용적 할당 없을 경우)
    public ArrayDeque() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    // 생성자 2 (초기 용적 할당 있을 경우)
    public ArrayDeque(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    private void resize(int newCapacity) {

        int arrayCapacity = array.length;

        Object[] newArray = new Object[newCapacity];

        /*
         * i = new array index
         * j = original array index
         * index 요소 개수(size)만큼 새 배열에 값 복사
         */
        for (int i = 1, j = front+1; i < size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = null;
        this.array = newArray; // 새 배열을 기존 array 의 배열로 덮음

        front = 0;
        rear = size;
    }

    @Override
    public boolean offer(E item) {

        return offerLast(item);
    }

    public boolean offerLast(E item) {

        // 용적이 가득 찼을 경우
        if ((rear + 1) % array.length == front) {
            resize(array.length * 2);
        }

        rear = (rear + 1) % array.length;

        array[rear] = item;
        size++;

        return true;
    }

    public boolean offerFirst(E item) {

        // 용적이 가득 찼을 경우
        if ((front - 1 + array.length) % array.length == rear) {
            resize(array.length * 2);
        }

        array[front] = item;    // 빈 공간에 추가하므로 추가 후 front 값 갱신
        front = (front - 1 + array.length) % array.length;
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

        front = (front + 1) % array.length;

        @SuppressWarnings("unchecked")
        E item = (E) array[front];

        array[front] = null;
        size--;

        // 용적이 최소 크기보다 크고 요소 개수가 1/4 미만일 경우
        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    public E remove() {

        return removeFirst();
    }

    public E removeFirst() {

        E item = pollFirst();

        if (item == null) {
            throw new NoSuchElementException();
        }

        return item;
    }

    public E pollLast() {

        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E item = (E) array[rear];

        rear = (rear - 1 + array.length) % array.length;
        size--;

        // 용적이 최소 크기보다 크고 요소 개수가 1/4 미만일 경우
        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    public E removeLast() {

        E item = pollLast();

        if (item == null) {
            throw new NoSuchElementException();
        }

        return item;
    }

    @Override
    public E peek() {

        return peekFirst();
    }

    public E peekFirst() {

        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E item = (E) array[(front + 1) % array.length];
        return item;
    }

    public E peekLast() {

        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E item = (E) array[rear];
        return item;
    }

    public E element() {

        return getFirst();
    }

    public E getFirst() {

        E item = peek();

        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }

    public E getLast() {

        E item = peekLast();

        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }

    public int size() {

        return size;
    }

    public boolean isEmpty() {

        return size == 0;
    }

    public boolean contains(Object value) {

        int start = (front + 1) % array.length;

        /*
         * i : 요소 개수만큼 반복
         * idx : 원소 위치임, (idx + 1) % array.length 로 갱신
         */
        for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % array.length) {
            if (array[idx].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {

        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }

        front = rear = size = 0;
    }

    public Object[] toArray() {

        return toArray(new Object[size]);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        final T[] res;

        // 들어오는 배열의 길이가 덱의 요소 개수 보다 작은 경우
        if (a.length < size) {
            // front 가 rear 보다 뒤에 있을 경우
            if (front <= rear) {
                return (T[]) Arrays.copyOfRange(array, front + 1, rear + 1, a.getClass());
            }

            // front 가 rear 앞인 경우
            res = (T[]) Arrays.copyOfRange(array, 0, size, a.getClass());
            int rearLength = array.length - 1 - front;  // 뒷 부분 요소 개수

            // 뒷 부분 복사
            if (rearLength > 0) {
                System.arraycopy(array, front + 1, res, 0, rearLength);
            }
            // 앞 부분 복사
            System.arraycopy(array, 0, res, rearLength, rear + 1);
        }

        // 들어오는 배열의 길이가 덱의 요소 개수 보다 큰은 경우
        else {
            // front 가 rear 보다 뒤에 있을 경우
            if (front <= rear) {
                System.arraycopy(array, front + 1, a, 0, size);
            }

            // front 가 rear 보다 앞에 있을 경우
            int rearLength = array.length - 1 - front;  // 뒷 부분 요소 개수

            // 뒷 부분 복사
            if (rearLength > 0) {
                System.arraycopy(array, front + 1, a, 0, rearLength);
            }
            // 앞 부분 복사
            System.arraycopy(array, 0, a, rearLength, rear + 1);
        }

        return a;
    }

    @Override
    // super.clone()은 CloneNotSupportedException 예외를 처리해 주어해 한다
    public Object clone() throws CloneNotSupportedException {

        @SuppressWarnings("unchecked")
        ArrayDeque<E> clone = (ArrayDeque<E>) super.clone(); // 새로운 덱 객체 생성

        // 새로운 덱의 배열도 생성해 주어야 함. (깊은 복사를 위해서)
        clone.array = Arrays.copyOf(array, array.length);
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

        // null 접근 방지를 위해 toArray 로 요소만 있는 배열을 얻어 이를 정렬한 뒤 덮어 씌운다
        Object[] res = toArray();

        Arrays.sort((E[]) res, 0, size, c);

        clear();
        /*
         * 정렬된 원소를 다시 array 에 0부터 차례대로 채운다.
         * 이 때 front = 0 index 는 비워야 하므로 1번째 index 부터 채워야 한다
         */
        System.arraycopy(res, 0, array, 1, res.length);

        this.rear = this.size = res.length;
    }
}


















