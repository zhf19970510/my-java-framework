package com.zhf.util.cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    public class Node {
        private int key;
        private int value;
        private Node next;
        private Node prev;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

    }

    private int capacity;
    private Map<Integer, Node> map;
    ;
    private Node head;
    private Node tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<Integer, Node>();
        head = new Node(0, 0);
        tail = head;
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        } else {
            Node node = map.get(key);
            swap(node);
            return node.value;
        }
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            swap(node);
        } else {
            if (map.size() >= capacity) {
                Node firstNode = this.head.next;
                if(firstNode.next != null) {
                    firstNode.next.prev = this.head;
                }
                map.remove(firstNode.key);
                this.head.next = firstNode.next;
                firstNode.next = null;
                firstNode.prev = null;
            }
        }
        Node newNode = new Node(key, value);
        map.put(key, newNode);
        this.tail.next = newNode;
        newNode.prev = this.tail;
        this.tail = newNode;
    }

    public void swap(Node node) {
        if (node != tail) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = tail;
            tail.next = node;
            tail = node;
            tail.next = null;
        }
    }

    public void print() {
        Node node = this.head;
        StringBuilder sb = new StringBuilder("{");
        while (node != null) {
            if(node.next != null) {
                sb.append(node.next.key).append("=").append(node.next.value).append(",");
            }
            node = node.next;
        }
        String str = null;
        str = sb.length() > 1 ? sb.substring(0, sb.length() - 1) : sb.toString();
        System.out.println(str + "}");
    }
}
