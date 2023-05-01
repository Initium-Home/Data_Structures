package _10_PriorityQueue;

import Interface_form.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueue<E> implements Queue<E>, Cloneable {

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10; // 기본 용적

    private int size;   // 요소 개수
    private Object[] array; // 요소를 담을 배열

    // 생성자 1 (초기 공간 할당 x)
    public PriorityQueue() {
        this(null);
    }

    public PriorityQueue(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    // 생성자 2 (초기 공간 할당 o)
    public PriorityQueue(int capacity) {

        this(capacity, null);
    }

    public PriorityQueue(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    // 받은 인덱스의 부모 노드 인덱스를 반환
    private int getParent(int index) {

        return index / 2;
    }

    // 받은 인덱스의 왼쪽 자식 노드 인덱스를 반환
    private int getLeftChild(int index) {

        return index * 2;
    }

    // 받은 인덱스의 오른쪽 자식 노드 인덱스를 반환
    private int getRightChild(int index) {

        return index * 2 + 1;
    }

    /**
     * @param newCapacity 새로운 용적 크기
     */
    private void resize(int newCapacity) {

        // 새로 만들 배열
        Object[] newArray = new Object[newCapacity];

        // 새 배열에 기존에 있던 배열의 요소들을 모두 복사해준다.
        for (int i = 1; i <= size; i++) {
            newArray[i] = array[i];
        }

        /*
         * 현재 배열은 GC 처리를 위해 null 값을 입력해 명시한 뒤
         * 새 배열을 array 에 연결해 준다.
         */
        this.array = null;
        this.array = newArray;
    }

    @Override
    public boolean offer(E value) {
        resize(array.length * 2);

        // 배열 용적이 꽈거 차있을 경우 용적을 두 배로 늘려준다.
        if (size + 1 == array.length) {
        }

        siftUp(size + 1, value);    // 가장 마지막에 추가되는 위치와 넣을 값을 넘겨줌
        size++;
        return true;
    }

    /**
     * 상향 선별
     *
     * @param idx   추가 할 노드의 인덱스
     * @param target    재배치 할 노드
     */
    private void siftUp(int idx, E target) {

        // comparator 가 있으면 넘겨주고, 없으면 Comparable 로 비교함
        if (comparator != null) {
            siftUpComparator(idx, target, comparator);
        }
        else {
            siftUpComparable(idx, target);
        }

    }

    @SuppressWarnings("unchecked")
    private void siftUpComparator(int idx, E target, Comparator<? super E> comp) {

        // root 노드보다 클 동안만 탐색
        while (idx > 1) {
            int parent = getParent(idx);    // 삽입노드의 부모노드 인덱스

            Object parentVal = array[parent];   // 부모노드의 값

            // 타겟 노드 값이 부모노드보다 크면 반복문 종료
            if (comp.compare(target, (E) parentVal) >= 0) {
                break;
            }

            /*
             * 부모노드가 타겟노드보다 우선순위가 크므로
             * 현재 삽입 될 위치에 부모노드 값으로 교체해주고
             * 타겟 노드의 위치를 부모노드의 위치로 변경해준다.
             */
            array[idx] = parentVal;
            idx = parent;
        }
        // 최종적으로 삽입 될 위치에 타겟 노드 요소를 저장
        array[idx] = target;
    }

    // 삽입 할 객체의 Comparable 을 이용한 sift-up
    @SuppressWarnings("unchecked")
    private void siftUpComparable(int idx, E target) {

        // 타겟노드가 비교 될 수 있도록 하는 변수를 만든다
        Comparable<? super E> comp = (Comparable<? super E>) target;

        // 노드 재배치 과정은 siftUpComparator 와 같음
        while (idx > 1) {
            int parent = getParent(idx);

            Object parentVal = array[parent];

            if (comp.compareTo((E) parentVal) >= 0) {
                break;
            }

            array[idx] = parentVal;
            idx = parent;
        }
    }

    @Override
    public E poll() {

        // poll 은 뽑으려는 요소(root)가 null 이면 null 을 반환한다.
        if (array[1] == null) {
            return null;
        }
        // 그 외의 경우 remove()에서 반환되는 요소를 반환한다.
        return remove();
    }

    @SuppressWarnings("unchecked")
    public E remove() {

        // 요소가 없으면 예외처리
        if (array[1] == null) {
            throw new NoSuchElementException();
        }

        E result = (E) array[1];    // 삭제되는 요소 반환을 위한 임시 변수
        E target = (E) array[size]; // 타겟이 되는 요소

        array[size] = null; // 타겟 노드의 index 를 비운다
        size--;
        siftDown(1, target);

        return result;
    }

    /**
     * 하향 선별
     *
     * @param idx   삭제 할 노드의 인덱스
     * @param target    재배치 할 노드
     */
    private void siftDown(int idx, E target) {

        if (comparator != null) {
            siftDownComparator(idx, target, comparator);
        }
        else  {
            siftDownComparable(idx, target);
        }
    }

    // Comparator 을 이용한 sift-down
    @SuppressWarnings("unchecked")
    public void siftDownComparator(int idx, E target, Comparator<? super E> comp) {

        array[idx] = null;  // 삭제 할 인덱스의 노드를 삭제

        int parent = idx;   // 삭제 노드부터 시작 할 부모 노드 인덱스를 가리키는 변수
        int child;  // 교환 될 자식 인덱스를 가리키는 변수

        // 왼쪽 자식 노드의 인덱스가 요소의 개수보다 작을 때까지 반복
        while ((child = getLeftChild(parent)) <= size) {

            int right = getRightChild(parent);  // 오른쪽 자식 인덱스
            Object childVal = array[child]; // 왼쪽 자식의 값 (교환 될 요소)

            /*
             * 오른쪽 자식의 인덱스가 size 를 넘지 않으면서
             * 왼쪽 자식이 오른쪽 자식보다 큰 경우
             * 재배치할 노드는 작은 자식과 비교해야 하므로
             * child 와 childVal 을 오른쪽 자식으로 바꾸어야 한다
             */
            if (right <= size && comp.compare((E) childVal, (E) array[right]) > 0) {
                child = right;
                childVal = array[child];
            }

            // 재배치 할 노드가 자식 노드보다 작을 경우 반복문을 종료
            if (comp.compare(target, (E) childVal) <= 0) {
                break;
            }

            /*
             * 현재 부모 인덱스를 자식 노드 값으로 대체하고
             * 부모 인덱스를 작식 인덱스로 교체
             */
            array[parent] = childVal;
            parent = child;
        }

        // 최종적으로 재배치 되는 위치에 타겟 을 넣어준다
        array[parent] = target;

        /*
         * 용적의 사이즈가 최소용적보다 크면서 요소의 개수가 전체 용적의 1/4 미만일 경우
         * 용적을 반으로 줄인다 (단, 최소용적보단 커야 함)
         */
        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(array.length / 2, DEFAULT_CAPACITY));
        }
    }

    // Comparable 을 이용한 sift-down
    @SuppressWarnings("unchecked")
    private void siftDownComparable(int idx, E target) {

        Comparable<? super E> comp = (Comparable<? super E>) target;

        array[idx] = null;

        int parent = idx;
        int child;


        while ((child = (parent << 1)) <= size) {

            int right = getRightChild(parent);
            Object childVal = array[child];

            if (right <= size && ((Comparable<? super E>) childVal).compareTo((E) array[right]) > 0) {
                child = right;
                childVal = array[child];
            }

            if (comp.compareTo((E) childVal) <= 0) {
                break;
            }

            array[parent] = childVal;
            parent = child;
        }
        array[parent] = comp;

        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(array.length / 2, DEFAULT_CAPACITY));
        }
    }

    public int size() {

        return this.size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {

        if (array[1] == null) { // root 요소가 null 이면 예외처리
            throw new NoSuchElementException();
        }
        return (E) array[1];
    }

    public boolean isEmpty() {

        return size == 0;
    }

    public boolean contains(Object value) {

        for (int i = 1; i < size; i++) {
            if (array[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {

        for (int i = 1; i < array.length; i++) {
            array[i] = null;
        }

        size = 0;
    }

    public Object[] toArray() {

        return toArray(new Object[size]);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        if (a.length <= size) {
            return (T[]) Arrays.copyOfRange(array, 1, size + 1, a.getClass());
        }
        System.arraycopy(array, 1, a, 0, size);
        return a;
    }

    @Override
    // super.clone()은 CloneNotSupportedException 예외 처리를 해야 함
    public Object clone() throws CloneNotSupportedException {

        PriorityQueue<?> cloneHeap = (PriorityQueue<?>) super.clone();

        cloneHeap.array = new Object[size + 1];

        // 깊은 복사를 위해 내부 데이터들 하나하나 넣어줌
        System.arraycopy(array, 0, cloneHeap.array, 0, size+1);
        return cloneHeap;
    }
}