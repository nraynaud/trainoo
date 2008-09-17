package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Group;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.PaginatedCollection;
import static com.nraynaud.sport.hibernate.SQLHelper.EMPTY_STRING_LIST;
import static com.nraynaud.sport.hibernate.SQLHelper.createInListPredicate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;

class WorkoutStore {
    private final EntityManager entityManager;

    public WorkoutStore(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PaginatedCollection<Workout> getWorkouts(final User user, final Collection<String> disciplines,
                                                    final int startIndex,
                                                    final int pageSize) {
        final String wherePart = user != null ? ":user MEMBER OF w.participants" : "1=1";
        final Query query = workoutSelection(disciplines, "WorkoutImpl w", wherePart);
        if (user != null)
            query.setParameter("user", user);
        return SQLHelper.paginateWorkoutQuery(startIndex, pageSize, query);
    }

    public PaginatedCollection<Workout> getWorkouts(final Group group, final String discipline, final int firstIndex,
                                                    final int pageSize) {
        final String joinPart = "GroupImpl g inner join g.members u inner join u.workouts w ";
        final Query query = workoutSelection(collectionDiscipline(discipline), joinPart, "g =:group");
        query.setParameter("group", group);
        return SQLHelper.paginateWorkoutQuery(firstIndex, pageSize, query);
    }

    private Query workoutSelection(final Collection<String> disciplines, final String joinPart,
                                   final String wherePart) {
        final SQLHelper.Predicate disciplinePredicate = createInListPredicate(" and w.discipline ", disciplines,
                "discpline");
        final Query query = query(
                "select w, count(m) from " + joinPart + " left join w.publicMessages m  where " + wherePart
                        + disciplinePredicate
                        + " group by w.id, w.user, w.date, w.duration, w.distance, w.energy, w.discipline, w.nikePlusId, w.comment"
                        + " order by w.date desc, w.id desc");
        disciplinePredicate.bindVariables(query);
        return query;
    }

    private Query query(final String queryString) {
        return entityManager.createQuery(queryString);
    }

    private static Collection<String> collectionDiscipline(final String discipline) {
        if (discipline == null)
            return EMPTY_STRING_LIST;
        else
            return Collections.singleton(discipline);
    }
}
