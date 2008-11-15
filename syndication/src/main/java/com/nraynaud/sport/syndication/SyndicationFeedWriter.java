package com.nraynaud.sport.syndication;

import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class SyndicationFeedWriter implements FeedWriter {
    public void writeOn(final GlobalWorkoutsPageData data, final Writer writer, final String feedType) throws
            IOException {
        final SyndFeed feed = getFeed(data);
        feed.setFeedType(feedType);
        try {
            new SyndFeedOutput().output(feed, writer);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }

    private static SyndFeed getFeed(final GlobalWorkoutsPageData workoutsPageData) {
        final SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("Trainoo.com : les derniers entraînements");
        feed.setLink("http://trainoo.com/");
        feed.setDescription("Les derniers entraînements sur Trainoo.com");
        final List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (final Workout workout : workoutsPageData.workoutsData.workouts) {
            final SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(workout.getDiscipline().toString() + " " + workout.getUser().getName());
            entry.setLink("http://trainoo.com/workout/?id=" + workout.getId());
            entry.setPublishedDate(workout.getDate());
            final SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(String.valueOf(workout.getComment()));
            entries.add(entry);
        }
        feed.setEntries(entries);
        return feed;
    }
}
