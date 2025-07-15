package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /**
     * Insert a new message into the database
     * @param message the message to insert
     * @return the inserted message with its generated message_id, or null if insertion failed
     */
    public Message insertMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int generated_message_id = rs.getInt(1);
                return new Message(
                    generated_message_id, 
                    message.getPosted_by(), 
                    message.getMessage_text(), 
                    message.getTime_posted_epoch()
                );
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all messages from the database
     * @return a list of all messages
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Get a message by its ID
     * @param message_id the ID of the message to retrieve
     * @return the Message if found, null otherwise
     */
    public Message getMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, message_id);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete a message by its ID
     * @param message_id the ID of the message to delete
     * @return the deleted Message if found and deleted, null otherwise
     */
    public Message deleteMessage(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            // First get the message to return it after deletion
            Message message = getMessageById(message_id);
            if(message != null) {
                String sql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                
                ps.setInt(1, message_id);
                
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected > 0) {
                    return message;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update a message's text by its ID
     * @param message_id the ID of the message to update
     * @param newMessageText the new text for the message
     * @return the updated Message if successful, null otherwise
     */
    public Message updateMessage(int message_id, String newMessageText) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, newMessageText);
            ps.setInt(2, message_id);
            
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                return getMessageById(message_id);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all messages posted by a particular user
     * @param account_id the ID of the user whose messages to retrieve
     * @return a list of messages posted by the user
     */
    public List<Message> getMessagesByUser(int account_id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, account_id);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
