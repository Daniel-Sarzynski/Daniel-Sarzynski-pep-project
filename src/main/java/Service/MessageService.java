package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * Add a new message
     * @param message the message to add
     * @return the added message if successful, null otherwise
     */
    public Message addMessage(Message message) {
        // Validate the message
        if (message.getMessage_text().isBlank() || 
            message.getMessage_text().length() > 255 || 
            accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    /**
     * Get all messages
     * @return a list of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Get a message by its ID
     * @param message_id the ID of the message to retrieve
     * @return the message if found, null otherwise
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Delete a message by its ID
     * @param message_id the ID of the message to delete
     * @return the deleted message if found and deleted, null otherwise
     */
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    /**
     * Update a message's text by its ID
     * @param message_id the ID of the message to update
     * @param newMessageText the new text for the message
     * @return the updated message if successful, null otherwise
     */
    public Message updateMessage(int message_id, String newMessageText) {
        // Validate the new message text
        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }

        return messageDAO.updateMessage(message_id, newMessageText);
    }

    /**
     * Get all messages posted by a particular user
     * @param account_id the ID of the user whose messages to retrieve
     * @return a list of messages posted by the user
     */
    public List<Message> getMessagesByUser(int account_id) {
        return messageDAO.getMessagesByUser(account_id);
    }
}