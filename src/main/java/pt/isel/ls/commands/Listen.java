package pt.isel.ls.commands;

import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.http.HttpApp;

public class Listen {
    public static void executeServer(Parameters p) {
        try {
            HttpApp servletApp = new HttpApp();
            int port = 0;
            if (p != null) {
                port = Integer.parseInt((String)p.getMapValue("port"));
            }
            servletApp.runServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
