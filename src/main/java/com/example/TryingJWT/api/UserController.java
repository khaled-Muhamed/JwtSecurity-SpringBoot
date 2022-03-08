package com.example.TryingJWT.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.domain.Role;
import com.example.TryingJWT.repo.RoleUserForm;
import com.example.TryingJWT.service.AccountService;
import com.example.TryingJWT.util.CommonMethodsUtil;
import com.example.TryingJWT.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final AccountService accountService;

    @GetMapping("/users")
    public ResponseEntity<List<Account>> getUsers(){
        return ResponseEntity.ok().body(accountService.getAccounts());
    }

    @PostMapping("/user/save")
    ResponseEntity<Account> addUser(@RequestBody Account account){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(accountService.saveAccount(account));
    }


    @PostMapping("/role/save")
    ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(accountService.saveRole(role));
    }

    @GetMapping("/user/{username}")
    ResponseEntity<Account> getAccount(@PathVariable String username){
        return ResponseEntity.ok().body(accountService.getAccount(username));
    }

    @PostMapping("/role/addToUser")
    ResponseEntity<?> addRoleToUser (@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToAccount(roleUserForm.getUsername(), roleUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }
}
