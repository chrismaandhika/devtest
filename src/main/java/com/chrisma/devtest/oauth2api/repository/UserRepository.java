package com.chrisma.devtest.oauth2api.repository;

import com.chrisma.devtest.oauth2api.entity.Oauth2ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Oauth2ApiUser, String> {
}
