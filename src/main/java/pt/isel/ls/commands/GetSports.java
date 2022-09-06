package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetSportsView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.mapper.SportMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSports implements CommandHandler {
    private SportMapper sportMapper = new SportMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetSportsView view = new GetSportsView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            System.out.println(con);
            Parameters p = commandRequest.getParameters();
            String s;

            PreparedStatement stmt;
            if (p != null) {
                s = "select * FROM sport OFFSET ? LIMIT ?";
                stmt = con.prepareStatement(s);
                stmt.setInt(1, Integer.parseInt((String) p.getMapValue("skip")));
                stmt.setInt(2, Integer.parseInt((String) p.getMapValue("top")));
            } else {
                s = "select * FROM sport";
                stmt = con.prepareStatement(s);
            }
            ResultSet rs;
            rs = stmt.executeQuery();
            view.map(rs, sportMapper);
            stmt.close();

        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
