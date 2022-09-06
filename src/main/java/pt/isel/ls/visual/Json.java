package pt.isel.ls.visual;

import pt.isel.ls.cmdhandling.CommandResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class Json {
    public String printJson(boolean isFile, File fileName, CommandResult cr) throws IOException {

        StringBuilder help;
        help = new StringBuilder("{ \n\t\"" + cr.getType() + "\"" + ": [\n");
        PrintStream ps;
        if (isFile) {
            ps = new PrintStream(new PrintStream(fileName));
        } else {
            ps = new PrintStream(System.out);
        }
        ps.print(help);

        LinkedList<String> list = cr.getList();
        ArrayList fields = cr.fields();
        int k = 0;
        for (String str : list) {
            String s = "";
            if (k == 0) {
                s += "\t{\n";
            }
            s += "\t\t\"" + fields.get(k) + "\": ";
            s += "\"" + str + "\",\n";
            k++;

            if (k >= fields.size()) {
                k = 0;
                s += "\t},\n";
            }
            ps.print(s);
            help.append(s);
        }
        help.append("\n\t]\n}");
        if (isFile) {
            ps.append("\n\t]\n}");
            ps.close();
        } else {
            ps.println("\n\t]\n}");
        }

        return help.toString();
    }

}
