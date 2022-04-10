package com.chrisma.devtest.oauth2api.resource;

import com.chrisma.devtest.oauth2api.dto.CreatePhoneContact;
import com.chrisma.devtest.oauth2api.dto.DeletePhoneContact;
import com.chrisma.devtest.oauth2api.dto.UpdatePhoneContact;
import com.chrisma.devtest.oauth2api.entity.PhoneContact;
import com.chrisma.devtest.oauth2api.service.PhoneContactUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@SecurityRequirement(name = "oauth2Password")
@Tag(name = "Phone Contact", description = "CRUD service of phone contact resource")
public class PhoneContactResource {
    private PhoneContactUserService phoneContactUserService;

    public PhoneContactResource(PhoneContactUserService phoneContactUserService) {
        this.phoneContactUserService = phoneContactUserService;
    }

    @Operation(summary = "Get all phone contacts of a user",
            description = "Get all phone contacts of a user. User is inferred from the token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PhoneContact.class)))),
            @ApiResponse(responseCode = "401", description = "Token is not set or is invalid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient token scope", content = @Content)
    })
    @GetMapping(path = "/phone_contacts")
    public ResponseEntity<List<PhoneContact>> getContacts(Principal principal)
    {
        return ResponseEntity.ok(phoneContactUserService.findAll(principal.getName()));
    }

    @Operation(summary = "Create a phone contact that will be owned by the user",
            description = "Create a phone contact that will be owned by the user. User is inferred from the token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not recognized", content = @Content),
            @ApiResponse(responseCode = "400", description = "One of mandatory fields is empty", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate contact", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token is not set or is invalid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient token scope", content = @Content)
    })
    @PostMapping(path = "/phone_contacts")
    public ResponseEntity createContact(@RequestBody CreatePhoneContact request, Principal principal) {
        phoneContactUserService.createOne(request, principal.getName());
        return ResponseEntity.created(ResourceHelper.buildResourceLocationURI("/phone_contacts")).build();
    }

    @Operation(summary = "Update phone contact that is owned by the user",
            description = "Update phone contact that is owned by the user. User is inferred from the token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "Original contract not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "One of mandatory fields is empty", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate contact", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token is not set or is invalid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient token scope", content = @Content)
    })
    @PutMapping(path = "/phone_contacts")
    public ResponseEntity updateContact(@RequestBody UpdatePhoneContact request, Principal principal) {
        phoneContactUserService.updateOne(request, principal.getName());
        return ResponseEntity.created(ResourceHelper.buildResourceLocationURI("/phone_contacts")).build();
    }

    @Operation(summary = "Delete phone contact that is owned by the user",
            description = "Delete phone contact that is owned by the user. User is inferred from the token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "Original contract not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token is not set or is invalid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient token scope", content = @Content)
    })
    @DeleteMapping(path = "/phone_contacts")
    public ResponseEntity deleteContact(@RequestBody DeletePhoneContact request, Principal principal) {
        phoneContactUserService.deleteOne(request, principal.getName());
        return ResponseEntity.noContent().build();
    }
}