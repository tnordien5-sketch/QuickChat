package com.mycompany;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        Login login = new Login(firstName, lastName);

        boolean valid = false;
        while (!valid) {
            System.out.print("Enter a username: ");
            login.setUsername(input.nextLine());
            System.out.println(login.usernameMessage());
            valid = login.checkUserName();
        }

        valid = false;
        while (!valid) {
            System.out.print("Enter a password: ");
            login.setPassword(input.nextLine());
            System.out.println(login.passwordMessage());
            valid = login.checkPasswordComplexity();
        }

        valid = false;
        while (!valid) {
            System.out.print("Enter your cell number (e.g. +27838968976): ");
            login.setCellNumber(input.nextLine());
            System.out.println(login.cellPhoneMessage());
            valid = login.checkCellPhoneNumber();
        }

        System.out.println(login.registerUser());

        System.out.println("=== Login ===");
        System.out.print("Enter your username: ");
        String enteredUser = input.nextLine();
        System.out.print("Enter your password: ");
        String enteredPass = input.nextLine();

        boolean loggedIn = login.loginUser(enteredUser, enteredPass);
        System.out.println(login.returnLoginStatus(loggedIn));
    }
}