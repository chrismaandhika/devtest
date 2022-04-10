package com.chrisma.devtest.oauth2api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class PhoneContactUserId implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "phone_contact_id")
    private long phoneContactId;

    public PhoneContactUserId() {

    }

    public PhoneContactUserId(String userId, long phoneContactId) {
        this.userId = userId;
        this.phoneContactId = phoneContactId;
    }
}
