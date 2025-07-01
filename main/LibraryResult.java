package main;

public class LibraryResult {
    private boolean success;
    private String message;

    public LibraryResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
