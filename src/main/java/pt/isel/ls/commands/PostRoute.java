package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.PostRouteView;
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
import java.sql.Statement;

public class PostRoute implements CommandHandler {
    private RouteMapper routeMapper = new RouteMapper();


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PostRouteView view = new PostRouteView();
        ErrorView errorView = new ErrorView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            Parameters p = commandRequest.getParameters();

            if (!(ParameterCheck.checkName(p.getMapValue("startLocation")))) {
                errorView.addToList(new Error("Start location is not valid!"));
            }
            if (!(ParameterCheck.checkName(p.getMapValue("endLocation")))) {
                errorView.addToList(new Error("End location is not valid!"));
            }
            if (!(ParameterCheck.isNumber(p.getMapValue("distance")))) {
                errorView.addToList(new Error("Distance is not valid!"));
            }
            if (errorView.getList().isEmpty()) {
                String s = "INSERT INTO route (startloc, endloc, distance) "
                        +
                        "VALUES ('?', '?', '?')";
                try (PreparedStatement stmt = con.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, (String) p.getMapValue("startLocation"));
                    stmt.setString(2, (String) p.getMapValue("endLocation"));
                    stmt.setDouble(3, Double.parseDouble((String) p.getMapValue("distance")));
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    view.map(rs, routeMapper);
                }
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        if (!errorView.getList().isEmpty()) {
            return errorView;
        }
        return view;
    }
}
