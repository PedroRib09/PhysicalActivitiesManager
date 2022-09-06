package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetUsersView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.mapper.UserMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GetUsers implements CommandHandler {
    private UserMapper userMapper = new UserMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetUsersView view = new GetUsersView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {

            Parameters p = commandRequest.getParameters();
            String s;
            PreparedStatement stmt;
            if (p != null) {
                s = "select * FROM users OFFSET ? LIMIT ?";
                stmt = con.prepareStatement(s);
                stmt.setInt(1, Integer.parseInt((String) p.getMapValue("skip")));
                stmt.setInt(2, Integer.parseInt((String) p.getMapValue("top")));
            } else {
                s = "select * FROM users ";
                stmt = con.prepareStatement(s);
            }
            ResultSet rs;
            rs = stmt.executeQuery();
            view.map(rs, userMapper);

        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}