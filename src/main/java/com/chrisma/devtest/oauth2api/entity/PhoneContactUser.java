package com.chrisma.devtest.oauth2api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "phone_contact_user")
@Getter
@Setter
public class PhoneContactUser {
    @EmbeddedId
    private PhoneContactUserId id = new PhoneContactUserId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Oauth2ApiUser user;

    @ManyToOne
    @MapsId("phoneContactId")
    @JoinColumn(name = "phone_contact_id", referencedColumnName = "id")
    private PhoneContact phoneContact;
}
