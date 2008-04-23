package com.nraynaud.sport.web.view;

import com.nraynaud.sport.data.PaginatedCollection;

public class PaginationView<T> {
    public final PaginatedCollection<T> collection;
    public final String pageVariable;

    private PaginationView(final PaginatedCollection<T> collection, final String pageVariable) {
        this.collection = collection;
        this.pageVariable = pageVariable;
    }

    public static <T> PaginationView<T> view(final PaginatedCollection<T> collection, final String pageVariable) {
        return new PaginationView<T>(collection, pageVariable);
    }
}
