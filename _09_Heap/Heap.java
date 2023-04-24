package _09_Heap;

import java.util.Comparator;

public class Heap<E> {

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 0;  // 최소 용적 크기

    private int size;   // 요소 개수

    private Object[] array; // 요소를 담을 배열

    // 생성자 1 (초기 공간 할당 x)
    public Heap() {

        this(null);
    }

    public Heap(Comparator<? super E> comparator) {

        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    // 생성자 2 (초기 공간 할당 o)
    public Heap(int capacity) {

        this(capacity, null);
    }

    public Heap(int capacity, Comparator<? super E> comparator) {

        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    // 받은 인덱스의 부모 노드 인덱스를 반환
    private int getParent(int index) {

        return index / 2;
    }

    // 받은 인덱스의 자식 노드 인덱스를 반환
    private int getChild(int index) {

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
         * 현재 배열은 GC 처리를 위해 null 로 처리한 뒤,
         * 새 배열을 연결해 준다.
         */
        this.array = null;
        this.array = newArray;
    }

    public void add(E value) {

        // 배열 용적이 꽉 차있을 경우 용적을 두 배로 늘려준다.
        if (size + 1 == array.length) {
            resize(array.length * 2);
        }

        siftUp(size + 1, value);    // 가장 마지막에 추가 되는 위치와 넣을 값을 넘겨줌
        size++;
    }

    // 상향 선별
    /**
     * @param idx 추가할 노드의 인덱스
     * @param target 재배치 할 노드
     */
    private void siftUp(int idx, E target) {

        // comparator 가 존재할 경우 comparator 을 인자로 넣는다
        if (comparator != null) {
            siftUpComparator(idx, target, comparator);
        }
        else {
            siftUpComparable(idx, target);
        }
    }

    // Comparator 을 이용한 siftUp
    @SuppressWarnings("unchecked")
    private void siftUpComparator(int idx, E target, Comparator<? super E> comp) {

        // root 노도보다 클 때까지만 탐색한다.
        while (idx > 1) {
            int parent = getParent(idx);    // 삽입노드의 부모노드 인덱스 구하기
            Object parentVal = array[parent];   // 부모노드의 값

            // 타겟 노드 값이 부모노드보다 크면 반복문 종료
            if (comp.compare(target, (E) parentVal) >= 0) {
                break;
            }

            /*
             * 부모노드가 타겟노드보다 크므로
             * 현재 삽입 될 위치를 부모노드 값으로 교체하고
             * 타겟 노드의 위치를 부모노드의 위치로 변경해준다.
             */
            array[idx] = parentVal;
            idx = parent;
        }

        // 최종적으로 삽입될 위치에 타겟 노드 값을 저장해준다.
        array[idx] = target;
    }

    // 삽입 할 객체의 Comparable 을 이용한 siftUp
    @SuppressWarnings("unchecked")
    private void siftUpComparable(int idx, E target) {

        // 타겟노드가 비교 될 수 있도록 변수 생성
        Comparable<? super E> comp = (Comparable<? super E>) target;

        while (idx > 1) {
            int parent = getParent(idx);
            Object parentVal = array[parent];

            if (comp.compareTo((E) parentVal) >= 0) {
                break;
            }
            array[idx] = parentVal;
            idx = parent;
        }
        array[idx] = comp;
    }


}














