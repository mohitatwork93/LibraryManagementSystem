package com.mohit.LMS;

import java.util.Scanner;

public class LibraryManagementSystemApp 
{

    private static LibraryMenuActions menu;
    private static Library library;
    private static Scanner sc;

    public static void main(String[] args) 
    {

        sc = new Scanner(System.in);
        library = new Library();
        menu = new LibraryMenuActions(library, sc);

        boolean loop = true;

        while (loop) 
        {

            System.out.println("\n====== LIBRARY MENU ======");
            System.out.println("1. Add Book");
            System.out.println("2. Add User");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Display Books");
            System.out.println("6. Display Users");
            System.out.println("7. Display Issued Books");
            System.out.println("8. Update Book");
            System.out.println("9. Update User");
            System.out.println("10. Search Book");
            System.out.println("11. Search User");
            System.out.println("12. Show Book Issue History");
            System.out.println("13. Count Books");
            System.out.println("14. Sort Books");
            System.out.println("0. Exit");

            System.out.print("Choose Option: ");
            String choice = sc.next();
            int op;

            try 
            {
                op = Integer.parseInt(choice);
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Invalid choice! Enter a number.");
                continue;
            }

            switch (op) 
            {
                case 1 : menu.addBook();
                break;
                case 2 : menu.addUser();
                break;
                case 3 : menu.issueBook();
                break;
                case 4 : menu.returnBook();
                break;
                case 5 : library.displayBooks();
                break;
                case 6 : library.displayUsers();
                break;
                case 7 : library.displayIssuedBooks();
                break;
                case 8 : menu.updateBook();
                break;
                case 9 : menu.updateUser();
                break;
                case 10 : menu.searchBook();
                break;
                case 11 : menu.searchUser();
                break;
                case 12 : menu.bookHistory();
                break;
                case 13 : library.countBooks();
                break;
                case 14 : menu.sortBooks();
                break;
                case 0 : {
		                    System.out.println("Exiting...");
		                    sc.close();
		                    loop = false;
		                 }
                break;
                default : System.out.println("Invalid Choice!");
            }
        }
    }
}
