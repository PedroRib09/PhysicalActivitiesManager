package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.PostSportView;
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
import java.sql.Statement;

public class PostSport implements CommandHandler {
    private SportMapper sportMapper = new SportMapper();
    ErrorView errorView = new ErrorView();

    @Override

    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PostSportView view = new PostSportView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            Parameters p = commandRequest.getParameters();

            if (!(ParameterCheck.checkName(p.getMapValue("name")))) {
                errorView.addToList(new Error("Name is not valid"));
            }
            if (!(ParameterCheck.isString(p.getMapValue("description")))) {
                errorView.addToList(new Error("Description is not valid"));
            }

            if (errorView.getList().isEmpty()) {
                String s = "INSERT INTO sport (name, description) "
                        +
                        "VALUES('?', '?')";
                try (PreparedStatement stmt = con.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, (String) p.getMapValue("name"));
                    stmt.setString(2, (String) p.getMapValue("description"));
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    view.map(rs, sportMapper);
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