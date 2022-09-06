package pt.isel.ls.visual;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

class Tabs {

    static void makeTabsFile(int ntabs, PrintStream ps) {
        for (int j = 0; j < ntabs; j++) {
            ps.print("\t");
        }
    }

    static void makeTabs(int ntabs) {
        for (int j = 0; j < ntabs; j++) {
            System.out.print("\t");
        }
    }

    static void makeTabs(int ntabs, OutputStream outputStream) throws IOException {
        for (int j = 0; j < ntabs; j++) {
            outputStream.write(("\t").getBytes(StandardCharsets.UTF_8));
        }
    }

    static int getTabCount(Object obj) {
        int depth;
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Element) {
            depth = getTabCount(((Element) obj).parent);
        } else {
            depth = getTabCount(((Text) obj).parent);
        }
        return depth + 1;
    }

    static void makeTabsHttp(int ntabs, OutputStream out) throws IOException {
        for (int j = 0; j < ntabs; j++) {
            out.write("\t".getBytes(StandardCharsets.UTF_8));
        }
    }

    static void makeTabs2(int ntabs, PrintStream out) {
        for (int j = 0; j < ntabs; j++) {
            out.print("\t");
        }
    }

    static String makeTabs2(int tabC) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < tabC; j++) {
            sb.append("\t");
        }
        return sb.toString();
    }

}
