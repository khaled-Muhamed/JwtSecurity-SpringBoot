package com.example.TryingJWT.repo;

import com.example.TryingJWT.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
