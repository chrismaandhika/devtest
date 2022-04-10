package com.chrisma.devtest.oauth2api.service;

import com.chrisma.devtest.oauth2api.dto.CreatePhoneContact;
import com.chrisma.devtest.oauth2api.dto.DeletePhoneContact;
import com.chrisma.devtest.oauth2api.dto.UpdatePhoneContact;
import com.chrisma.devtest.oauth2api.entity.Oauth2ApiUser;
import com.chrisma.devtest.oauth2api.entity.PhoneContact;
import com.chrisma.devtest.oauth2api.entity.PhoneContactUser;
import com.chrisma.devtest.oauth2api.entity.PhoneContactUserId;
import com.chrisma.devtest.oauth2api.repository.PhoneContactRepository;
import com.chrisma.devtest.oauth2api.repository.PhoneContactUserRepository;
import com.chrisma.devtest.oauth2api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class PhoneContactUserService {
    private UserRepository userRepo;
    private PhoneContactRepository phoneContactRepo;
    private PhoneContactUserRepository phoneContactUserRepo;

    public PhoneContactUserService(UserRepository userRepo,
                                   PhoneContactRepository phoneContactRepo,
                                   PhoneContactUserRepository phoneContactUserRepo) {
        this.userRepo = userRepo;
        this.phoneContactRepo = phoneContactRepo;
        this.phoneContactUserRepo = phoneContactUserRepo;
    }

    @Transactional
    public Long createOne(CreatePhoneContact request, String username) {
        Oauth2ApiUser user = userRepo.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
        throwErrorOnDuplicateContact(username, request.getName(), request.getPhone());
        throwErrorOnMissingMandatoryFields(request.getName(), request.getPhone());

        PhoneContact phoneContact = phoneContactRepo.save(request.phoneContact());
        PhoneContactUser phoneContactUser = new PhoneContactUser();
        phoneContactUser.setPhoneContact(phoneContact);
        phoneContactUser.setUser(user);
        phoneContactUserRepo.save(phoneContactUser);
        return phoneContact.getId();
    }

    public List<PhoneContact> findAll(String username) {
        return phoneContactUserRepo.findByUserUsername(username);
    }

    @Transactional
    public void updateOne(UpdatePhoneContact request, String username) {
        PhoneContact phoneContact = findPhoneContactWithErrorIfNotFound(
                username,
                request.getCurrentName(),
                request.getCurrentPhone()
        );
        throwErrorOnDuplicateContact(username, request.getNewName(), request.getNewPhone());
        throwErrorOnMissingMandatoryFields(request.getCurrentName(), request.getCurrentPhone(), request.getNewName(), request.getNewPhone());

        phoneContact.setName(request.getNewName());
        phoneContact.setPhone(request.getNewPhone());
    }

    @Transactional
    public void deleteOne(DeletePhoneContact request, String username) {
        PhoneContact phoneContact = findPhoneContactWithErrorIfNotFound(
                username,
                request.getName(),
                request.getPhone()
        );

        phoneContactUserRepo.deleteById(new PhoneContactUserId(username, phoneContact.getId()));
        phoneContactRepo.delete(phoneContact);
    }

    private void throwErrorOnMissingMandatoryFields(String ...fields) {
        Arrays.stream(fields).forEach(s -> {
            if(!StringUtils.hasText(s)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing mandatory field");
            }
        });
    }

    private void throwErrorOnDuplicateContact(String username, String name, String phone) {
        if(phoneContactUserRepo.countByUsernameAndPhoneContact(username, name, phone) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"duplicate contact");
        }
    }

    private PhoneContact findPhoneContactWithErrorIfNotFound(String username, String name, String phone) {
        return phoneContactUserRepo.findByUsernameAndPhoneContact(username,name,phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"contact not found"));
    }
}
