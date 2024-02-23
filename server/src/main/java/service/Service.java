package service;

import java.util.UUID;

public class Service {
    protected static String generateAuth() {
        return UUID.randomUUID().toString();
    }
}
