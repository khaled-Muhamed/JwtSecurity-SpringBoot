package com.example.TryingJWT.service.impl;

import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.domain.Role;
import com.example.TryingJWT.repo.RoleRepo;
import com.example.TryingJWT.repo.UserRepo;
import com.example.TryingJWT.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepo.findByUsername(username);

        if(account == null){
            log.error("User {} not found in database", username);
            throw new UsernameNotFoundException("User not found in database");
        }else{
            log.info("User {} found in database", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), authorities);
    }

    @Override
    public Account saveAccount(Account account) {
        log.info("saving new user {} to database", account.getName());
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        return userRepo.save(account);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving new role {} to database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToAccount(String username, String roleName) {
        // you may need some validations here before just saving in DB
        log.info("Adding role {} to the user {}", roleName, username);
        Account account = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        //because of @Transactional no need to insert in DB again it's
        //automatically done
        account.getRoles().add(role);
    }

    @Override
    public Account getAccount(String username) {
        log.info("Fetching user by username = {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<Account> getAccounts() {
        // try to use pagination here instead of loading all users
        log.info("Fetching all users");
        return userRepo.findAll();
    }

}
