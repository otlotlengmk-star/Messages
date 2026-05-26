package com.mycompany.messages;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Messages {

    static Scanner input = new Scanner(System.in);

    static String[] sentMessages = new String[100];
    static int totalMessagesSent = 0;

    public static void main(String[] args) {

        int option;

        System.out.println("=================================");
        System.out.println(" QUICKCHAT SYSTEM");
        System.out.println("=================================");

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose option: ");

        option = input.nextInt();
        input.nextLine();

        switch (option) {

            case 1:

                String registerResult = registerUser(input);
                System.out.println(registerResult);
                break;

            case 2:

                System.out.print("Enter username: ");
                String username = input.nextLine();

                System.out.print("Enter password: ");
                String password = input.nextLine();

                boolean loginStatus = loginUser(username, password);

                System.out.println(
                        returnLoginStatus(loginStatus, username)
                );

                if (loginStatus) {

                    System.out.println("\nWelcome to QuickChat.");

                    System.out.print("How many messages would you like to send? ");
                    int numberOfMessages = input.nextInt();
                    input.nextLine();

                    if (numberOfMessages <= 0 || numberOfMessages > 250) {

                        System.out.println(
                                "Message exceeds 250 characters please reduce the size."
                        );

                        return;
                    }

                    System.out.println("Message ready to send.");

                    int sentCount = 0;

                    while (true) {

                        System.out.println("\n========= MENU =========");
                        System.out.println("1. Send Messages");
                        System.out.println("2. Show recently sent messages");
                        System.out.println("3. Quit");

                        System.out.print("Choose option: ");
                        int menuOption = input.nextInt();
                        input.nextLine();

                        switch (menuOption) {

                            case 1:

                                if (sentCount < numberOfMessages) {

                                    sendMessage(sentCount + 1);
                                    sentCount++;

                                } else {

                                    System.out.println("Message limit reached.");
                                }

                                break;

                            case 2:

                                System.out.println(printMessages());
                                break;

                            case 3:

                                System.out.println("\nTotal messages sent: " + totalMessagesSent);
                                System.out.println("Goodbye.");
                                return;

                            default:

                                System.out.println("Invalid option.");
                        }
                    }
                }

                break;

            default:

                System.out.println("Invalid option.");
        }
    }

    // Validating the username

    public static boolean checkUserName(String username) {
        return username.matches("^(?=.*_).{1,5}$");
    }

    public static boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(regex);
    }

    public static boolean checkCellPhoneNumber(String cellphone) {
        return cellphone.matches("^\\+27\\d{9}$");
    }

    // Registering the user

    public static String registerUser(Scanner input) {

        String username, password, cellphone;

        System.out.println("\n======= REGISTER =======");

        System.out.print("Enter username: ");
        username = input.nextLine();

        if (!checkUserName(username)) {
            return "Username is not correctly formatted.";
        }

        System.out.println("Username successfully captured.");

        System.out.print("Enter password: ");
        password = input.nextLine();

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted.";
        }

        System.out.println("Password successfully captured.");

        System.out.print("Enter cell phone number: ");
        cellphone = input.nextLine();

        if (!checkCellPhoneNumber(cellphone)) {
            return "Cell phone number incorrectly formatted.";
        }

        System.out.println("Cell phone number successfully added.");

        try {

            FileWriter writer = new FileWriter("users.txt", true);

            writer.write(username + "," + password + "," + cellphone + "\n");

            writer.close();

        } catch (IOException e) {
            return "Error writing to file.";
        }

        return "User has been registered successfully.";
    }

    // Login for the user

    public static boolean loginUser(String username, String password) {

        boolean found = false;

        try {

            File file = new File("users.txt");
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length >= 2) {

                    if (data[0].equals(username) && data[1].equals(password)) {
                        found = true;
                    }
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        return found;
    }

    public static String returnLoginStatus(boolean loginStatus, String username) {

        if (loginStatus) {
            return "Welcome " + username + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Sending messages

    public static void sendMessage(int messageNumber) {

        Random random = new Random();

        long messageID = 1000000000L + (long)(random.nextDouble() * 9000000000L);

        System.out.println("\nMessage number: " + messageNumber);

        System.out.print("Enter recipient cell number: ");
        String recipient = input.nextLine();

        if (!checkCellPhoneNumber(recipient)) {
            System.out.println("Cell phone number incorrectly formatted.");
            return;
        }

        System.out.print("Enter your message: ");
        String message = input.nextLine();

        if (message.length() > 250) {
            System.out.println("Message not sent: please enter a message of less than 250 characters.");
            return;
        }

        System.out.println("Message successfully captured.");

        String messageHash = createMessageHash(
                String.valueOf(messageID),
                messageNumber,
                message
        );

        System.out.println("\n======= MESSAGE DETAILS =======");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);

        System.out.println("\n1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");

        System.out.print("Choose option: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {

            case 1:

                System.out.println("Message successfully sent.");

                sentMessages[totalMessagesSent] = message;
                totalMessagesSent++;

                break;

            case 2:

                System.out.println("Message deleted.");
                break;

            case 3:

                storeMessageJSON(messageID, recipient, message, messageHash);
                System.out.println("Message stored successfully.");
                break;

            default:

                System.out.println("Invalid option.");
        }
    }

    // Creat the message hash

    public static String createMessageHash(String messageID, int messageNumber, String message) {

        String firstTwo = messageID.substring(0, 2);

        String[] words = message.split(" ");

        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        return firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    // Storing the JSON 

    public static void storeMessageJSON(long messageID, String recipient, String message, String messageHash) {

        try {

            FileWriter writer = new FileWriter("messages.json", true);

            writer.write("[\n");
            writer.write("{\n");
            writer.write("\"MessageID\": \"" + messageID + "\",\n");
            writer.write("\"MessageHash\": \"" + messageHash + "\",\n");
            writer.write("\"Recipient\": \"" + recipient + "\",\n");
            writer.write("\"Message\": \"" + message + "\"\n");
            writer.write("}\n");
            writer.write("]\n");

            writer.close();

        } catch (IOException e) {
            System.out.println("Error storing message.");
        }
    }

    //  Printing messages

    public static String printMessages() {

        String output = "";

        for (int i = 0; i < totalMessagesSent; i++) {
            output += sentMessages[i] + "\n";
        }

        return output;
    }
}