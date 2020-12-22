package lark.net.rpc.client;

import lark.net.Address;

/**
 * @author cuigh
 */
public class Node {
    private Address address;
    private ClientOptions options;
    private Node next;

    public Node(Address address, ClientOptions options) {
        this.address = address;
        this.options = options;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Object invoke(String service, String method, Object[] args, Class<?> returnType) {
        return null;
    }
}
