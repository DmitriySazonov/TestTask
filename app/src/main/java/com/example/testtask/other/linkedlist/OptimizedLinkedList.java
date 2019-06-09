package com.example.testtask.other.linkedlist;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * Список с возможностью добавления и удаления элементов с асимпнотикой O(1)
 */
public class OptimizedLinkedList<T> implements Iterable<T> {

    private class Node {
        T value;
        Node next;
        Node prev;

        Node(T value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private class NodeMark implements Mark {
        WeakReference<Node> node;

        NodeMark(Node node) {
            this.node = new WeakReference<>(node);
        }
    }


    private int size = 0;
    private Node mHead = null;
    private Node mTail = null;

    public Mark add(T value) {
        Node tail = mTail;
        if (tail == null) {
            tail = new Node(value, null, null);
            mTail = tail;
            mHead = tail;
        } else {
            tail.next = new Node(value, null, tail);
            mTail = tail.next;
        }
        size++;
        return new NodeMark(tail);
    }

    public void remove(T value) {
        Node node = mHead;
        while (node != null && node.value != value)
            node = node.next;
        if (node != null)
            removeNode(node);
    }

    @SuppressWarnings("unchecked")
    public void remove(Mark mark) {
        if (mark instanceof OptimizedLinkedList.NodeMark) {
            Node node = ((NodeMark) mark).node.get();
            if (node != null)
                removeNode(node);
        }
    }

    public void clear() {
        mHead = null;
        mTail = null;
        size = 0;
    }

    public boolean contains(T value) {
        for (T iterableValue : this)
            if (iterableValue == value)
                return true;
        return false;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return getSize() > 0;
    }

    @Override
    @NonNull
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node node = mHead;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public T next() {
                T value = node.value;
                node = node.next;
                return value;
            }
        };
    }

    private void removeNode(@NonNull Node node) {
        if (node == mHead && node == mTail) {
            clear();
            return;
        }
        if (mHead == node) {
            mHead = mHead.next;
            if (mHead != null)
                mHead.prev = null;
        } else if (mTail == node) {
            mTail = mTail.prev;
            if (mTail != null)
                mTail.next = null;
        } else {
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
        }
        size--;
    }
}

