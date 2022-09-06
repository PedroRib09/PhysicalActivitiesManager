package pt.isel.ls;

import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Headers;
import pt.isel.ls.cmdhandling.Method;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.cmdhandling.Path;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.cmdhandling.Router;
import pt.isel.ls.commands.Listen;
import pt.isel.ls.commands.Options;
import pt.isel.ls.visual.Element;
import pt.isel.ls.visual.Json;
import pt.isel.ls.visual.PlainText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws SQLException, IOException {
        Scanner in = new Scanner(System.in);
        Method method;
        Path path;
        Parameters parameters = null;
        Headers headers = null;
        Router router = new Router();
        RouteResult routeResult = null;
        if (args.length != 0) {
            System.out.println("Executing non-interactive mode");
            String str = convertStringArrayToString(args, " ").trim();

            String[] splitCmd = str.split(" ");
            if (Method.EXIT.toString().equals(splitCmd[0]) && splitCmd[1].equals("/")) {
                System.exit(0);
            } else if (Method.OPTION.toString().equals(splitCmd[0]) && splitCmd[1].equals("/")) {
                Options.listCommands();
                System.exit(0);
            }
            method = Method.valueOf(splitCmd[0]);
            path = new Path(splitCmd[1]);

            if (splitCmd.length == 3) {
                if (splitCmd[2].contains("accept")) {
                    headers = new Headers(splitCmd[2]);
                } else {
                    parameters = new Parameters(splitCmd[2]);
                }
            }
            if (splitCmd.length == 4) {
                headers = new Headers(splitCmd[2]);
                parameters = new Parameters(splitCmd[3]);
            }

            CommandResult resultI = getHandlerAndExecuteCmd(router, routeResult, method, path, parameters);
            if (method.equals(Method.GET)) {
                printType(resultI, headers, method.toString() + " " + path.getMyPath());
            }
            System.exit(0);
        }

        System.out.println("Interactive Mode - Type Command");

        while (true) {
            CommandResult resultI;
            try {
                System.out.print("> ");
                headers = null;
                parameters = null;
                String input = in.nextLine();
                input = input.trim();
                String[] splitCmd = input.split(" ");
                if (Method.EXIT.toString().equals(splitCmd[0]) && splitCmd[1].equals("/")) {
                    break;
                } else if (Method.OPTION.toString().equals(splitCmd[0]) && splitCmd[1].equals("/")) {
                    Options.listCommands();
                    System.exit(0);
                } else if (Method.LISTEN.toString().equals(splitCmd[0]) && splitCmd[1].equals("/")) {
                    Listen.executeServer(new Parameters(splitCmd[2]));
                }
                method = Method.valueOf(splitCmd[0]);
                path = new Path(splitCmd[1]);

                if (splitCmd.length == 3) {
                    if (splitCmd[2].contains("accept")) {
                        headers = new Headers(splitCmd[2]);
                    } else {
                        parameters = new Parameters(splitCmd[2]);
                    }
                }
                if (splitCmd.length == 4) {
                    headers = new Headers(splitCmd[2]);
                    parameters = new Parameters(splitCmd[3]);
                }
                try {
                    resultI = getHandlerAndExecuteCmd(router, routeResult, method, path, parameters);
                    if (method.equals(Method.GET)) {
                        printType(resultI, headers, method.toString() + " " + path.getMyPath());
                    } else {
                        printResults(resultI);
                    }
                } catch (SQLException e) {
                    if (e.toString().contains("duplicate key")) {
                        System.out.println("Duplicate Unique value");
                    } else {
                        System.out.println("SQL Exception");
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid Command");
            }
        }
    }

    private static void printResults(CommandResult resultI) {
        if (resultI.getList().size() == 0) {
            System.out.println("Nothing Found");
        }
        if (!resultI.getList().get(0).contains("Error")) {
            System.out.println("Success!");
        } else {
            for (String g :
                    resultI.getList()) {
                System.out.println(g);
            }
        }
    }

    public static byte[] printBaos(CommandResult result, ByteArrayOutputStream baos,
                                   String title, String link, String query, boolean notFirst) throws IOException {
        Element e = Element.init(title);
        e.printHttpUsers(baos, result, link, query, notFirst);
        return baos.toByteArray();
    }

    public static byte[] printBaosId(CommandResult result,
                                     ByteArrayOutputStream baos, String link,
                                     String query, boolean notFirst) throws IOException, SQLException {
        boolean secondTable = false;
        if (link.contains("/users/")) {
            secondTable = true;
        }
        Element e = Element.intermediateInit(secondTable);
        e.printIntermediate(baos, result, link, query, notFirst);
        return baos.toByteArray();
    }

    private static void printType(CommandResult result, Headers headers, String title) throws IOException {

        if (headers == null) {
            Element e = Element.init(title);
            e.print(System.out, result, false);
        }

        if (headers != null) {
            Object file = headers.getMapValue("file-name");
            File myObj = null;

            if (file != null) {
                myObj = new File(file.toString());
            }

            Object type = headers.getMapValue("accept");

            if (type.equals("text/plain")) {
                PlainText pt = new PlainText();
                if (myObj != null) {
                    pt.printText(true, myObj, result);
                } else {
                    pt.printText(false, null, result);
                }
            }

            if (type.equals("text/html")) {
                if (myObj != null) {
                    Element e = Element.init(title);
                    PrintStream ps = new PrintStream(myObj);
                    e.print(ps, result, true);
                } else {
                    Element e = Element.init(title);
                    e.print(System.out, result, false);
                }
            }

            if (type.equals("application/json")) {
                Json json = new Json();
                if (myObj != null) {
                    json.printJson(true, myObj, result);
                } else {
                    json.printJson(false, null, result);
                }
            }
        }
        if (result.getList().size() == 0) {
            System.exit(0);
        }
    }

    public static CommandResult getHandlerAndExecuteCmd(Router router,
                                                        RouteResult routeResult,
                                                        Method method, Path path,
                                                        Parameters parameters) throws SQLException {
        path.extractId(path);
        Optional<RouteResult> rr = router.findRoute(method, path);
        if (rr.isPresent()) {
            routeResult = rr.get();
        }
        CommandRequest request = new CommandRequest(routeResult, parameters);
        CommandHandler handler = routeResult.getCommandHandler();
        return handler.execute(request);
    }

    public static CommandHandler getHandler(Router router, Method method, Path path) {
        path.extractId(path);
        Optional<RouteResult> rr = router.findRoute(method, path);
        RouteResult routeResult = null;
        if (rr.isPresent()) {
            routeResult = rr.get();
        }

        return routeResult == null ? null : routeResult.getCommandHandler();
    }


    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(str).append(delimiter);
        }
        return sb.substring(0, sb.length() - 1);
    }
}
