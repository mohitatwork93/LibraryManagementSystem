package com.mohit.LMS;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class LibraryMenuActions 
{

    private final Library library;
    private final Scanner sc;

    public LibraryMenuActions(Library library, Scanner sc) 
    {
        this.library = library;
        this.sc = sc;
    }

    // ===== Helper Methods =====

    private int readInt(String msg) 
    {
        while (true) 
        {
            System.out.print(msg);
            try 
            {
                return Integer.parseInt(sc.next());
            } 
            catch (Exception e) 
            {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private String readString(String msg) 
    {
        System.out.print(msg);
        return sc.next();
    }

    private String readLine(String msg) 
    {
        System.out.print(msg);
        sc.nextLine();
        return sc.nextLine();
    }

    private LocalDate readDate(String msg) 
    {
        System.out.print(msg);
        String input = sc.next();
        try 
        {
            return LocalDate.parse(input);
        } 
        catch (DateTimeParseException e) 
        {
            System.out.println("Invalid date format! Use YYYY-MM-DD.");
            return readDate(msg);
        }
    }

    // ===== Menu Functions =====

    public void addBook() 
    {
        System.out.println("\n--- Add Book ---");
        int id = readInt("Enter Book ID (0 auto): ");
        String title = readLine("Enter Title: ");
        String author = readLine("Enter Author: ");
        LocalDate date = readDate("Enter Release Date (YYYY-MM-DD): ");

        Book b = new Book(id == 0 ? 0 : id, title, author, date);
        library.addBook(b);
    }

    public void addUser() 
    {
        System.out.println("\n--- Add User ---");
        int id = readInt("Enter User ID (0 auto): ");
        String name = readLine("Enter User Name: ");
        String phone = readLine("Enter Phone: ");

        User u = new User(id == 0 ? 0 : id, name, phone);
        library.addUser(u);
    }

    public void issueBook() 
    {
        int bookId = readInt("Enter Book ID: ");
        int userId = readInt("Enter User ID: ");
        library.issueBook(bookId, userId);
    }

    public void returnBook() 
    {
        int bookId = readInt("Enter Book ID: ");
        library.returnBook(bookId);
    }

    public void updateBook() 
    {
        System.out.println("\n--- Update Book ---");
        int id = readInt("Enter Book ID: ");
        String title = readLine("Enter New Title: ");
        String author = readLine("Enter New Author: ");
        LocalDate date = readDate("Enter New Release Date: ");

        library.updateBook(id, title, author, date);
    }

    public void updateUser() 
    {
        int id = readInt("Enter User ID: ");
        String name = readLine("Enter New Name: ");
        String phone = readLine("Enter New Phone: ");

        library.updateUser(id, name, phone);
    }

    public void searchBook() 
    {
        String keyword = readLine("Enter Book Title/Author keyword: ");
        library.searchBook(keyword);
    }

    public void searchUser() 
    {
        String keyword = readLine("Enter User Name keyword: ");
        library.searchUser(keyword);
    }

    public void bookHistory() 
    {
        int bookId = readInt("Enter Book ID: ");
        library.showBookHistory(bookId);
    }

    public void sortBooks() 
    {
        String column = readString("Sort by (title/author/release_date): ");
        library.sortBooks(column);
    }
}
