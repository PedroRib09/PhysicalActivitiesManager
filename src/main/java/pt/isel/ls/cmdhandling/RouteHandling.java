package pt.isel.ls.cmdhandling;

public class RouteHandling {

    public Method method;
    public PathTemplate pathTemplate;
    public CommandHandler commandHandler;

    public RouteHandling(Method method, PathTemplate pathTemplate, CommandHandler commandHandler) {
        this.method = method;
        this.pathTemplate = pathTemplate;
        this.commandHandler = commandHandler;
    }

    Method getMethod() {
        return method;
    }

    PathTemplate getPathTemplate() {
        return pathTemplate;
    }

}
