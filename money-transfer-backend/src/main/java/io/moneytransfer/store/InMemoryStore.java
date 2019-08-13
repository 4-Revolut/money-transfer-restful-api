package io.moneytransfer.store;

import io.moneytransfer.model.User;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InMemoryStore {

    private Map<String, User> users = new ConcurrentHashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }
}
