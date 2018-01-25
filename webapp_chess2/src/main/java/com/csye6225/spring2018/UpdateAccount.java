package com.csye6225.spring2018;

import com.csye6225.spring2018.entity.*;

public class UpdateAccount {
    private Account account;
    private AccountDirectory accountDirectory;
    private String username;
    private String password;

    public UpdateAccount(AccountDirectory accountDirectory, String username, String password) {
        this.accountDirectory = accountDirectory;
        this.username = username;
        this.password = password;
        System.out.println(username);
        System.out.println(password);

    }

    public void addAccount() {
        accountDirectory.addAccount(username, password);
    }

    public boolean checkAccount() {
        for(Account a: accountDirectory.getAccountDirectory()) {
            if(username.equals(a.getName()) && BCrypt.checkpw(password, a.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
