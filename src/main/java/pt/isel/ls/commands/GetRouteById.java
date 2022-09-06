package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetRouteByIdView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.RouteMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GetRouteById implements CommandHandler {

    private RouteMapper routeMapper = new RouteMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {

        GetRouteByIdView view = new GetRouteByIdView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();

            if (!ParameterCheck.isNumber(r.getKeyValue("routes"))) {
                errorView.addToList(new Error("Id is not an Integer!"));
            }

            ResultSet rs;
            String s = "select * FROM route where rid = ?";

            try (PreparedStatement stmt = con.prepareStatement(s)) {
                stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("routes")));
                rs = stmt.executeQuery();
                view.map(rs, routeMapper);
            }

        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}

