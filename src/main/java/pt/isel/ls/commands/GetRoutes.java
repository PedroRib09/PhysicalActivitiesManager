package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetRoutesView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.mapper.RouteMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRoutes implements CommandHandler {

    private RouteMapper routeMapper = new RouteMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {

        GetRoutesView view = new GetRoutesView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {

            String s;
            Parameters p = commandRequest.getParameters();
            PreparedStatement stmt;
            if (p != null) {
                s = "select * FROM route OFFSET ? LIMIT ?";
                stmt = con.prepareStatement(s);
                stmt.setInt(1, Integer.parseInt((String) p.getMapValue("skip")));
                stmt.setInt(2, Integer.parseInt((String) p.getMapValue("top")));
            } else {
                s = "select * FROM route";
                stmt = con.prepareStatement(s);
            }
            ResultSet rs;
            rs = stmt.executeQuery();
            view.map(rs, routeMapper);

            stmt.close();
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}



