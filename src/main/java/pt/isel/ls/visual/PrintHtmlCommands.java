package pt.isel.ls.visual;

import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Method;
import pt.isel.ls.cmdhandling.Path;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.cmdhandling.Router;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

class PrintHtmlCommands {

    void printHtmlHttpUserId(OutputStream out, int tabCount, CommandResult cr) throws IOException, SQLException {

        int userId = Integer.parseInt(cr.getList().get(0));
        createNewTable(1, out, tabCount, cr, null);
        out.write("<br>".getBytes(StandardCharsets.UTF_8));

        cr = executeCommand("/users/" + userId + "/sports");
        createNewTable(2, out, tabCount, cr, null);
        out.write("<br>".getBytes(StandardCharsets.UTF_8));


        cr = executeCommand("/users/" + userId + "/activities");
        createNewTable(3, out, tabCount, cr, null);
    }

    private void printTableHeaders(OutputStream out, CommandResult cr) throws IOException {
        LinkedList<String> list = cr.getList();
        ArrayList fields = cr.fields();
        if (list.size() != 0) {
            out.write(("<tr>\n").getBytes(StandardCharsets.UTF_8));
            for (Object field : fields) {
                out.write(("<th>" + field + "</th>\n").getBytes(StandardCharsets.UTF_8));
            }
            out.write(("<th> link </th>\n").getBytes(StandardCharsets.UTF_8));
            out.write(("</tr>\n").getBytes(StandardCharsets.UTF_8));
        }
    }

    void createNewTable(int type, OutputStream out, int tabCount, CommandResult cr, String link) throws IOException {
        ArrayList fields = cr.fields();
        int counter = 0;
        String aux = "";
        out.write("<table>".getBytes(StandardCharsets.UTF_8));
        printTableHeaders(out, cr);
        String[] splitType;
        String linkType = "";
        String result = "";
        if (link != null) {
            splitType = link.split("/");
            linkType = splitType[1];
        }
        String[] save = new String[10];

        LinkedList<String> list = cr.getList();
        for (String str : list) {
            if (counter == 0) {
                Tabs.makeTabs(tabCount);
                out.write(("<tr>\n").getBytes(StandardCharsets.UTF_8));
                aux = str;
            }
            out.write(("<th>" + str + "</th>\n").getBytes(StandardCharsets.UTF_8));
            Tabs.makeTabs(tabCount + 1);
            save[counter] = str;
            counter++;
            if (counter >= fields.size()) {
                counter = 0;
                Tabs.makeTabs(tabCount);
                String s = getTypeOfReturn(type, aux, link, linkType, str);
                out.write(s.getBytes(StandardCharsets.UTF_8));
                result = str;
                Tabs.makeTabs(tabCount + 1, out);
                out.write(("</tr>\n").getBytes(StandardCharsets.UTF_8));
            }

        }
        out.write("</table>".getBytes(StandardCharsets.UTF_8));
        if (type == 8) {
            out.write(("<a href=/sports/" + result.trim() + ">"
                    + "Go to sports/" + result.trim()
                    + " </a>\n").getBytes(StandardCharsets.UTF_8));
        }
        if (type == 9) {
            createAnchors(out, save);
        }
    }

    private void createAnchors(OutputStream out, String[] save) throws IOException {
        out.write(("<a href=/users/" + save[3].trim() + ">"
                + "Go to users/" + save[3].trim()
                + " </a> <br>\n").getBytes(StandardCharsets.UTF_8));
        out.write(("<a href=/routes/" + save[4].trim() + ">"
                + "Go to routes/" + save[4].trim()
                + " </a> <br>\n").getBytes(StandardCharsets.UTF_8));
        out.write(("<a href=/sports/" + save[5].trim() + ">"
                + "Go to sports/" + save[5].trim()
                + " </a> <br>\n").getBytes(StandardCharsets.UTF_8));
        out.write(("<a href=/sports/" + save[5].trim() + "/activities>"
                + "Go to sports/" + save[5].trim() + "/activities"
                + " </a> <br>\n").getBytes(StandardCharsets.UTF_8));
    }

    private String getTypeOfReturn(int type, String aux, String link, String linkType, String str) {
        String s = "";
        switch (type) {
            case 1:
                s = "<th> <a href=/users> Go back to /users </a>\n </th>\n";
                break;
            case 2:
                s = "<th> <a href=/sports/" + aux.trim() + ">"
                        + "Go to sports sid => " + aux + " </a>\n </th>\n";
                break;
            case 3:
                s = "<th> <a href=/sports/" + str.trim() + "/activities/"
                        + aux.trim() + ">"
                        + "Go to /sports/" + str.trim() + "/activities/"
                        + aux + "</a>\n </th>\n";
                break;
            case 4:
                s = "<th> <a href=" + link + aux.trim() + ">"
                        + "Go to " + linkType + " id => " + aux + " </a>\n </th>\n";
                break;
            case 5:
                s = "<th> <a href=/routes>"
                        + "Go back to /routes </a>\n </th>\n";
                break;
            case 6:
                s = " <th> <a href=/sports/" + aux.trim() + ">"
                        + "Go to sports sid => " + aux + " </a> </th> \n";
                break;
            case 7:
                s = "<th> <a href=/sports/" + aux.trim() + "/activities" + ">"
                        + "Go to /sports/" + aux + "/activities </a>\n </th>\n";
                break;
            case 8:
                s = "<th> <a href=/sports/" + str.trim() + "/activities/"
                        + aux.trim() + ">"
                        + "Go to sports/" + str.trim()
                        + "/activities/" + aux.trim()
                        + " </a> </th> \n";
                break;
            default:
                break;
        }
        return s;
    }


    private CommandResult executeCommand(String s) throws SQLException {
        Path path = new Path(s);
        path.extractId(path);
        Router router = new Router();
        Optional<RouteResult> rr = router.findRoute(Method.GET, path);
        RouteResult routeResult = null;
        if (rr.isPresent()) {
            routeResult = rr.get();
        }
        CommandRequest request = new CommandRequest(routeResult, null);
        assert routeResult != null;
        CommandHandler handler = routeResult.getCommandHandler();
        return handler.execute(request);
    }


    void printHtmlHttpRouteId(OutputStream out, int tabCount, CommandResult cr) throws IOException, SQLException {
        createNewTable(5, out, tabCount, cr, null);
        out.write("<br>".getBytes(StandardCharsets.UTF_8));

        int routeId = Integer.parseInt(cr.getList().get(0));
        cr = executeCommand("/routes/" + routeId + "/sports");
        createNewTable(6, out, tabCount, cr, null);
    }
}

