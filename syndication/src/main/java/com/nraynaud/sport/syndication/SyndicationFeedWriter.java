package com.nraynaud.sport.syndication;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.nraynaud.sport.formatting.FormatHelper;
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
        feed.setUri("http://trainoo.com/");
        feed.setLink("http://trainoo.com/");
        feed.setDescription("Les derniers entraînements sur Trainoo.com");
        final List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (final Workout workout : workoutsPageData.workoutsData.workouts) {
            final SyndEntry entry = new SyndEntryImpl();
            entry.setUri("workout" + workout.getId());
            entry.setTitle(formatTitle(workout));
            entry.setLink("http://trainoo.com/workout/?id=" + workout.getId());
            entry.setAuthor(workout.getUser().getName().nonEscaped());
            entry.setPublishedDate(workout.getDate());
            final SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            final UserString comment = workout.getComment();
            description.setValue(comment == null ? "Aucun compte-rendu" : comment.nonEscaped());
            entry.setDescription(description);
            entries.add(entry);
        }
        feed.setEntries(entries);
        return feed;
    }

    private static String formatTitle(final Workout workout) {
        final StringBuilder builder = new StringBuilder();
        builder.append(workout.getDiscipline().nonEscaped())
                .append(" ")
                .append(workout.getUser().getName().nonEscaped());
        builder.append(" ").append(FormatHelper.formatDistance(workout.getDistance(), ""));
        builder.append(" ").append(FormatHelper.formatDuration(workout.getDuration(), ""));
        return builder.toString();
    }
}
