package com.chrisma.devtest.oauth2api.repository;

import com.chrisma.devtest.oauth2api.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Scope> {
    List<Scope> findByRole(String role);
}
