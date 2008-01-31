package com.nraynaud.sport.data;

public interface PaginatedCollection<T> extends Iterable<T> {
    public boolean hasPrevious();

    public boolean hasNext();

    /**
     * previous PAGE index
     */
    public int getPreviousIndex();

    /**
     * next PAGE index
     */
    public int getNextIndex();

    public boolean isEmpty();
}
