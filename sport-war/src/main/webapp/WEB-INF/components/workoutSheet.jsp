<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="com.nraynaud.sport.web.view.TableContent" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% final TableContent.TableSheet sheet = top(TableContent.TableSheet.class);%>
<dt><%=sheet.label%>
</dt>
<dd>
    <ul>
        <%
            if (sheet.rows.iterator().hasNext()) {
                boolean parity = false;
                for (final Workout workout : sheet.rows) {
        %>
        <li class="<%=parity ? "odd":"even"%>">
            <a href="<%=Helpers.createUrl("/workout", "", "id", String.valueOf(workout.getId()))%>"
               title="Voir le détail de cet entrainement">
                <%
                    sheet.renderer.render(workout, pageContext);
                %>
            </a>
        </li>
        <%
                    parity = !parity;
                }
            }
        %>
    </ul>
</dd>
