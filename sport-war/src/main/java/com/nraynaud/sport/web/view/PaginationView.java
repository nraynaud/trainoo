package com.nraynaud.sport.web.view;

import com.nraynaud.sport.data.PaginatedCollection;

public class PaginationView<T, U> {
    public final PaginatedCollection<T> collection;
    public final String pageVariable;
    public final PaginatedCollection.Transformer<T, U> transformer;

    private PaginationView(final PaginatedCollection<T> collection, final String pageVariable,
                           final PaginatedCollection.Transformer<T, U> transformer) {
        this.collection = collection;
        this.pageVariable = pageVariable;
        this.transformer = transformer;
    }

    public static <T, U> PaginationView<T, U> view(final PaginatedCollection<T> collection, final String pageVariable,
                                                   final PaginatedCollection.Transformer<T, U> transformer) {
        return new PaginationView<T, U>(collection, pageVariable, transformer);
    }

    public static <T> PaginationView<T, PaginatedCollection<T>> view(final PaginatedCollection<T> collection,
                                                                     final String pageVariable) {
        return view(collection, pageVariable, new PaginatedCollection.Transformer<T, PaginatedCollection<T>>() {
            public PaginatedCollection<T> transform(final PaginatedCollection<T> tPaginatedCollection) {
                return tPaginatedCollection;
            }
        });
    }
}
