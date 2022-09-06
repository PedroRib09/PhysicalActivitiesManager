package pt.isel.ls.visual;

import pt.isel.ls.cmdhandling.CommandResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

public class PlainText {
    public String printText(boolean isFile, File fileName, CommandResult cr) throws IOException {
        LinkedList<String> list = cr.getList();
        StringBuilder res = new StringBuilder();
        PrintStream ps = null;
        if (isFile) {
            ps = new PrintStream(new PrintStream(fileName));
        }
        for (String str : list) {
            if (isFile) {
                ps.println(str);
                res.append(str).append("\n");
            } else {
                res.append(str).append("\n");
                System.out.println(str);
            }
        }
        return res.toString();
    }
}
