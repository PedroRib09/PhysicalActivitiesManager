package pt.isel.ls.cmdhandling;

import java.sql.SQLException;

public interface CommandHandler {
    CommandResult execute(CommandRequest commandRequest) throws SQLException;
}
