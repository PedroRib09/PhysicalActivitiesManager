package pt.isel.ls.cmdhandling;

import java.util.HashMap;

public class RouteResult {

    private HashMap<String,Object> map;
    public CommandHandler commandHandler;

    public RouteResult(HashMap<String, Object> map, CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.map = map;
    }

    public void getRouteResMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public Object getKeyValue(String key) {
        return map.get(key);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
