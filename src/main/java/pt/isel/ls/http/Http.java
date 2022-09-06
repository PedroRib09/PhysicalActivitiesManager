package pt.isel.ls.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.App;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Method;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.cmdhandling.Path;
import pt.isel.ls.cmdhandling.Router;
import pt.isel.ls.visual.Element;
import pt.isel.ls.visual.Json;
import pt.isel.ls.visual.PlainText;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class Http extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(Http.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        log.info("incoming request: method={}, uri={}, accept={}",
                req.getMethod(),
                req.getRequestURI(),
                req.getHeader("Accept"));

        String query = "skip=0&top=5";
        boolean notFirst = false;
        byte[] respBodyBytes = null;
        String respBody;
        Charset utf8 = StandardCharsets.UTF_8;
        Router router = new Router();
        router.initTemplates();
        Path path = new Path(req.getRequestURI());
        Parameters parameters = new Parameters(query);
        if (req.getQueryString() != null) {
            parameters = new Parameters(req.getQueryString());
        }
        Method method = Method.valueOf(req.getMethod());

        CommandHandler handler = App.getHandler(router, method, path);

        if (handler == null) {
            if (!req.getRequestURI().equals("/")) {
                resp.setStatus(404);
                resp.setContentType(String.format("text/plain;charset=%s", utf8.name()));
                respBody = "Handler doesn't exist";
                respBodyBytes = respBody.getBytes(utf8);
            } else {
                resp.setContentType(String.format("text/html;charset=%s", utf8.name()));
                Element e = Element.homeInit();
                respBody = e.printToRoot();
                resp.setStatus(200);
                respBodyBytes = respBody.getBytes(utf8);
            }
            resp.setContentLength(respBodyBytes.length);
        } else {
            CommandResult result = null;
            path = new Path(req.getRequestURI());
            try {
                result = App.getHandlerAndExecuteCmd(router, null, method,
                        path, parameters);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String link = path.getMyPath();
            link = removeLastChar(link);
            link = setlink(link, path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            query = req.getQueryString() != null ? req.getQueryString() : query;
            if (result != null) {
                if (req.getQueryString() != null) {
                    notFirst = true;
                }
                resp.setStatus(200);
                if (req.getHeader("Accept").equals("text/plain")) {
                    PlainText pt = new PlainText();
                    respBodyBytes = pt.printText(false,null,result).getBytes();
                    resp.setContentType(String.format("text/plain;charset=%s", utf8.name()));
                } else if (req.getHeader("Accept").equals("application/json")) {
                    Json json = new Json();
                    respBodyBytes = json.printJson(false, null,result).getBytes();
                    resp.setContentType(String.format("application/json;charset=%s", utf8.name()));
                } else {
                    resp.setContentType(String.format("text/html;charset=%s", utf8.name()));
                    if (req.getRequestURI().contains("/sports/") && req.getRequestURI().contains("/activities/")) {
                        link = req.getRequestURI();
                        try {
                            respBodyBytes = App.printBaosId(result,baos,link,query,notFirst);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (req.getRequestURI().contains("/sports/")
                            && req.getRequestURI().contains("/activities")) {
                        link = req.getRequestURI();
                        try {
                            respBodyBytes = App.printBaosId(result,baos,link,query,notFirst);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (req.getRequestURI().contains("/users/")) {
                        link = req.getRequestURI();
                        try {
                            respBodyBytes = App.printBaosId(result,baos,link,query,notFirst);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (req.getRequestURI().contains("/sports/")) {
                        link = req.getRequestURI();
                        try {
                            respBodyBytes = App.printBaosId(result,baos,link,query,notFirst);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (req.getRequestURI().contains("/routes/")) {
                        link = req.getRequestURI();
                        try {
                            respBodyBytes = App.printBaosId(result,baos,link,query,notFirst);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (req.getRequestURI().contains("/users") || req.getRequestURI().contains("/sports")
                            || req.getRequestURI().contains("/routes")) {
                        respBodyBytes = App.printBaos(result, baos, path.getMyPath(), link,query,notFirst);
                    }
                    resp.setStatus(200);
                }
            }
            resp.setContentLength(respBodyBytes.length);
        }

        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.flush();
        log.info("outgoing response: method={}, uri={}, status={}, Content-Type={}",
                req.getMethod(),
                req.getRequestURI(),
                resp.getStatus(),
                resp.getHeader("Content-Type"));
    }

    private String setlink(String link, Path path) {
        String newLink = null;
        String [] str = link.split("/");
        if (str.length == 1) {
            return link;
        } else if (str.length == 2) {
            newLink = link + "/";
            return newLink;
        }
        return newLink;
    }

    private String removeLastChar(String link) {
        if (link.charAt(link.length() - 1) == '/') {
            link = link.substring(0, link.length() - 1);
        }
        return link;
    }

}