package org.hospitalmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public interface Firebase {
    FirebaseApp getApp();
    FirebaseAuth getAuth();
}

@Configuration
class FirebaseConfig implements Firebase {
    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:firebase-sdk.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build();
        FirebaseApp.initializeApp(options);
    }

    @Override
    public FirebaseApp getApp() {
        return FirebaseApp.getInstance();
    }

    @Override
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance(getApp());
    }
}
