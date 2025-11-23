package com.mohit.LMS;

import java.sql.*;
import java.time.LocalDate;

public class Library 
{

    // ========== ADD BOOK ==========
    public void addBook(Book book) 
    {
        String sql = "INSERT INTO books (id, title, author, release_date, is_issued) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            if (book.getId() <= 0) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, book.getId());

            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setDate(4, Date.valueOf(book.getReleaseDate()));
            ps.setBoolean(5, false);

            ps.executeUpdate();
            System.out.println("Book added: " + book);

        } 
        catch (SQLException e) 
        {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    // ========== ADD USER ==========
    public void addUser(User user) 
    {
        String sql = "INSERT INTO users (id, name, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            if (user.getId() <= 0) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, user.getId());

            ps.setString(2, user.getName());
            ps.setString(3, user.getPhone());

            ps.executeUpdate();
            System.out.println("User added: " + user);

        } 
        catch (SQLException e) 
        {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // ========== ISSUE BOOK ==========
    public void issueBook(int bookId, int userId) 
    {
        try (Connection conn = DBConnection.getConnection()) 
        {

            // check availability
            PreparedStatement ps1 = conn.prepareStatement("SELECT is_issued FROM books WHERE id=?");
            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) 
            {
                System.out.println("Book not found!");
                return;
            }

            if (rs.getBoolean("is_issued")) 
            {
                System.out.println("Book already issued!");
                return;
            }

            // mark issued
            PreparedStatement ps2 = conn.prepareStatement("UPDATE books SET is_issued=true WHERE id=?");
            ps2.setInt(1, bookId);
            ps2.executeUpdate();

            // insert into issued_books
            PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO issued_books (book_id, user_id, issue_date) VALUES (?, ?, ?)");

            ps3.setInt(1, bookId);
            ps3.setInt(2, userId);
            ps3.setDate(3, Date.valueOf(LocalDate.now()));

            ps3.executeUpdate();

            System.out.println("Book issued Successfully!");

        } 
        catch (SQLException e) 
        {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    // ========== RETURN BOOK ==========
    public void returnBook(int bookId) 
    {
        try (Connection conn = DBConnection.getConnection()) 
        {

            PreparedStatement ps1 = conn.prepareStatement("SELECT is_issued FROM books WHERE id=?");
            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) 
            {
                System.out.println("Book not found!");
                return;
            }

            if (!rs.getBoolean("is_issued")) 
            {
                System.out.println("Book is not issued!");
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement("UPDATE books SET is_issued=false WHERE id=?");
            ps2.setInt(1, bookId);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE issued_books SET return_date=? WHERE book_id=? AND return_date IS NULL");

            ps3.setDate(1, Date.valueOf(LocalDate.now()));
            ps3.setInt(2, bookId);

            ps3.executeUpdate();

            System.out.println("Book returned successfully!");

        } 
        catch (SQLException e) 
        {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    // ========== DISPLAY BOOKS ==========
    public void displayBooks() 
    {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) 
        {

            ResultSet rs = st.executeQuery("SELECT * FROM books");
            System.out.println("Books:");
            System.out.printf("\n%-5s %-60s %-25s %-15s %-10s\n",
                    "ID", "Title", "Author", "R. Date", "Status");
            System.out.println("===================++=================================================================================================");
            while (rs.next()) 
            {
                String status = rs.getBoolean("is_issued") ? "Issued" : "Available";

                System.out.printf("%-5d %-60s %-25s %-15s %-10s\n", rs.getInt("id"), rs.getString("title"), rs.getString("author"), String.valueOf(rs.getDate("release_date")), status);
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error displaying books: " + e.getMessage());
        }
    }

    // ========== DISPLAY USERS ==========
    public void displayUsers() 
    {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) 
        {

            ResultSet rs = st.executeQuery("SELECT * FROM users");
            System.out.println("Users:");
            System.out.printf("\n%-5s %-25s %-15s\n", "ID", "Name", "Phone");

            while (rs.next()) 
            {
                System.out.printf("%-5d %-25s %-15s\n", rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error displaying users: " + e.getMessage());
        }
    }

    // ========== DISPLAY ISSUED BOOKS ==========
    public void displayIssuedBooks() 
    {
        String sql = "SELECT b.id AS book_id, b.title, u.name, i.issue_date " +
                "FROM issued_books i " +
                "JOIN books b ON i.book_id=b.id " +
                "JOIN users u ON i.user_id=u.id " +
                "WHERE i.return_date IS NULL";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) 
        {

            ResultSet rs = st.executeQuery(sql);

            System.out.println("Issued Books:");
            System.out.printf("\n%-8s %-30s %-25s %-15s\n",
                    "BookID", "Title", "User Name", "Issue Date");

            while (rs.next()) 
            {
                System.out.printf("%-8d %-30s %-25s %-15s\n", rs.getInt("book_id"), rs.getString("title"), rs.getString("name"), String.valueOf(rs.getDate("issue_date")));
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error displaying issued books: " + e.getMessage());
        }
    }

    // ========== UPDATE BOOK ==========
    public void updateBook(int id, String title, String author, LocalDate date) 
    {
        String sql = "UPDATE books SET title=?, author=?, release_date=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDate(3, Date.valueOf(date));
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? "Book updated!" : "Book not found!");

        } 
        catch (SQLException e) 
        {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    // ========== UPDATE USER ==========
    public void updateUser(int id, String name, String phone) {
        String sql = "UPDATE users SET name=?, phone=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? "User updated!" : "User not found!");

        } 
        catch (SQLException e) 
        {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    // ========== SEARCH BOOK ==========
    public void searchBook(String key) 
    {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            String like = "%" + key + "%";

            ps.setString(1, like);
            ps.setString(2, like);

            ResultSet rs = ps.executeQuery();

            System.out.println("Search Results:");
            System.out.printf("\n%-5s %-30s %-25s %-15s\n",
                    "ID", "Title", "Author", "Release Date");

            while (rs.next()) 
            {
                System.out.printf("%-5d %-30s %-25s %-15s\n", rs.getInt("id"), rs.getString("title"), rs.getString("author"), String.valueOf(rs.getDate("release_date")));
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }

    // ========== SEARCH USER ==========
    public void searchUser(String key) 
    {
        String sql = "SELECT * FROM users WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setString(1, "%" + key + "%");

            ResultSet rs = ps.executeQuery();

            System.out.printf("\n%-5s %-25s %-15s\n", "ID", "Name", "Phone");

            while (rs.next()) 
            {
                System.out.printf("%-5d %-25s %-15s\n", rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error searching users: " + e.getMessage());
        }
    }

    // ========== ISSUE HISTORY ==========
    public void showBookHistory(int bookId) 
    {
        String sql = """
                SELECT u.name, u.phone, i.issue_date, i.return_date
                FROM issued_books i
                JOIN users u ON i.user_id = u.id
                WHERE i.book_id = ?
                ORDER BY i.issue_date DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            System.out.println("Book History:");
            System.out.printf("\n%-25s %-15s %-15s %-15s\n",
                    "User Name", "Phone", "Issue Date", "Return Date");

            while (rs.next()) 
            {
                System.out.printf("%-25s %-15s %-15s %-15s\n", rs.getString("name"), rs.getString("phone"), String.valueOf(rs.getDate("issue_date")), String.valueOf(rs.getDate("return_date")));
            }

        } 
        catch (SQLException e) 
        {
            System.out.println("Error fetching history: " + e.getMessage());
        }
    }

    // ========== COUNT BOOKS ==========
    public void countBooks() 
    {
        String sql = """
                SELECT COUNT(*) AS total,
                       SUM(is_issued = 0) AS available,
                       SUM(is_issued = 1) AS issued
                FROM books
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) 
        {

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) 
            {
                System.out.println("Total Books: " + rs.getInt("total"));
                System.out.println("Available: " + rs.getInt("available"));
                System.out.println("Issued: " + rs.getInt("issued"));
            }

        } 
        catch (SQLException e) 
        {
            System.out.println("Error counting: " + e.getMessage());
        }
    }

    // ========== SORT BOOKS ==========
    public void sortBooks(String column) 
    {
        if (!column.equals("title") && !column.equals("author") && !column.equals("release_date")) 
        {
            System.out.println("Invalid sort column!");
            return;
        }

        String sql = "SELECT * FROM books ORDER BY " + column;

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) 
        {

            ResultSet rs = st.executeQuery(sql);

            System.out.println("Sorted Books:");
            System.out.printf("\n%-5s %-30s %-25s %-15s\n",
                    "ID", "Title", "Author", "Release Date");

            while (rs.next()) 
            {
                System.out.printf("%-5d %-30s %-25s %-15s\n",rs.getInt("id"), rs.getString("title"), rs.getString("author"), String.valueOf(rs.getDate("release_date")));
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error sorting: " + e.getMessage());
        }
    }
}
