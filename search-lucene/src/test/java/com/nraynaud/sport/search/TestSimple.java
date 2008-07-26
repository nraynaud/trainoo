package com.nraynaud.sport.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;

public class TestSimple {
    @Test
    public void testMe() throws IOException, ParseException {
        final Analyzer analyzer = new StandardAnalyzer();

        // Store the index in memory:
        final Directory directory = new RAMDirectory();
        final IndexWriter iwriter = new IndexWriter(directory, analyzer, true);
        final Document doc = new Document();
        final String text = "This is the text to be indexed.";
        doc.add(new Field("fieldname", text, true, true, true));
        iwriter.addDocument(doc);
        iwriter.optimize();
        iwriter.close();
        // Now search the index:
        final IndexSearcher isearcher = new IndexSearcher(directory);
        // Parse a simple query that searches for "text":
        final QueryParser parser = new QueryParser("fieldname", analyzer);
        final Query query = parser.parse("text");
        final Hits hits = isearcher.search(query);
        assertEquals(1, hits.length());
        // Iterate through the results:
        for (int i = 0; i < hits.length(); i++) {
            final Document hitDoc = hits.doc(i);
            assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
        }
        isearcher.close();
        directory.close();
    }
}
