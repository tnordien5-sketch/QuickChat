package com.mycompany;

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
        // Reference: Oracle (n.d.) Lesson: Regular Expressions. The Java Tutorials. Available at: https://docs.oracle.com/javase/tutorial/essential/regex/ (Accessed: 18 November 2026).
    public boolean checkCellPhoneNumber() {
        return cellNumber.matches("^\\+\\d{1,3}\\d{1,10}$");
    }
    public String usernameMessage() {
        if (checkUserName()) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username "
             + "contains an underscore and is no more than five characters in length.";
    }

    public String passwordMessage() {
        if (checkPasswordComplexity()) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password "
             + "contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String cellPhoneMessage() {
        if (checkCellPhoneNumber()) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; "
             + "please correct the number and try again.";
    }

    public String registerUser() {
        if (!checkUserName()) {
            return usernameMessage();
        }
        if (!checkPasswordComplexity()) {
            return passwordMessage();
        }
        if (!checkCellPhoneNumber()) {
            return cellPhoneMessage();
        }
        return "User has been registered successfully.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
