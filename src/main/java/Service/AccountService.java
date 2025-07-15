package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Register a new account
     * @param account the account to register
     * @return the registered account if successful, null otherwise
     */
    public Account registerAccount(Account account) {
        if (account.getUsername().isBlank() || 
            account.getPassword().length() < 4 || 
            accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    /**
     * Login to an account
     * @param account the account credentials to login with
     * @return the logged in account if successful, null otherwise
     */
    public Account loginAccount(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

    /**
     * Get an account by its ID
     * @param account_id the ID of the account to retrieve
     * @return the account if found, null otherwise
     */
    public Account getAccountById(int account_id) {
        // This method might be useful for future extensions
        // Currently not used in the requirements but good to have
        return accountDAO.getAccountById(account_id);
    }
}