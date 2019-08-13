package io.moneytransfer.store;

import io.moneytransfer.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore {

    private Map<String, User> users = new ConcurrentHashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }
}
