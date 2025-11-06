import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con.setAutoCommit(false);
            while (true) {
                System.out.println("\n1. Insert Product");
                System.out.println("2. Display All Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter ProductID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter ProductName: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        String insert = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement ps = con.prepareStatement(insert)) {
                            ps.setInt(1, id);
                            ps.setString(2, name);
                            ps.setDouble(3, price);
                            ps.setInt(4, qty);
                            ps.executeUpdate();
                            con.commit();
                            System.out.println("Product inserted successfully.");
                        } catch (SQLException e) {
                            con.rollback();
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        String select = "SELECT * FROM Product";
                        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(select)) {
                            while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("ProductID") +
                                        ", Name: " + rs.getString("ProductName") +
                                        ", Price: " + rs.getDouble("Price") +
                                        ", Quantity: " + rs.getInt("Quantity"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter ProductID to update: ");
                        int uid = sc.nextInt();
                        System.out.print("Enter new Price: ");
                        double newPrice = sc.nextDouble();
                        System.out.print("Enter new Quantity: ");
                        int newQty = sc.nextInt();
                        String update = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
                        try (PreparedStatement ps = con.prepareStatement(update)) {
                            ps.setDouble(1, newPrice);
                            ps.setInt(2, newQty);
                            ps.setInt(3, uid);
                            int rows = ps.executeUpdate();
                            if (rows > 0) {
                                con.commit();
                                System.out.println("Product updated successfully.");
                            } else {
                                con.rollback();
                                System.out.println("No product found with that ID.");
                            }
                        } catch (SQLException e) {
                            con.rollback();
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                        System.out.print("Enter ProductID to delete: ");
                        int did = sc.nextInt();
                        String delete = "DELETE FROM Product WHERE ProductID = ?";
                        try (PreparedStatement ps = con.prepareStatement(delete)) {
                            ps.setInt(1, did);
                            int rows = ps.executeUpdate();
                            if (rows > 0) {
                                con.commit();
                                System.out.println("Product deleted successfully.");
                            } else {
                                con.rollback();
                                System.out.println("No product found with that ID.");
                            }
                        } catch (SQLException e) {
                            con.rollback();
                            e.printStackTrace();
                        }
                        break;

                    case 5:
                        con.close();
                        System.out.println("Exiting program.");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
