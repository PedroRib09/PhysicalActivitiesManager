package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetTopsActivitiesView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.mapper.ActivityMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetTopsActivities implements CommandHandler {
    private ActivityMapper activityMapper = new ActivityMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetTopsActivitiesView view = new GetTopsActivitiesView();

        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            Parameters p = commandRequest.getParameters();

            String order = p.getMapValue("orderBy").equals("ascending") ? "ASC" : "DESC";
            String date = "";
            String rid = "";
            if (p.getMapValue("date") != null) {
                date = " AND a.date = " + p.getMapValue("date") + " ";
            }
            if (p.getMapValue("rid") != null) {
                rid = " AND a.rid = " + p.getMapValue("rid") + " ";
            }
            String s = "select * from activity a where sid =" + p.getMapValue("sid")
                    + date + rid + " ORDER BY duration " + order;
            if (p.getMapValue("distance") != null) {
                s = "select a.aid,a.duration,a.date,a.uid,a.rid,a.sid "
                        +
                        "from (activity a join route r on a.rid=r.rid) where a.sid = "
                        + p.getMapValue("sid")
                        + date
                        + rid
                        + "and r.distance>=" + p.getMapValue("distance") + " ORDER BY a.duration "
                        +
                        order;
            }

            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(s);
            view.map(rs, activityMapper);

        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
