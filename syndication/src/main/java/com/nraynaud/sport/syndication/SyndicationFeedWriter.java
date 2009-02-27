package com.nraynaud.sport.syndication;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.nraynaud.sport.data.PaginatedCollection;
import com.nraynaud.sport.formatting.FormatHelper;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
        final PaginatedCollection<Workout> workouts = workoutsPageData.workoutsData.workouts;
        final Iterator<Workout> iterator = workouts.iterator();
        if (iterator.hasNext())
            feed.setPublishedDate(iterator.next().getDate());
        final List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (final Workout workout : workouts) {
            final SyndEntry entry = new SyndEntryImpl();
            final String url = "http://trainoo.com/workout/?id=" + workout.getId();
            entry.setUri(url);
            entry.setTitle(formatTitle(workout));
            entry.setLink(url);
            final SyndPersonImpl person = new SyndPersonImpl();
            final User user = workout.getUser();
            person.setName(user.getName().nonEscaped());
            person.setUri("http://trainoo.com/bib/?id=" + user.getId());
            entry.setAuthors(Collections.singletonList(person));
            entry.setPublishedDate(workout.getDate());
            final SyndContent description = new SyndContentImpl();
            description.setType("text");
            final UserString comment = workout.getComment();
            description.setValue(
                    comment == null || comment.nonEscaped().isEmpty() ? "Aucun compte-rendu" : comment.nonEscaped());
            entry.setContents(Collections.singletonList(description));
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
        builder.append(" ").append(FormatHelper.formatDistance(workout.getDistance(), "km", ""));
        builder.append(" ").append(FormatHelper.formatDuration(workout.getDuration(), ""));
        return builder.toString();
    }
}
