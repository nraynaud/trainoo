package com.nraynaud.sport;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookWebappHelper;
import com.google.code.facebookapi.IFacebookRestClient;
import com.nraynaud.sport.formatting.DateIO;
import com.nraynaud.sport.formatting.FormatHelper;
import com.nraynaud.sport.web.view.WorkoutView;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import static java.util.Collections.singleton;

public class FacebookUtil {
    public static final String API_KEY = System.getenv("FACEBOOK_PUBLIC");
    public static final String SECRET_KEY = System.getenv("FACEBOOK_PRIVATE");

    private FacebookUtil() {
    }

    public static IFacebookRestClient<Document> getClient(final HttpServletRequest request,
                                                          final HttpServletResponse response) {
        return FacebookWebappHelper.newInstanceXml(request, response, API_KEY, SECRET_KEY).get_api_client();
    }

    public static Long getFacebookUserId(final HttpServletRequest request, final HttpServletResponse response) throws
            FacebookException {
        final IFacebookRestClient<Document> facebookClient = getClient(request, response);
        return facebookClient.users_getLoggedInUser();
    }

    public static String formatWorkout(final Workout workout) {
        final UserString debriefing = workout.getDebriefing();
        final WorkoutView view = new WorkoutView(workout.getId().toString(),
                Helper.escapedForJavascript(workout.getDiscipline().nonEscaped()),
                DateIO.DATE_FORMATTER.print(workout.getDate().getTime()),
                FormatHelper.formatDistance(workout.getDistance(), "km", ""),
                FormatHelper.formatDuration(workout.getDuration(), ""),
                FormatHelper.formatEnergy(workout.getEnergy(), "kcal", ""),
                debriefing == null ? "" : debriefing.toString());
        return view.discipline + " " + view.distance + " " + view.duration + " " + view.energy;
    }

    public static String formatActivity(final Workout newWorkout, final boolean feminine) {
        final String auxiliary = feminine ? "est allée " : "est allé ";
        final String discipline = newWorkout.getDiscipline().nonEscaped();
        if (discipline.equals("course"))
            return auxiliary + "courir";
        if (discipline.equals("vélo"))
            return auxiliary + "faire du vélo";
        if (discipline.equals("natation"))
            return auxiliary + "nager";
        if (discipline.equals("roller"))
            return auxiliary + "faire du roller";
        if (discipline.equals("VTT"))
            return auxiliary + "faire du VTT";
        if (discipline.equals("marche"))
            return auxiliary + "marcher";
        if (discipline.equals(Workout.ELLIPTIC_BIKE))
            return "a fait du vélo elliptique";
        if (discipline.equals(Workout.HOME_BIKE))
            return "a fait du vélo d'appartement";
        throw new RuntimeException("unknown discipline");
    }

    public static String getInfo(final IFacebookRestClient<Document> facebook, final String field) throws
            FacebookException {
        final Long facebookId;
        facebookId = facebook.users_getLoggedInUser();
        final Document document = facebook.users_getInfo(singleton(facebookId),
                Collections.<CharSequence>singleton(field));
        return document.getFirstChild().getFirstChild().getLastChild().getFirstChild().getTextContent();
    }
}
