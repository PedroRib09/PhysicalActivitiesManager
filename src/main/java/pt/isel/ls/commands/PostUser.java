package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.PostUserView;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.mapper.UserMapper;
import pt.isel.ls.model.Error;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostUser implements CommandHandler {
    private UserMapper userMapper = new UserMapper();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PostUserView view = new PostUserView();
        ErrorView errorView = new ErrorView();

        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            Parameters p = commandRequest.getParameters();
            if (!(ParameterCheck.checkEmail(p.getMapValue("email")))) {
                errorView.addToList(new Error("Email is not valid!"));
            }

            if (!(ParameterCheck.checkName(p.getMapValue("name")))) {
                errorView.addToList(new Error("Name is not valid!"));
            }
            String helper = "SELECT * FROM users WHERE email = ? ";

            PreparedStatement stmt = con.prepareStatement(helper, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, (String) p.getMapValue("email"));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                errorView.addToList(new Error("That email has already been registered."));
            }
            stmt.close();
            if (errorView.getList().isEmpty()) {
                String s = "INSERT INTO users (name, email) " + "VALUES "
                        +
                        "('" + p.getMapValue("name")
                        + "', '" + p.getMapValue("email")
                        + "')";

                stmt = con.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                view.map(rs, userMapper);
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