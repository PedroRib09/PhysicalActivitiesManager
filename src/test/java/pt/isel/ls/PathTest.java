package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.cmdhandling.Path;
import pt.isel.ls.cmdhandling.PathTemplate;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathTest {

    @Test
    public void isValidPath() {
        Path p = new Path("/users/1/sports/2");
        PathTemplate pt = new PathTemplate("/users/{uid}/sports/{sid}");
        p.extractId(p);
        assertTrue(PathTemplate.isMatch(pt,p));
    }

    @Test
    public void extract2Ids() {
        Path p = new Path("/users/1/sports/2");
        final Path p1 = new Path("/users/activities");

        HashMap<String,Object> pathMap;
        p.extractId(p);
        pathMap = p.getMap();
        assertEquals("1",pathMap.get("users"));
        assertEquals("2",pathMap.get("sports"));

        HashMap<String, Object> pathMap2;
        p1.extractId(p1);
        pathMap2 = p1.getMap();

        assertTrue(pathMap2.containsKey("users"));
        assertTrue(pathMap2.containsKey("activities"));
    }



}
