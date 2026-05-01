package com.cinco;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic sorted list ADT that maintains its ordering as elements are added
 *
 * @param <T>
 */
public class SortedList<T> implements Iterable<T> {

	private static final int DEFAULT_CAPACITY = 10;

	private final Comparator<T> comparator;
	private Object[] elements;
	private int size;

	/**
	 * Constructs a sorted list using the provided comparator as its fixed ordering
	 *
	 * @param comparator
	 */
	public SortedList(Comparator<T> comparator) {
		if (comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null");
		}
		this.comparator = comparator;
		this.elements = new Object[DEFAULT_CAPACITY];
		this.size = 0;
	}

	/**
	 * Adds an element at the position determined by this list's comparator
	 *
	 * @param element
	 */
	public void add(T element) {
		if (element == null) {
			throw new IllegalArgumentException("Element cannot be null");
		}
		ensureCapacity(size + 1);

		int insertionIndex = 0;
		while (insertionIndex < size && comparator.compare(element, get(insertionIndex)) > 0) {
			insertionIndex++;
		}

		for (int i = size; i > insertionIndex; i--) {
			elements[i] = elements[i - 1];
		}
		elements[insertionIndex] = element;
		size++;
	}

	/**
	 * Removes and returns the element at a given index
	 *
	 * @param index
	 * @return 
	 */
	public T remove(int index) {
		checkIndex(index);
		T removed = get(index);

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[size - 1] = null;
		size--;
		shrinkIfNeeded();

		return removed;
	}

	/**
	 * Retrieves the element at a given index
	 *
	 * @param index
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		checkIndex(index);
		return (T) elements[index];
	}

	/**
	 * @return the number of elements currently in the list
	 */
	public int size() {
		return size;
	}

	/**
	 * @return true when the list holds no elements
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes all elements from this list
	 */
	public void clear() {
		this.elements = new Object[DEFAULT_CAPACITY];
		this.size = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new SortedListIterator();
	}

	private void ensureCapacity(int requiredCapacity) {
		if (requiredCapacity <= elements.length) {
			return;
		}

		int newCapacity = elements.length * 2;
		while (newCapacity < requiredCapacity) {
			newCapacity *= 2;
		}
		resize(newCapacity);
	}

	private void shrinkIfNeeded() {
		if (elements.length <= DEFAULT_CAPACITY || size >= elements.length / 4) {
			return;
		}
		int newCapacity = Math.max(DEFAULT_CAPACITY, elements.length / 2);
		resize(newCapacity);
	}

	private void resize(int newCapacity) {
		Object[] resized = new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			resized[i] = elements[i];
		}
		elements = resized;
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
		}
	}

	private class SortedListIterator implements Iterator<T> {
		private int currentIndex = 0;

		@Override
		public boolean hasNext() {
			return currentIndex < size;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return get(currentIndex++);
		}
	}
}
