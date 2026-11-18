package com.mycompany;

import java.util.ArrayList;
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
            Message.setCurrentSender(login.getCellNumber());
            Message.loadStoredMessages();
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
            System.out.println("4) Stored Messages");
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
                        System.out.println("Message Hash: " + m.getMessageHash());
                        Message.storeMessage();
                    }
                    entered++;
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                running = false;
            } else if (choice == 4) {
                storedMessagesMenu(input);
            } else {
                System.out.println("Please choose 1, 2, 3 or 4.");
            }
        }

        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }

    // The sub-menu for option 4
    private static void storedMessagesMenu(Scanner input) {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("=== Stored Messages ===");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all sent messages");
            System.out.println("7) Back to the main menu");
            int choice = readNumber(input, "Choose an option: ");

            if (choice == 1) {
                System.out.println(Message.displaySenderAndRecipient());
            } else if (choice == 2) {
                System.out.println("Longest message: " + Message.longestMessage());
            } else if (choice == 3) {
                System.out.print("Enter the message ID: ");
                Message found = Message.findByMessageID(input.nextLine());
                if (found == null) {
                    System.out.println("Message ID not found.");
                } else {
                    System.out.println("Recipient: " + found.getRecipient());
                    System.out.println("Message: " + found.getMessageText());
                }
            } else if (choice == 4) {
                System.out.print("Enter the recipient's cell number: ");
                ArrayList<String> results = Message.searchByRecipient(input.nextLine());
                if (results.size() == 0) {
                    System.out.println("No messages found for that recipient.");
                } else {
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                    }
                }
            } else if (choice == 5) {
                System.out.print("Enter the message hash: ");
                System.out.println(Message.deleteByHash(input.nextLine()));
                Message.storeMessage();   // keeps messages.json in step
            } else if (choice == 6) {
                System.out.println(Message.displayReport());
            } else if (choice == 7) {
                back = true;
            } else {
                System.out.println("Please choose a number from 1 to 7.");
            }
        }
    }
}