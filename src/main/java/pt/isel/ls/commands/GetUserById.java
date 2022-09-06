package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetUserByIdView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.UserMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserById implements CommandHandler {
    private UserMapper userMapper = new UserMapper();
    ErrorView errorView = new ErrorView();

    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetUserByIdView view = new GetUserByIdView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();

            if (!ParameterCheck.isNumber(r.getKeyValue("users"))) {
                errorView.addToList(new Error("Id is not an Integer!"));
            }

            ResultSet rs;
            String s = "select * FROM users where uid =?";
            try (PreparedStatement stmt = con.prepareStatement(s)) {
                stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("users")));
                rs = stmt.executeQuery();
                view.map(rs, userMapper);
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
