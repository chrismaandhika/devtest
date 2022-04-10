package com.chrisma.devtest.oauth2api.resource;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class ResourceHelper {
    public static URI buildResourceLocationURI(String path) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .build()
                .toUri();
    }
}
