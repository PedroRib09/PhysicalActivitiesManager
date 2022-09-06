package pt.isel.ls.cmdhandling;

import pt.isel.ls.commands.DeleteUsersIdActivities;
import pt.isel.ls.commands.GetRouteById;
import pt.isel.ls.commands.GetRoutes;
import pt.isel.ls.commands.GetSportById;
import pt.isel.ls.commands.GetSports;
import pt.isel.ls.commands.GetSportsByRouteId;
import pt.isel.ls.commands.GetSportsByUserId;
import pt.isel.ls.commands.GetSportsIdActivities;
import pt.isel.ls.commands.GetSportsIdActivitiesId;
import pt.isel.ls.commands.GetTopsActivities;
import pt.isel.ls.commands.GetUserById;
import pt.isel.ls.commands.GetUsers;
import pt.isel.ls.commands.GetUsersIdActivities;
import pt.isel.ls.commands.PostRoute;
import pt.isel.ls.commands.PostSport;
import pt.isel.ls.commands.PostSportIdActivities;
import pt.isel.ls.commands.PostUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class Router {

    private ArrayList<RouteHandling> allRoutes;

    public Router() {
        allRoutes = new ArrayList<>();
        initTemplates();
    }

    public void addRoute(Method method, PathTemplate template, CommandHandler handler) {
        allRoutes.add(new RouteHandling(method, template, handler));
    }

    public Optional<RouteResult> findRoute(Method method, Path path) {
        Iterator<RouteHandling> iter = allRoutes.iterator();
        RouteHandling route;
        Optional<RouteResult> opt;
        RouteResult res;

        while (iter.hasNext()) {
            route = iter.next();
            if (route.getMethod().equals(method) && PathTemplate.isMatch(route.getPathTemplate(), path)) {
                HashMap<String, Object> m = path.getMap();
                res = new RouteResult(m, route.commandHandler);
                opt = Optional.of(res);
                return opt;
            }
        }
        return Optional.empty();
    }

    public void initTemplates() {
        addRoute(Method.POST, new PathTemplate("/users"), new PostUser());
        addRoute(Method.GET, new PathTemplate("/users/{uid}"), new GetUserById());
        addRoute(Method.GET, new PathTemplate("/users"), new GetUsers());
        addRoute(Method.POST, new PathTemplate("/routes"), new PostRoute());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/sports"), new GetSportsByUserId());
        addRoute(Method.GET, new PathTemplate("/routes/{rid}"), new GetRouteById());
        addRoute(Method.GET, new PathTemplate("/routes"), new GetRoutes());
        addRoute(Method.POST, new PathTemplate("/sports"), new PostSport());
        addRoute(Method.GET, new PathTemplate("/sports"), new GetSports());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}"), new GetSportById());
        addRoute(Method.POST, new PathTemplate("/sports/{sid}/activities"), new PostSportIdActivities());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities"), new GetSportsIdActivities());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities/{aid}"), new GetSportsIdActivitiesId());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/activities"), new GetUsersIdActivities());
        addRoute(Method.GET, new PathTemplate("/tops/activities"), new GetTopsActivities());
        addRoute(Method.GET, new PathTemplate("/routes/{rid}/sports"), new GetSportsByRouteId());
        addRoute(Method.DELETE, new PathTemplate("/users/{uid}/activities"), new DeleteUsersIdActivities());
    }
}