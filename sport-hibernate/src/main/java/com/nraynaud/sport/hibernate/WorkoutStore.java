package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.data.PaginatedCollection;
import static com.nraynaud.sport.hibernate.SQLHelper.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

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

    public Workout createWorkout(final Date date,
                                 final User user,
                                 final Long duration,
                                 final Double distance,
                                 final Long energy,
                                 final String discipline,
                                 final String comment,
                                 final String nikePlusId) {
        if (nikePlusId != null) {
            final Query query = entityManager.createNativeQuery("select ID from WORKOUTS where NIKEPLUSID=:nikeId");
            query.setParameter("nikeId", nikePlusId);
            //noinspection unchecked
            final List<Number> list = query.getResultList();
            if (!list.isEmpty()) {
                final Long workoutId = list.get(0).longValue();
                return entityManager.find(WorkoutImpl.class, workoutId);
            }
        }
        final Workout workout = new WorkoutImpl(user, date, duration, distance, energy, discipline, comment,
                nikePlusId);
        entityManager.persist(workout);
        try {
            setWorkoutParticipants(user, workout.getId(), new String[0]);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        return workout;
    }

    public void setWorkoutParticipants(final User user, final Long workoutId, final String[] participants) throws
            AccessDeniedException {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, workoutId);
        if (!workout.getUser().equals(user))
            throw new AccessDeniedException();
        deleteParticipation(workoutId);
        final Set<String> participantWithSelf = new HashSet<String>(Arrays.asList(participants));
        participantWithSelf.add(user.getName().nonEscaped());
        final Query query = createParticipantsInsertQuery(workoutId, participantWithSelf);
        final int count = query.executeUpdate();
        if (count != participantWithSelf.size())
            throw new RuntimeException(
                    " erreur : " + count + " lignes insérées au lieu de " + participants.length);
    }

    public Workout fetchWorkoutForEdition(final Long id, final User user, final boolean willWrite) throws
            WorkoutNotFoundException, AccessDeniedException {
        final Workout workout = fetchWorkout(id);
        if (willWrite)
            checkEditionGrant(workout, user);
        return workout;
    }

    public Workout fetchWorkout(final Long id) throws WorkoutNotFoundException {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, id);
        if (workout == null)
            throw new WorkoutNotFoundException();
        return workout;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    public void checkEditionGrant(final Workout workout, final User user) throws AccessDeniedException {
        if (!workout.getUser().equals(user))
            throw new AccessDeniedException();
    }

    public PaginatedCollection<Workout> getSimilarWorkouts(final Workout workout, final int similarPageIndex) {
        final double precision = 0.1;
        final double infCoeff = 1.0 - precision;
        final double suppCoeff = 1.0 + precision;
        final List<WhereFragment> clauses = new ArrayList<WhereFragment>(3);
        if (workout.getDistance() != null) {
            final double distance = workout.getDistance().doubleValue();
            clauses.add(new WhereFragment("(w.distance between :minDist and :maxDist)",
                    new Parameter("minDist", distance * infCoeff),
                    new Parameter("maxDist", distance * suppCoeff)));
        }
        if (workout.getDuration() != null) {
            final long duration = workout.getDuration().longValue();
            clauses.add(new WhereFragment("(w.duration between :minDur and :maxDur)",
                    new Parameter("minDur", (long) (duration * infCoeff)),
                    new Parameter("maxDur", (long) (duration * suppCoeff))));
        }
        if (clauses.isEmpty())
            return emptyPage();
        final StringBuilder dynamicPart = new StringBuilder("(");
        for (Iterator<WhereFragment> i = clauses.iterator(); i.hasNext();) {
            final WhereFragment clause = i.next();
            dynamicPart.append(clause.wherePart);
            if (i.hasNext())
                dynamicPart.append(" or ");
        }
        dynamicPart.append(')');
        final Query query = query(
                "select w from WorkoutImpl w where w.discipline=:discipline and w.id!=:selfId and w.user=:user and "
                        + dynamicPart
                        + " order by w.date desc, w.id desc");
        query.setParameter("selfId", workout.getId());
        query.setParameter("user", workout.getUser());
        query.setParameter("discipline", workout.getDiscipline().nonEscaped());
        for (final WhereFragment clause : clauses) {
            for (final Parameter parameter : clause.parameters) {
                query.setParameter(parameter.name, parameter.value);
            }
        }
        return paginateQuery(10, similarPageIndex, query);
    }

    private Query createParticipantsInsertQuery(final Long workoutId, final Set<String> participants) {
        final SQLHelper.Predicate namePred = createInListPredicate("NAME", participants, "participant");
        final Query query = entityManager.createNativeQuery(
                "insert INTO WORKOUT_USER (USER_ID, WORKOUT_ID) SELECT ID, :workoutId FROM USERS WHERE " + namePred);
        query.setParameter("workoutId", workoutId);
        namePred.bindVariables(query);
        return query;
    }

    private void deleteParticipation(final Long workoutId) {
        final Query query = entityManager.createNativeQuery("delete from WORKOUT_USER where WORKOUT_ID=:workoutId");
        query.setParameter("workoutId", workoutId);
        query.executeUpdate();
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

    private static class WhereFragment {
        public final String wherePart;
        public final Parameter[] parameters;

        private WhereFragment(final String wherePart, final Parameter... parameters) {
            this.wherePart = wherePart;
            this.parameters = parameters;
        }
    }

    private static class Parameter {
        public final String name;
        public final Object value;

        private Parameter(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
