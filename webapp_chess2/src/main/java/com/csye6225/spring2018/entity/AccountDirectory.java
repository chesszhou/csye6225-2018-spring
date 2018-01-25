package com.csye6225.spring2018.entity;

import java.util.ArrayList;
import com.csye6225.spring2018.BCrypt;

public class AccountDirectory {
    private ArrayList<Account> accountDirectory;

    public AccountDirectory() {
        accountDirectory = new ArrayList<>();
    }

    public ArrayList<Account> getAccountDirectory() {
        return accountDirectory;
    }

    public void setAccountDirectory(ArrayList<Account> accountDirectory) {
        this.accountDirectory = accountDirectory;
    }

    public Account addAccount(String username, String password) {
        Account account = new Account();
        account.setName(username);
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        account.setPassword(hash);
        accountDirectory.add(account);
        return account;
    }

    public void deleteAirplane(Account account) {
        accountDirectory.remove(account);
    }
}
