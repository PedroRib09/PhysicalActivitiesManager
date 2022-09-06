package pt.isel.ls.cmdhandling;

import java.util.ArrayList;
import java.util.HashMap;

public class Parameters {

    private HashMap<String,Object> map;
    public String parameter;

    public Parameters(String p) {
        map = new HashMap<>();
        splitAndSaveParams(p);
        this.parameter = p;
        saveString();
    }

    public String saveString() {
        return parameter;
    }

    public Object getMapValue(String s) {
        return map.get(s);
    }

    private void splitAndSaveParams(String parameter) {
        String [] pairs = parameter.split("&");
        for (int i = 0; i < pairs.length; i++) {
            String [] keyValues = pairs[i].split("=");
            keyValues[1] = replaceWithSpaces(keyValues[1]);
            map.put(keyValues[0],keyValues[1]);
        }
    }

    public String replaceWithSpaces(String str) {
        str = str.replace('+',' ');
        return str;
    }

    public ArrayList<Integer> getActivitiesId(String p) {
        String [] pairs = p.split("&");
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < pairs.length; i++) {
            String [] keyValues = pairs[i].split("=");
            list.add(Integer.parseInt(keyValues[1]));
        }
        return list;
    }
}
