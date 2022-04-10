package com.chrisma.devtest.oauth2api.repository;

import com.chrisma.devtest.oauth2api.entity.PhoneContact;
import com.chrisma.devtest.oauth2api.entity.PhoneContactUser;
import com.chrisma.devtest.oauth2api.entity.PhoneContactUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhoneContactUserRepository extends JpaRepository<PhoneContactUser, PhoneContactUserId> {
    @Query("SELECT pcu.phoneContact FROM PhoneContactUser pcu WHERE pcu.user.username = :username")
    List<PhoneContact> findByUserUsername(@Param("username") String username);

    @Query("SELECT pcu.phoneContact FROM PhoneContactUser pcu " +
            "WHERE pcu.user.username = :username " +
            "AND pcu.phoneContact.name = :contactName " +
            "AND pcu.phoneContact.phone = :contactPhone")
    Optional<PhoneContact> findByUsernameAndPhoneContact(
            @Param("username") String username,
            @Param("contactName") String contactName,
            @Param("contactPhone") String contactPhone
    );

    @Query("SELECT COUNT(pcu) FROM PhoneContactUser pcu " +
            "WHERE pcu.user.username = :username " +
            "AND pcu.phoneContact.name = :contactName " +
            "AND pcu.phoneContact.phone = :contactPhone")
    Long countByUsernameAndPhoneContact(
            @Param("username") String username,
            @Param("contactName") String contactName,
            @Param("contactPhone") String contactPhone
    );
}
