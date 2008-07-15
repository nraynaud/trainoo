package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Workout;

import javax.servlet.jsp.PageContext;
import java.util.Collection;

public class TableContent {
    public final Collection<TableSheet> sheets;

    public TableContent(final Collection<TableSheet> sheets) {
        this.sheets = sheets;
    }

    public static class TableSheet {
        public final String label;
        public final Iterable<Workout> rows;
        public final RowRenderer renderer;

        public TableSheet(final String label, final Iterable<Workout> rows, final RowRenderer renderer) {
            this.label = label;
            this.rows = rows;
            this.renderer = renderer;
        }
    }

    public interface RowRenderer {
        public void render(Workout workout, PageContext context) throws Exception;
    }
}
