
import java.sql.*;
import java.util.Scanner;

public class UserManagement {

    static final String URL =
        "jdbc:mysql://localhost:3306/user_management";
    static final String USER = "root";
    static final String PASSWORD = "your_mysql_password";

    static Connection con;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            con = DriverManager.getConnection(
                URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

            while (true) {
                System.out.println("\n===== USER MANAGEMENT SYSTEM =====");
                System.out.println("1. Add User");
                System.out.println("2. View Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        addUser(sc);
                        break;
                    case 2:
                        viewUsers();
                        break;
                    case 3:
                        updateUser(sc);
                        break;
                    case 4:
                        deleteUser(sc);
                        break;
                    case 5:
                        System.out.println("Program terminated.");
                        con.close();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    // CREATE
    static void addUser(Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        String sql =
            "INSERT INTO users(name, email, phone) VALUES(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                ? "User added successfully!"
                : "User was not added.");
        }
    }

    // READ
    static void viewUsers() throws SQLException {
        String sql = "SELECT * FROM users";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID\tName\tEmail\t\tPhone");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + "\t" +
                    rs.getString("name") + "\t" +
                    rs.getString("email") + "\t" +
                    rs.getString("phone"));
            }
        }
    }

    // UPDATE
    static void updateUser(Scanner sc) throws SQLException {
        System.out.print("Enter user ID to update: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new email: ");
        String email = sc.nextLine();

        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();

        String sql =
            "UPDATE users SET name=?, email=?, phone=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                ? "User updated successfully!"
                : "User ID not found.");
        }
    }

    // DELETE
    static void deleteUser(Scanner sc) throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM users WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                ? "User deleted successfully!"
                : "User ID not found.");
        }
    }
}
