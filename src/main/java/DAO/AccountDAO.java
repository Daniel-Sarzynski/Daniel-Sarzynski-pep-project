package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    /**
     * Insert a new account into the database
     * @param account the account to insert
     * @return the inserted account with its generated account_id, or null if insertion failed
     */
    public Account insertAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int generated_account_id = rs.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an account by username
     * @param username the username to search for
     * @return the Account if found, null otherwise
     */
    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an account by id
     * @param account_id the user id to search for
     * @return the Account if found, null otherwise
     */
    public Account getAccountById(int account_id) {
    Connection conn = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM Account WHERE account_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setInt(1, account_id);
        
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")
            );
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    /**
     * Get an account by username and password (for login)
     * @param username the username
     * @param password the password
     * @return the Account if credentials match, null otherwise
     */
    public Account getAccountByUsernameAndPassword(String username, String password) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
