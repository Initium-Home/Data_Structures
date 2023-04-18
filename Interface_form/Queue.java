package Interface_form;

/**
 * 자바 Queue Interface 입니다.
 * Queue 는 ArrayQueue, LinkedQueue,
 * Deque, PriorityQueue 에 의해 구현됩니다.
 *
 * @param <E> the type of elements in this Que
 */
public interface Queue<E> {

    /**
     * 큐의 가장 마지막에 요소를 추가합니다.
     *
     * @param e 큐에 추가할 요소
     * @return 큐에 요소가 정상적으로 추가되었을 때 true 반환
     */
    boolean offer(E e);

    /**
     * 큐의 첫 번째 요소를 삭제하고 삭제 된 요소를 반환
     *
     * @return 삭제된 요소 반환
     */
    E pull();

    /**
     * 큐의 첫 번째 요소를 반환합니다.
     *
     * @return 큐의 첫 번 째 요소
     */
    E peek();
}
