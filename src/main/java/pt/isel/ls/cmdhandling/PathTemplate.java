package pt.isel.ls.cmdhandling;

import java.util.HashMap;

public class PathTemplate {

    private String template;
    public static HashMap<String,Object> templateMap;

    public PathTemplate(String s) {
        this.template = s;
    }

    String getTemplate() {
        return template;
    }

    public static boolean isMatch(PathTemplate p,Path path) {
        String str = path.getMyPath();
        String[] strPath = str.split("/");
        templateMap = new HashMap<>();
        String s = p.getTemplate();
        String[] strPathTemplate = s.split("/");
        if (strPathTemplate.length == strPath.length) {
            for (int i = 0; i < strPathTemplate.length; i++) {
                if (strPathTemplate[i].length() > 0 && strPathTemplate[i] != null
                        && !isParameterTemplate(strPathTemplate[i])) {
                    if (strPathTemplate[i].length() > 0 &&  (i + 1) < strPathTemplate.length
                            && isParameterTemplate(strPathTemplate[i + 1])) {
                        templateMap.put(strPathTemplate[i], strPath[i + 1]);
                    } else {
                        templateMap.put(strPathTemplate[i], null);
                    }
                }
            }
        }
        return path.getMap().equals(templateMap);
    }

    public static boolean  isParameterTemplate(String s) {
        return s != null && s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}';
    }
}
