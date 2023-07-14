package dao.impl;

import dao.ContactDAO;
import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ContactDAOImplementation implements ContactDAO {
    String url = "jdbc:mysql://localhost:3306/agenda2";
    String username = "root";
    String password = "admin";
    @Override
    public void createContact(Contact contact) {
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            String createSQL = "INSERT INTO contact (firstName, lastName, phoneNumber, email) VALUES (?,?,?,?)";

            try(PreparedStatement ps = connection.prepareStatement(createSQL)){
                ps.setString(1, contact.firstName);
                ps.setString(2, contact.lastName);
                ps.setString(3, contact.phoneNumber);
                ps.setString(4, contact.email);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> showAll() {
        List<Contact> contactList = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String showAllQuery = "SELECT * FROM contact";

            try (PreparedStatement st = connection.prepareStatement(showAllQuery)){
                rs = st.executeQuery();

                contactList = mapResultSet(rs);

                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    @Override
    public List<Contact> getByFirstName(Contact contact) {
        List<Contact> contactList = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String byNameSQL = "SELECT * FROM contact WHERE firstName LIKE CONCAT('%',?,'%')";

            try (PreparedStatement st = connection.prepareStatement(byNameSQL)){
                st.setString(1, contact.firstName);

                rs = st.executeQuery();

                contactList = mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contactList;
    }

    @Override
    public List<Contact> getByLastName(Contact contact) {
        List<Contact> contactList = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String lastNameSQL = "SELECT * FROM contact WHERE lastName LIKE CONCAT('%',?,'%')";

            try (PreparedStatement st = connection.prepareStatement(lastNameSQL)){
                st.setString(1, contact.lastName);

                rs = st.executeQuery();

                contactList = mapResultSet(rs);
            }
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
            throw new RuntimeException("Error getting last name");
        } catch (SQLException sqlException) {
            if (sqlException instanceof SQLSyntaxErrorException) {

            }
            Logger.getAnonymousLogger().info("TESTING");
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contactList;
    }

    @Override
    public List<Contact> getByPhoneNumber(Contact contact) {
        List<Contact> contactList = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            String phoneNumberSQL = "SELECT * FROM contact WHERE phoneNumber LIKE CONCAT('%',?,'%')";

            try (PreparedStatement st = connection.prepareStatement(phoneNumberSQL)){
                st.setString(1, contact.phoneNumber);
                rs = st.executeQuery();

                contactList = mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contactList;
    }

    @Override
    public List<Contact> getByEmail(Contact contact) {

        List<Contact> contactList = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String emailSQL = "SELECT * FROM contact where email LIKE CONCAT('%', ?, '%')";

            try (PreparedStatement st = connection.prepareStatement(emailSQL)) {
                st.setString(1, contact.email);
                rs = st.executeQuery();

                return mapResultSet(rs);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contactList;
    }

    @Override
    public int delete(Contact contact) {
        int affectedRows = 0;

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            String deleteSQL = "DELETE FROM contact WHERE phoneNumber LIKE CONCAT('%',?,'%')";

            try (PreparedStatement st = connection.prepareStatement(deleteSQL)){
                st.setString(1, contact.phoneNumber);
                affectedRows = st.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    //result map for list
    private List<Contact> mapResultSet(ResultSet rs) throws SQLException {
        List<Contact> contactList = new ArrayList<>();

        while (rs.next()) {
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String phoneNumber = rs.getString("phoneNumber");
            String email = rs.getString("email");

            Contact contact = new Contact();
            contact.firstName = firstName;
            contact.lastName = lastName;
            contact.phoneNumber = phoneNumber;
            contact.email = email;

            contactList.add(contact);
        }
        return contactList;
    }
}
