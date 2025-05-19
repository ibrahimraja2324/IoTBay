package iotbay.model;

public class Log {
    private int logID;
    private String email;
    private String action;
    private String role;
    private String time;

    public Log() {
    }

    public Log(int logID, String email, String action, String role, String time) {
        this.logID = logID;
        this.email = email;
        this.action = action;
        this.role = role;
        this.time = time;
    }

    public Log(String email, String action, String role) {
        this.email = email;
        this.action = action;
        this.role = role;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
