package com.mycompany.login;

public class Login {
    private String firstName;
    private String lastName;
    private String username = "";
    private String password = "";
    private String cellNumber = "";

    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }
}
