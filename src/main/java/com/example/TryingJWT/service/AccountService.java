package com.example.TryingJWT.service;

import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.domain.Role;

import java.util.List;

public interface AccountService {

    Account saveAccount(Account account);
    Role saveRole(Role role);
    void addRoleToAccount(String username, String roleName);
    Account getAccount(String username);
    List<Account> getAccounts();

}
