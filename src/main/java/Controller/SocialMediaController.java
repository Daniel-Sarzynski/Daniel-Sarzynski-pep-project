package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.*;
import Model.*;
import Service.*;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @return a Javalin app object which defines the behavior of the Javalin controller.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }
 
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // Account 
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        
        // Message 
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        
        return app;
    }


    /**
     * Handler for user registration
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        Account account = objectMapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        
        if (registeredAccount != null) {
            context.json(registeredAccount);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for user login
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        Account account = objectMapper.readValue(context.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);
        
        if (loggedInAccount != null) {
            context.json(loggedInAccount);
        } else {
            context.status(401);
        }
    }

    /**
     * Handler for creating a new message
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.addMessage(message);
        
        if (createdMessage != null) {
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for getting all messages
     */
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * Handler for getting a message by ID
     */
    private void getMessageByIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        
        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
    }

    /**
     * Handler for deleting a message
     */
    private void deleteMessageHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        
        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.json("");
        }
    }

    /**
     * Handler for updating a message
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messageUpdate = objectMapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(message_id, messageUpdate.getMessage_text());
        
        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for getting all messages by a user
     */
    private void getMessagesByUserHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getMessagesByUser(account_id));
    }


}