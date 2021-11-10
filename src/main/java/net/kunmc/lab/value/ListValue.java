package net.kunmc.lab.value;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public abstract class ListValue<E> implements CollectionValue<List<E>, E>, Iterable<E> {
    protected List<E> value;

    public ListValue(List<E> value) {
        this.value = value;
    }

    @Override
    public List<E> value() {
        return this.value;
    }

    @Override
    public void value(List<E> value) {
        this.value = value;
    }

    @Override
    public boolean validateOnAdd(E element) {
        return true;
    }

    @Override
    public boolean validateOnRemove(E element) {
        return value.contains(element);
    }

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean contains(Object o) {
        return value.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return value.iterator();
    }

    @NotNull
    public Object[] toArray() {
        return value.toArray();
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return value.toArray(a);
    }

    public boolean add(E t) {
        return value.add(t);
    }

    public boolean remove(Object o) {
        return value.remove(o);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return value.containsAll(c);
    }

    public boolean addAll(@NotNull Collection<? extends E> c) {
        return value.addAll(c);
    }

    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return value.addAll(index, c);
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        return value.removeAll(c);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        return value.retainAll(c);
    }

    public void clear() {
        value.clear();
    }

    public E get(int index) {
        return value.get(index);
    }

    public E set(int index, E element) {
        return value.set(index, element);
    }

    public void add(int index, E element) {
        value.add(index, element);
    }

    public E remove(int index) {
        return value.remove(index);
    }

    public int indexOf(Object o) {
        return value.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return value.lastIndexOf(o);
    }

    @NotNull
    public ListIterator<E> listIterator() {
        return value.listIterator();
    }

    @NotNull
    public ListIterator<E> listIterator(int index) {
        return value.listIterator(index);
    }

    @NotNull
    public List<E> subList(int fromIndex, int toIndex) {
        return value.subList(fromIndex, toIndex);
    }

    public Stream<E> stream() {
        return value.stream();
    }

    public Spliterator<E> spliterator() {
        return value.spliterator();
    }
}
