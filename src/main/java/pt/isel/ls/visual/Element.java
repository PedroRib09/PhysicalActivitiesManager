package pt.isel.ls.visual;

import pt.isel.ls.cmdhandling.CommandResult;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Element {

    private Object data;
    private List<Object> children = new ArrayList<>();
    Object parent;
    private static List<Object> allNodes = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();
    private PrintHtmlCommands printHtmlCommands = new PrintHtmlCommands();

    public String printToRoot() {
        Element root = getRoot(allNodes);
        assert root != null;
        stringBuilder.append("<").append(root.toString()).append(">\n");
        getRootHyperLink((root));
        stringBuilder.append("</").append(root.toString()).append(">\n");
        return stringBuilder.toString();
    }

    public static Element init(String htmlTitle) {
        allNodes.clear();
        Element html = new Element("html");
        Element head = new Element("head");
        Element title = new Element("title");
        final Element style = new Element("style");
        final Element body = new Element("body");
        final Element a = new Element("a");
        final Element table = new Element("table");
        Text titleText = new Text(htmlTitle);
        html.addChild(head);
        head.addChild(title);
        title.addChild(titleText);
        head.addChild(style);
        body.addChild(a);
        html.addChild(body);
        body.addChild(table);
        return html;
    }

    public static Element intermediateInit(boolean needSecondTable) {
        allNodes.clear();
        Element html = new Element("html");
        Element head = new Element("head");
        Element title = new Element("title");
        Element style = new Element("style");
        final Element body = new Element("body");
        final Element a = new Element("a");
        final Element table = new Element("table");

        html.addChild(head);
        head.addChild(title);
        head.addChild(style);
        body.addChild(a);
        html.addChild(body);
        body.addChild(table);
        return html;
    }

    public static Element homeInit() {
        allNodes.clear();
        Element html = new Element("html");
        Element head = new Element("head");
        Element body = new Element("body");
        Element a = new Element("a");

        html.addChild(head);
        body.addChild(a);
        html.addChild(body);

        return html;
    }

    private String getRootHyperLink(Element root) {
        if (root.children == null) {
            return stringBuilder.toString();
        }
        for (int i = 0; i < root.children.size(); i++) {
            int tabCount = Tabs.getTabCount(root);
            stringBuilder.append(Tabs.makeTabs2(tabCount));

            if (!root.children.get(i).toString().equals("a")) {
                stringBuilder.append("<").append(root.children.get(i).toString()).append(">\n");
            } else {
                stringBuilder.append("<").append(root.children.get(i).toString()).append(" ");
            }

            if (root.children.get(i) instanceof Element) {
                getRootHyperLink((Element) root.children.get(i));
            } else {
                return stringBuilder.toString();
            }

            if (root.children.get(i).toString().equals("a")) {
                stringBuilder.append(" href=/users>/users</")
                        .append(root.children.get(i).toString()).append(">\n");
                stringBuilder.append(Tabs.makeTabs2(tabCount));
                stringBuilder.append("<").append(root.children.get(i).toString())
                        .append(" href=/sports>/sports</")
                        .append(root.children.get(i).toString()).append(">\n");
                stringBuilder.append(Tabs.makeTabs2(tabCount));
                stringBuilder.append("<").append(root.children.get(i).toString())
                        .append(" href=/routes>/routes");
            }

            if (!root.children.get(i).toString().equals("a")) {
                stringBuilder.append(Tabs.makeTabs2(tabCount));
            }
            stringBuilder.append("</").append(root.children.get(i).toString()).append(">\n");
        }
        return stringBuilder.toString();
    }

    public void print(PrintStream out, CommandResult result, boolean isFile) {
        Element root = getRoot(allNodes);
        assert root != null;
        out.println("<" + root.toString() + ">");
        printNodeInformation(root, out, result, isFile);
        out.println("</" + root.toString() + ">");
    }

    public void printHttpUsers(OutputStream out, CommandResult result,
                               String link, String query, boolean notFirst) throws IOException {
        Element root = getRoot(allNodes);
        assert root != null;
        out.write(("<" + root.toString() + ">\n").getBytes(StandardCharsets.UTF_8));
        printNodeHttpInformation(root, out, result, link, query, notFirst);
        out.write(("</" + root.toString() + ">\n").getBytes(StandardCharsets.UTF_8));
    }

    public String printIntermediate(OutputStream out,
                                    CommandResult result, String link,
                                    String query, boolean notFirst) throws IOException, SQLException {
        Element root = getRoot(allNodes);
        assert root != null;
        out.write(("<" + root.toString() + ">\n").getBytes(StandardCharsets.UTF_8));
        String[] typeSplit = link.split("/");
        String type = typeSplit[1];
        if (link.contains("/activities/")) {
            printNodeHttp(root, out, result, "activityid", link, query, notFirst);
        } else if (link.contains("/activities")) {
            printNodeHttp(root, out, result, "activitylist", link, query, notFirst);
        } else {
            printNodeHttp(root, out, result, type, link, query, notFirst);

        }
        out.write(("</" + root.toString() + ">\n").getBytes(StandardCharsets.UTF_8));
        return stringBuilder.toString();
    }

    private void printNodeHttpInformation(Element root, OutputStream out,
                                          CommandResult result,
                                          String link, String query, boolean notFirstPage) throws IOException {
        if (root.children == null) {
            return;
        }
        for (int i = 0; i < root.children.size(); i++) {
            int tabCount = Tabs.getTabCount(root);
            Tabs.makeTabsHttp(tabCount, out);

            if (!root.children.get(i).toString().equals("a")) {
                out.write(("<" + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
            }

            if (root.children.get(i) instanceof Element) {
                printNodeHttpInformation((Element) root.children.get(i), out, result, link, query, notFirstPage);
            } else {
                return;
            }

            if (root.children.get(i).toString().equals("a")) {
                out.write(("<" + root.children.get(i).toString() + " href=/>Go back to root</"
                        + root.children.get(i).toString() + ">\n<br>").getBytes(StandardCharsets.UTF_8));

                query = paging(root, out, result, link, query, notFirstPage, i);
            }

            if (root.children.get(i).toString().equals("style")) {
                Tabs.makeTabsHttp(tabCount + 1, out);
                out.write(("table, th, td { border: 1px solid black; }").getBytes(StandardCharsets.UTF_8));
            }
            if (root.children.get(i).toString().equals("table")) {

                printHtmlCommands.createNewTable(4, out, tabCount + 1, result, link);
            }
            Tabs.makeTabsHttp(tabCount, out);
            if (!root.children.get(i).toString().equals("a")) {
                out.write(("</" + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private String paging(Element root, OutputStream out, CommandResult result, String link, String query,
                          boolean notFirstPage, int i) throws IOException {
        String help = link;
        String queryPreviousHelper = "";
        if (notFirstPage) {
            queryPreviousHelper = link;
            queryPreviousHelper = queryPreviousHelper.substring(0, queryPreviousHelper.length() - 1) + '?';
            queryPreviousHelper += query;
        }

        if ((result.getList().size() - 5) % 5 == 0) {
            help = help.substring(0, help.length() - 1) + '?';
            query = setQueryUp(query);
            out.write(("<" + root.children.get(i).toString()
                    + " href=" + help + query + ">Next Page</"
                    + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
        }
        if (!query.contains("skip=0") && notFirstPage) {
            query = setQueryDown(queryPreviousHelper.equals("") ? query : queryPreviousHelper);
            if (query.equals("")) {
                queryPreviousHelper = link;
                queryPreviousHelper = queryPreviousHelper.substring(0, help.length() - 1);
            } else {
                queryPreviousHelper = query;
            }
            out.write(("<" + root.children.get(i).toString()
                    + " href=" + queryPreviousHelper + ">Previous Page</"
                    + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
        }
        return query;
    }

    private String setQueryUp(String query) {
        String[] aux = query.split("&");
        String[] str = aux[0].split("=");
        query = str[0];
        int strl = Integer.parseInt(str[1]);
        strl += 5;
        str = aux[1].split("=");
        int strr = Integer.parseInt(str[1]);
        query += "=" + strl + "&" + str[0] + "=" + strr;
        return query;
    }

    private String setQueryDown(String query) {
        String[] aux = query.split("&");
        String[] str = aux[0].split("=");
        query = str[0];
        int strl = Integer.parseInt(str[1]);
        strl -= 5;
        str = aux[1].split("=");
        int strr = Integer.parseInt(str[1]);
        if (strl > 0) {
            query += "=" + strl + "&" + str[0] + "=" + strr;
        } else {
            query = "";
        }
        return query;
    }

    private void printNodeHttp(Element root, OutputStream out,
                               CommandResult result, String type, String link, String query, boolean notFirstPage)
            throws IOException, SQLException {

        if (root.children == null) {
            return;
        }
        for (int i = 0; i < root.children.size(); i++) {
            int tabCount = Tabs.getTabCount(root);
            Tabs.makeTabsHttp(tabCount, out);
            if (root.children.get(i).toString().equals("a")) {
                out.write(("<" + root.children.get(i).toString()).getBytes(StandardCharsets.UTF_8));
            } else {
                out.write(("<" + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
            }
            if (root.children.get(i) instanceof Element) {
                printNodeHttp((Element) root.children.get(i), out, result, type, link, query, notFirstPage);
            } else {
                return;
            }
            if (root.children.get(i).toString().equals("a")) {
                out.write(((" href=/>Go back to root</" + root.children.get(i).toString()
                        + "> <br> \n")).getBytes(StandardCharsets.UTF_8));
                out.write(Tabs.makeTabs2(tabCount).getBytes(StandardCharsets.UTF_8));
                String aux = type;
                if (type.contains("activity")) {
                    aux = "";
                }
                if (!aux.equals("")) {
                    out.write((("<" + root.children.get(i).toString()
                            + " href=/routes>Go back to /" + type + "</" + root.children.get(i).toString()
                            + "> <br>\n").getBytes(StandardCharsets.UTF_8)));
                }
                query = paging(root, out, result, link, query, notFirstPage, i);
            }


            if (root.children.get(i).toString().equals("style")) {
                Tabs.makeTabsHttp(tabCount + 1, out);
                out.write(("table, th, td { border: 1px solid black; }").getBytes(StandardCharsets.UTF_8));
            }
            if (root.children.get(i).toString().equals("table")) {
                switch (type) {
                    case "sports":
                        printHtmlCommands.createNewTable(7, out, tabCount + 1, result, null);
                        out.write("<br>".getBytes(StandardCharsets.UTF_8));
                        break;
                    case "routes":
                        printHtmlCommands.printHtmlHttpRouteId(out, tabCount + 1, result);
                        break;

                    case "users":
                        printHtmlCommands.printHtmlHttpUserId(out, tabCount + 1, result);
                        break;
                    case "activityid":
                        printHtmlCommands.createNewTable(9, out, tabCount + 1, result, null);
                        out.write("<br>".getBytes(StandardCharsets.UTF_8));
                        break;

                    case "activitylist":
                        printHtmlCommands.createNewTable(8, out, tabCount + 1, result, null);
                        break;
                    default:
                        break;
                }
            }
            Tabs.makeTabsHttp(tabCount, out);
            if (!root.children.get(i).toString().equals("a")) {
                if (root.children.get(i).toString().equals("table")) {
                    out.write(("</" + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
                }
                out.write(("</" + root.children.get(i).toString() + ">\n").getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void printNodeInformation(Element root, PrintStream out, CommandResult result, boolean isFile) {
        if (root.children == null) {
            return;
        }
        for (int i = 0; i < root.children.size(); i++) {
            int tabCount = Tabs.getTabCount(root);
            Tabs.makeTabs2(tabCount, out);
            out.print("<" + root.children.get(i).toString() + ">\n");
            if (root.children.get(i) instanceof Element) {
                printNodeInformation((Element) root.children.get(i), out, result, isFile);
            } else {
                return;
            }
            if (root.children.get(i).toString().equals("style")) {
                Tabs.makeTabs2(tabCount + 1, out);
                out.println("table, th, td { border: 1px solid black; }");
            }
            if (root.children.get(i).toString().equals("table")) {
                HtmlPrint htmlPrint = new HtmlPrint();
                htmlPrint.printHtml(out, isFile, tabCount + 1, result);
            }
            Tabs.makeTabs2(tabCount, out);
            out.print("</" + root.children.get(i).toString() + ">\n");
        }
    }

    private Element(Object data) {
        this.data = data;
    }

    private Element getRoot(List<Object> list) {
        for (Object o : list) {
            if (o instanceof Element && ((Element) o).parent == null) {
                return (Element) o;
            }
        }
        return null;
    }

    private void addChild(Object curr) {
        if (curr instanceof Element) {
            ((Element) curr).parent = this;
        }

        if (curr instanceof Text) {
            ((Text) curr).parent = this;
        }

        children.add(curr);

        if (!allNodes.contains(curr)) {
            allNodes.add(curr);
        }

        if (!allNodes.contains(this)) {
            allNodes.add(this);
        }
    }

    @Override
    public String toString() {
        return "" + data;
    }
}
