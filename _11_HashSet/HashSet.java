package _11_HashSet;

import Interface_form.Set;
import java.util.Arrays;

public class HashSet<E> implements Set<E> {

    // 최소 기본 용적이며 2^n 꼴 형태가 좋다.
    private final static int DEFAULT_CAPACITY = 1 << 4;

    // 3/4 이상 채워질 경우 resize 하기 위함
    private final static float LOAD_FACTOR = 0.75f;

    Node<E>[] table;    // 요소의 정보를 담고있는 Node 를 저장할 Node 타입 배열
    private int size;   // 요소의 개수

    @SuppressWarnings("unchecked")
    public HashSet() {

        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // 보조 해시 함수 (상속 방지를 위해 private static final 선언)
    private static final int hash(Object key) {

        int hash;
        if (key == null) {
            return 0;
        }
        else {
            // hashCode()는 음수가 나올 수 있으므로 양수로 변환.
            return Math.abs(hash = key.hashCode()) ^ (hash >>> 16);
        }
    }

    public boolean add(E e) {

        // key(e)에 대해 만들어두었던 보조해시 함수의 값과 key(데이터 e)를 보낸다
        return add(hash(e), e) == null;
    }

    private E add(int hash, E key) {

        int idx = hash % table.length;

        // table[idx] 가 비어있을 경우 새 노드 생성
        if (table[idx] == null) {
            table[idx] = new Node<E>(hash, key, null);
        }

        // table[idx]에 요소가 이미 존재할 경우 (해시충돌)
        else {
            Node<E> temp = table[idx];  // 현재 위치 노드
            Node<E> prev = null;        // temp 의 이전 노드

            // 첫 노드(table[idx])부터 탐색한다.
            while (temp != null) {

                /*
                 * 현재 노드의 객체가 같은 경우
                 * 중복 허용이 안되므로 key 를 반환한다.
                 */
                if ((temp.hash == hash) && (temp.key == key || temp.key.equals(key))) {
                    return key;
                }
                prev = temp;
                temp = temp.next;   // 다음노드로 이동
            }

            // 마지막 노드에 새 노드를 연결해준다.
            prev.next = new Node<E>(hash, key, null);
        }
        size++;

        // 데이터의 개수가 현재 table 용적의 75%를 넘어가면 용적을 늘려줌
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {

        int newCapacity = table.length * 2;

        // 기존 테이블의 두배 용적으로 생성
        final Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // 기존 table 순회
        for (int i = 0; i < table.length; i++) {
            // 각 인덱스의 첫 번째 노드
            Node<E> value = table[i];

            // 해당 값이 없을 경우 다음으로
            if (value == null) {
                continue;
            }

            table[i] = null;

            Node<E> nextNode;

            // 현재 인덱스에 연결 된 노드들 순회
            while (value != null) {
                int idx = value.hash % newCapacity; // 새로운 인덱스를 구한다.

                // 해시충돌 할 경우
                if (newTable[idx] != null) {
                    Node<E> tail = newTable[idx];

                    // 가장 마지막 노드로
                    while (tail.next != null) {
                        tail = tail.next;
                    }
                    // value 가 참조하는 다음 노드와의 연결을 끊어 잘못된 참조 방지
                    nextNode = value.next;
                    value.next = null;
                    tail.next = value;
                }
                // 해시충돌 안할 경우
                else {
                    nextNode = value.next;
                    value.next = null;
                    newTable[idx] = value;
                }

                value = nextNode;   // 다음노드로 이동
            }
        }

        table = null;
        table = newTable;
    }

    @Override
    public boolean remove(Object o) {

        // null 이 아니라는 것은 노드가 삭제되었다는 의미
        return remove(hash(o), o) != null;
    }

    private Object remove(int hash, Object key) {

        int idx = hash % table.length;

        Node<E> node = table[idx];  // 비교할 대상
        Node<E> removedNode = null;
        Node<E> prev = null;

        if (node == null) {
            return null;
        }

        while (node != null) {
            // 같은 노드를 찾은 경우
            if (node.hash == hash && (node.key == key || node.key.equals(key))) {
                removedNode = node; // 삭제되는 노드 반환을 위한 임시 변수

                // 해당 노드의 이전 노드가 존재하지 않는 경우
                if (prev == null) {
                    table[idx] = node.next;
                    node = null;
                }
                else {
                    prev.next = node.next;
                    node = null;
                }
                size--;
                break;
            }
            prev = node;
            node = node.next;
        }
        return removedNode;
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
    public boolean contains(Object o) {

        int idx = hash(o) % table.length;
        Node<E> temp = table[idx];

        while (temp != null) {
            if (o == temp.key || (o != null && (o.equals(temp.key)))) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public void clear() {

        if (table != null && size > 0) {
            Arrays.fill(table, null);
            size = 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        // 만약 o 가 HashSet 이 아닌 경우
        if (!(o instanceof HashSet)) {
            return false;
        }
        HashSet<E> oSet;
        /*
         * Object 로 부터 HashSet<E>로 캐스팅이 되어야 비교가 가능하고
         * 캐스팅이 불가능하면 ClassCastException 이 발생한다
         * 그래서 캐스팅이 불가능한 경우 false 를 return 하도록 try-catch 문 작성
         */
        try {
            oSet = (HashSet<E>) o;
            if (oSet.size() != size) {
                return false;
            }

            for (int i = 0; i < oSet.table.length; i++) {
                Node<E> oTable = oSet.table[i];

                while (oTable != null) {
                    /*
                     * Capacity 가 다르면 hash 값에 따른 index 도 다르기 때문에
                     * contains 로 원소의 존재 여부를 확인한다.
                     */
                    if (!contains(oTable)) {
                        return false;
                    }
                    oTable = oTable.next;
                }
            }
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    public Object[] toArray() {

        if (table == null) {
            return null;
        }
        Object[] ret = new Object[size];
        int index = 0;

        for (int i = 0; i < table.length; i++) {

            Node<E> node = table[i];

            // 해당 인덱스에 연결 된 모든 노드를 하나씩 추가함
            while (node != null) {
                ret[index++] = node.key;
                node = node.next;
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

        Object[] copy = toArray();

        // 매개변수로 받은 배열이 copy 된 요소 개수보다 작으면 size 에 맞게 늘려주며 복사
        if (a.length < size) {
            return (T[]) Arrays.copyOf(copy, size, a.getClass());
        }

        // 그 외에는 copy 배열을 a에 0번째부터 채운다.
        System.arraycopy(copy, 0, a, 0, size);

        return a;
    }
}