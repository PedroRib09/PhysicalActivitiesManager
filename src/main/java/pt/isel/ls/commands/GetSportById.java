package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetSportByIdView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.SportMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSportById implements CommandHandler {
    private SportMapper sportMapper = new SportMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetSportByIdView view = new GetSportByIdView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();

            if (!ParameterCheck.isNumber(r.getKeyValue("sports"))) {
                errorView.addToList(new Error("Id is not an Integer!"));
            }

            ResultSet rs;
            String s = "select * FROM sport where sid = ?";
            PreparedStatement stmt = con.prepareStatement(s);
            stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("sports")));
            rs = stmt.executeQuery();
            view.map(rs, sportMapper);
            stmt.close();
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}