package pt.isel.ls.model;

public class Error {

    private String error;

    public Error(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Error -> " + error;
    }
}
