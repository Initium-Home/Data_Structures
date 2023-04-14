package _01_ArrayList;


import Interface_form.List;

import java.util.Arrays;

public class ArrayList<E> implements List<E>, Cloneable {

    private static final int DEFAULT_CAPACITY = 10;	// 최소(기본) 용적 크기
    private static final Object[] EMPTY_ARRAY = {};	// 빈 배열

    private int size;	// 요소 개수

    Object[] array;	// 요소를 담을 배열

    // 생성자1 (초기 공간 할당 X)
    public ArrayList() {

        this.array = EMPTY_ARRAY;
        this.size = 0;
    }

    // 생성자2 (초기 공간 할당 O)
    public ArrayList(int capacity) {

        this.array = new Object[capacity];
        this.size = 0;
    }
    private void resize() {

        int array_capacity = array.length;

        // array의 capacity가 0인 경우
        if (Arrays.equals(array, EMPTY_ARRAY)) {
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        // 용량이 꽉 찰 경우
        if (size == array_capacity) {
            int new_capacity = array_capacity * 2;
            // copy
            array = Arrays.copyOf(array, new_capacity);
            return;
        }

        // 용적의 절반 미만으로 요소가 차지하고 있을 경우
        if (size < (array_capacity / 2)) {
            int new_capacity = array_capacity / 2;
            array = Arrays.copyOf(array, Math.max(new_capacity, DEFAULT_CAPACITY));
            return;
        }
    }

    @Override
    public boolean add(E value) {

         // 꽉 차있으면 용적 재할당
        if (size == array.length) {
            resize();
        }
        array[size] = value;    // 마지막 위치에 요소 추가
        size++;
        return true;
    }


    @Override
    public void add(int index, E value) {

        if (index > size || index < 0) {    // 영역을 벗어날 경우 예외 발생
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {    // index가 마지막 위치라면 addLast 메소드로 요소추가
            add(value);
        } else {

            if (size == array.length) { // 꽉 차있으면 용적 재할당
                resize();
            }

            for (int i = size; i > index; i--) {    // index 뒤에 요소 한칸씩 뒤로

                array[i] = array[i-1];
            }

            array[index] = value;   // index에 요소 할당
            size++;
        }
    }


    @SuppressWarnings("unchecked")  //E타입으로 반환이 되어야 하는데 반환되는 타입이 E타입으로
                                    //캐스팅이 안되는 경우가 없으므로 타입 안정성에 대한 경고가 필요없음
    @Override
    public E get(int index) {

        if (index >= size || index < 0) {   // 범위 벗어날 경우 예외 발생
            throw  new IndexOutOfBoundsException();
        }
        return (E) array[index];    // Object 타입에서 E타입으로 캐스팅 후 반환
    }

    @Override
    public void set(int index, E value) {

        if (index >= size || index < 0) {   // 범위를 벗어날 경우 예외 발생
            throw new IndexOutOfBoundsException();
        } else {
            array[index] = value;   // 해당 위치의 요소를 교체
        }
    }

    @Override
    public int indexOf(Object value) {

        // value와 같은 객체(요소 값)일 경우 i(위치) 반환
        for (int i = 0; i < size; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;  // 일치하는 것이 없을 경우 -1을 반환
    }

    public int lastIndexOf(Object value) {

        for (int i = size -1; i >= 0; i--) {
            if (array[i].equals((value))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object value) {

        if (indexOf(value) >= 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {

        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        E element = (E) array[index];   //삭제될 요소를 반환하려 담아둠
        array[index] = null;

        //삭제한 요소의 뒤에 있는 모든 요소들을 한칸씩 당겨옴
        for (int i = index; i < size; i++) {
            array[i] = array[i+1];
            array[i+1] = null;  //이전 요소들에 대한 참조가 있는경우 GC가 정리하지 않아 메모리 누수 발생위험이 있음
        }
        return element;
    }

    @Override
    public boolean remove(Object value) {

        //삭제할 요소 찾기
        int index = indexOf(value);
        if (index == -1) { // 요소가 없는 경우
            return false;
        }

        remove(index);
        return true;
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

        //모든 공간을 null 처리
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
        resize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        // 새로운 객체 생성
        ArrayList<?> cloneList = (ArrayList<?>)super.clone();
        // 새로운 객체의 배열도 생성해주어야 함(객체는 얕은복사가 되기 때문)
        cloneList.array = new Object[size];
        // 배열의 값을 복사함
        System.arraycopy(array, 0, cloneList.array, 0, size);
        return cloneList;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // copyOf(원본 배열, 복사할 길이, Class<? extends T[]> 타입)
            return (T[]) Arrays.copyOf(array, size, a.getClass());
        }
        // 원본배열, 원본배열 시작위치, 복사할 배열, 복사할배열 시작위치, 복사할 요소 수
        System.arraycopy(array, 0, a, 0, size);
        return a;
    }
}
