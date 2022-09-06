package pt.isel.ls.visual;

import pt.isel.ls.cmdhandling.CommandResult;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class HtmlPrint {

    void printHtml(PrintStream ps, boolean isFile, int tabCount, CommandResult cr) {
        LinkedList<String> list = cr.getList();
        ArrayList fields = cr.fields();
        if (isFile) {
            headersFile(ps, fields, tabCount);
        } else {
            headers(fields, tabCount);
        }
        int counter = 0;
        for (String str : list) {
            if (counter == 0) {
                if (isFile) {
                    Tabs.makeTabsFile(tabCount, ps);
                    ps.println("<tr>");
                } else {
                    Tabs.makeTabs(tabCount);
                    System.out.println("<tr>");
                }
            }
            if (isFile) {
                Tabs.makeTabsFile(tabCount + 1, ps);
                ps.println("<td>" + str + "</td>");
            } else {
                Tabs.makeTabs(tabCount + 1);
                System.out.println("<th>" + str + "</th>");
            }
            counter++;
            if (counter >= fields.size()) {
                counter = 0;
                if (isFile) {
                    Tabs.makeTabsFile(tabCount, ps);
                    ps.println("</tr>");
                } else {
                    Tabs.makeTabs(tabCount);
                    System.out.println("</tr>");
                }
            }
        }
    }

    private void headersFile(PrintStream ps, ArrayList fields, int tabCount) {
        Tabs.makeTabsFile(tabCount, ps);
        ps.println("<tr>");
        for (Object field : fields) {
            Tabs.makeTabsFile(tabCount + 1, ps);
            ps.println("<th>" + field + "</th>");
        }
        Tabs.makeTabsFile(tabCount, ps);
        ps.println("<tr>");
    }

    private void headers(ArrayList fields, int tabCount) {
        Tabs.makeTabs(tabCount);
        System.out.println("<tr>");
        for (Object field : fields) {
            Tabs.makeTabs(tabCount);
            System.out.println("<th>" + field + "</th>");
        }
        Tabs.makeTabs(tabCount);
        System.out.println("<tr>");
    }
}
