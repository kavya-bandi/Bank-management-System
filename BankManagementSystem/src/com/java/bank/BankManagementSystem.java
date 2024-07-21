package com.java.bank;

import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public static void main(String[] args) throws ClassNotFoundException{
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database.");

            while (true) {
                System.out.println("Bank Management System");
                System.out.println("1. Add Customer");
                System.out.println("2. Add Account");
                System.out.println("3. View Customer");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addCustomer(conn, scanner);
                        break;
                    case 2:
                        addAccount(conn, scanner);
                        break;
                    case 3:
                        viewCustomer(conn, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addCustomer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO Customers (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
        }
    }

    private static void addAccount(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter account type: ");
        String accountType = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();

        String sql = "INSERT INTO Accounts (CustomerID, AccountType, Balance) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, accountType);
            pstmt.setDouble(3, balance);
            pstmt.executeUpdate();
            System.out.println("Account added successfully.");
        }
    }

    private static void viewCustomer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Customer ID: " + rs.getInt("CustomerID"));
                    System.out.println("Name: " + rs.getString("Name"));
                    System.out.println("Address: " + rs.getString("Address"));
                    System.out.println("Phone: " + rs.getString("Phone"));
                    System.out.println("Email: " + rs.getString("Email"));
                } else {
                    System.out.println("Customer not found.");
                }
            }
        }
    }
}

