package _05_ArrayQueue;

import Interface_form.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class ArrayQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 64; // 최소 용적 크기

    private Object[] array; // 요소를 담을 배열
    private int size;   // 요소 개수
    private int front;  // 시작 인덱스를 가리키는 변수
    private int rear;   // 마지막 인덱스를 가리키는 변수

    // 생성자1 (초기 용적 할당을 안할 경우)
    public ArrayQueue() { 

        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    // 생성자2 (초기 용적 할당을 할 경우)
    public ArrayQueue(int capacity) {

        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public void resize(int newCapacity) {

        int arrayCapacity = array.length;   // 현재 용적

        Object[] newArray = new Object[newCapacity];

        /*
         * i = new array index
         * j % arrayCapacity = original array index
         * j 가 현재 용적을 넘어가지 않도록 arrayCapacity 로 나눈
         * 나머지를 index 로 함
         * index 요소 개수(size)만큼 새 배열에 값 복사
         */
        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = null;
        this.array = newArray;  // 기본 배열을 비우고 새 배열로 덮음

        front = 0;
        rear = size;
    }

    @Override
    public boolean offer(E item) {

        // 용적이 가득 찼을 경우
        if ((rear + 1)% array.length == front) {
            resize(array.length*2);
        }

        rear = (rear + 1) % array.length;   // rear 을 rear 의 다음 위치로 갱신

        array[rear] = item;
        size++;

        return true;
    }

    @Override
    public E poll() {

        if (size == 0) {
            return null;
        }

        front = (front + 1) % array.length; // front 를 한칸 옮김

        @SuppressWarnings("unchecked")
        E item = (E) array[front];  // 반환할 데이터 임시 저장

        array[front] = null;
        size--;

        // 용적이 최소 크기보다 크고 요소 개수가 1/4 미만일 경우
        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            // 최소용적 미만으로 줄이지 않음
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    public E remove() {

        E item = poll();

        if (item == null) {
            throw new NoSuchElementException();
        }

        return item;
    }

    @Override
    public E peek() {

        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E item = (E) array[(front + 1) % array.length];

        return item;
    }

    public E element() {

        E item = peek();

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
         * i : 요소 개수만큼 반복한다.
         * idx : 원소 위치로, 매 회 (idx + 1) % array.length; 의 위치로 갱신
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

        front = rear = 0;
    }

    public Object[] toArray() {
        return toArray(new Object[size]);
    }
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        final T[] res;
        // 들어오는 배열의 길이가 큐의 요소 개수보다 작은 경우 res 에 넣어 반환
        if (a.length < size) {

            // front 가 rear 보다 앞에 있거나 요소가 없을 경우
            if (front <= rear) {
                return (T[]) Arrays.copyOfRange(array, front + 1, rear + 1, a.getClass());
            }

            // front 가 rear 보다 뒤에 있을 경우

            res = (T[]) Arrays.copyOfRange(array, 0, size, a.getClass());
            int rearLength = array.length - 1 - front;  // 뒷 부분 요소 개수

            // 뒷 부분 복사
            if (rearLength > 0) {
                System.arraycopy(array, front + 1, res, 0, rearLength);
            }
            // 앞 부분 복사
            System.arraycopy(array, 0, res, rearLength, rear + 1);

            return res;
        }

        // front 가 rear 보다 앞이거나 요소가 없을 경우
        if (front <= rear) {
            System.arraycopy(array, front + 1, a, 0, size);
        }
        // front 가 rear 보다 뒤에 있을 경우
        else {
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

    public void sort(Comparator<? super E> c) {

        // null 접근 방지를 위해 toArray 로 요소만 있는 배열을 얻어 이를 정렬한 뒤 덮어 씌움
        Object[] res = toArray();

        Arrays.sort((E[]) res, 0, size, c);

        clear();
        /*
         * 정렬된 원소를 다시 array 에 0부터 차례대로 채운다.
         * 이 때 front = 0 인덱스는 비워야 하므로 1번째 인덱스부터 채워야 한다.
         */
        System.arraycopy(res, 0, array, 1, res.length);

        this.rear = this.size = res.length;
    }
}
