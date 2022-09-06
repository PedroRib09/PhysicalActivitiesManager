package pt.isel.ls.commands;


public class ParameterCheck {

    public static boolean checkName(Object nameValue) {
        String name;

        if (!isString(nameValue)) {
            return false;
        }

        name = (String) nameValue;
        if (name.length() <= 1) {
            return false;
        }

        return checkLetters(name);
    }

    public static boolean checkEmail(Object emailValue) {
        String email;

        try {
            email = (String) emailValue;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return email.contains("@");
    }

    public static boolean checkDuration(Object dur) {

        if (!isString(dur)) {
            return false;
        }

        String duration = (String) dur;

        if (!checkNumbers(duration)) {
            return false;
        }

        String[] str = duration.split(":");

        if (str.length != 3) {
            return false;
        }

        String[] str2 = str[2].split("\\.");

        if (str2.length != 2) {
            return false;
        }

        for (int i = 0; i < str.length - 1; i++) {
            if (str[i].length() != 2) {
                return false;
            }
        }

        return str2[0].length() == 2 && str2[1].length() == 3;
    }


    public static boolean checkDate(Object dt) {
        if (!isString(dt)) {
            return false;
        }

        String date = (String) dt;
        if (!checkNumbers(date)) {
            return false;
        }

        String[] str = date.split("-");
        if (str.length != 3) {
            return false;
        }

        return str[0].length() == 4 && str[1].length() == 2 && str[2].length() == 2;
    }


    public static boolean isString(Object s) {
        return s instanceof String;
    }

    public static boolean isNumber(Object n) {

        try {
            Double.parseDouble(n.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean checkLetters(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (!((str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
                    || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                    || str.charAt(i) == ' ')) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkNumbers(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (!((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == ':' || str.charAt(i) == '-'
                    || str.charAt(i) == '.')) {
                return false;
            }
        }
        return true;
    }

}
