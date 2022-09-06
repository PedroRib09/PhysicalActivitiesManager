package pt.isel.ls.cmdhandling;

import java.util.HashMap;

public class Headers {
    private HashMap<String,Object> map;

    public Headers(String p) {
        map = new HashMap<>();
        splitAndSaveHeaders(p);
    }

    public Object getMapValue(String s) {
        return map.get(s);
    }

    private void splitAndSaveHeaders(String headers) {
        String [] pairs = headers.split("\\|");
        for (int i = 0; i < pairs.length; i++) {
            String [] keyValues = pairs[i].split(":");
            map.put(keyValues[0],keyValues[1]);
        }
    }
}
