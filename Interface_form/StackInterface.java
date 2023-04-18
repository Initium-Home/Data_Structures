package Interface_form;

/**
 *  자바 Stack Interface 입니다.
 *  StackInterface 는 Stack 에 의해 구현됩니다
 *
 *  @param <E> the type of elements in this Stack
 */
public interface StackInterface<E> {
    /**
     * 스택의 맨 위에 있는 요소 추가
     *
     * @param item 스택에 추가할 요소
     * @return 스택에 추가된 요소
     */
     E push(E item);

    /**
     * 스택의 맨 위에 잇는 요소를 제거하고 제거 된 요소를 반환
     *
     * @return 제거 된 요소
     */
    E pop();

    /**
     * 스택의 맨 위에 있는 요소를 제거하지 않고 반환
     *
     * @retrun 맨 위에 요소
     */
    E peek();

    /**
     * 스택의 상반 부터 특정 요소가 몇 번째 위치에 잇는지 반환
     * 중복되는 원소가 있을 경우 가장 위에 잇는 요소의 위치가 반환됨
     *
     * @param value 스택에서 위치를 찾을 요소
     * @return 스택의 상단부터 찾은 첫번째 요소의 위치를 반환
     *          요소가 없을 시 -1 반환
     */
    int search(Object value);

    /**
     * 스택의 요소 개수 반환
     *
     * @return 스택의 요소 개수 반환
     */
    int size();

    /**
     * 스택의 모든 요소 삭제
     */
    void clear();

    /**
     * 스택에 요소가 비여있는지 반환
     *
     * @return 스택의 요소가 없으면 true 아니면 false 를 반환
     */
    boolean empty();
}
