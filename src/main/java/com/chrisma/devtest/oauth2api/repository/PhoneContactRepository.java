package com.chrisma.devtest.oauth2api.repository;

import com.chrisma.devtest.oauth2api.entity.PhoneContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneContactRepository extends JpaRepository<PhoneContact, Long> {
}
