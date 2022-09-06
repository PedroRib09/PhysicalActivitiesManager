package pt.isel.ls.cmdhandling;

import java.util.HashMap;

public class Path {

    public String myPath;
    public HashMap<String,Object> pathMap;

    public Path(String myPath) {
        this.myPath = myPath;
    }

    public String getMyPath() {
        return myPath;
    }

    public Object getValue(String key) {
        return pathMap.get(key);
    }

    public void extractId(Path p) {
        pathMap = new HashMap<>();
        String s = p.getMyPath();
        s = s.substring(1);//para nao ficar empty o primeiro elemento
        String[] strPath = s.split("/");
        for (int i = 0; i < strPath.length; i++) {
            if (strPath[i] != null && !isNumeric(strPath[i])) {
                if ((i + 1) < strPath.length && isNumeric(strPath[i + 1])) {
                    pathMap.put(strPath[i], strPath[i + 1]);

                } else {
                    pathMap.put(strPath[i], null);
                }
            }
        }
    }

    public  boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public HashMap<String, Object> getMap() {
        return pathMap;
    }
}
