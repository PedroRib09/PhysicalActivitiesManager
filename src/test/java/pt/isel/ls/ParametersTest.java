package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.commands.ParameterCheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParametersTest {

    @Test
    public void validDuration() {
        String str = "12:21:32.412";
        assertTrue(ParameterCheck.checkDuration(str));

        String str2 = "12:41:21.22";
        assertFalse(ParameterCheck.checkDuration(str2));
    }

    @Test
    public void validDate() {
        String str = "2021-04-24";
        assertTrue(ParameterCheck.checkDate(str));

        String str2 = "aaaa-aa-aa";
        assertFalse(ParameterCheck.checkDate(str2));
    }

    @Test
    public void validEmail() {
        String str = "abcgmail.com";
        assertFalse(ParameterCheck.checkName(str));

        String str2 = "abc@gmailcom";
        assertTrue(ParameterCheck.checkEmail(str2));

        String str3 = "abc@isel.com";
        assertTrue(ParameterCheck.checkEmail(str3));
    }

    @Test
    public void validName() {
        String str = "Joao21";
        assertFalse(ParameterCheck.checkName(str));

        String str2 = "Jo@a";
        assertFalse(ParameterCheck.checkName(str2));

        String str3 = "Joao";
        assertTrue(ParameterCheck.checkName(str3));
    }


}
