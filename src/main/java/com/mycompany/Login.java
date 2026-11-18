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
    // Username must contain an underscore and be no more than 5 characters
    public boolean checkUserName() {
        return username.indexOf('_') >= 0 && username.length() <= 5;
    }

    // Password must have 8+ characters, a capital, a number and a special character
    public boolean checkPasswordComplexity() {
        boolean longEnough = password.length() >= 8;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return longEnough && hasCapital && hasNumber && hasSpecial;
    }

    // Cell number: starts with +, country code, then up to 10 digits
    // Reference: (add your reference here, see note below)
    public boolean checkCellPhoneNumber() {
        return cellNumber.matches("^\\+\\d{1,3}\\d{1,10}$");
    }
}
