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

        // Only logged-in users may send messages
        if (loggedIn) {
            runQuickChat(input);
        }
    }

    // Keeps asking until the user types a whole number
    private static int readNumber(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private static void runQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        int maxMessages = readNumber(input, "How many messages do you want to enter? ");
        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int choice = readNumber(input, "Choose an option: ");

            if (choice == 1) {
                if (entered >= maxMessages) {
                    System.out.println("You have already entered all of your messages.");
                } else {
                    String id = Message.generateMessageID();
                    Message m = null;

                    while (m == null) {
                        System.out.print("Enter the recipient's cell number: ");
                        String recipient = input.nextLine();
                        System.out.print("Enter your message: ");
                        String text = input.nextLine();

                        Message candidate = new Message(id, entered, recipient, text);
                        String cellResult = candidate.checkRecipientCell();
                        String lengthResult = candidate.checkMessageLength();

                        if (!cellResult.startsWith("Cell phone number successfully")) {
                            System.out.println(cellResult);
                        } else if (!lengthResult.equals("Message ready to send.")) {
                            System.out.println(lengthResult);
                        } else {
                            System.out.println(lengthResult);
                            m = candidate;
                        }
                    }

                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");
                    int action = readNumber(input, "Choose an option: ");
                    System.out.println(m.sentMessage(action));

                    if (action == 1) {
                        System.out.println("Message ID: " + m.getMessageID());
                        System.out.println("Message Hash: " + m.getMessageHash());
                        System.out.println("Recipient: " + m.getRecipient());
                        System.out.println("Message: " + m.getMessageText());
                    } else if (action == 3) {
                        Message.storeMessage();
                    }
                    entered++;
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                running = false;
            } else {
                System.out.println("Please choose 1, 2 or 3.");
            }
        }

        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }
}