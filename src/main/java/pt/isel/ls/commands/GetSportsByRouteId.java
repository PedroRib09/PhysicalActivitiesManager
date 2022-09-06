package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetSportsByRouteIdView;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.SportMapper;
import pt.isel.ls.model.Error;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSportsByRouteId implements CommandHandler {
    private SportMapper sportMapper = new SportMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetSportsByRouteIdView view = new GetSportsByRouteIdView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();
            ResultSet rs;
            String s = "select s.name,s.description,s.sid FROM (route r join activity a on r.rid = a.uid "
                    + "JOIN sport s on a.sid=s.sid)"
                    + " WHERE r.rid=?";
            try (PreparedStatement stmt = con.prepareStatement(s)) {
                stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("routes")));
                rs = stmt.executeQuery();
                view.map(rs, sportMapper);
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
