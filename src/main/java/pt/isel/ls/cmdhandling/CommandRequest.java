package pt.isel.ls.cmdhandling;


public class CommandRequest {

    private Parameters parameters;
    private RouteResult res;

    public CommandRequest(RouteResult res, Parameters parameters) {
        this.res = res;
        this.parameters = parameters;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public RouteResult getRouteResult() {
        return res;
    }
}
