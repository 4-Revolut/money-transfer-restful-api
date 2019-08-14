package io.moneytransfer.store;

import io.moneytransfer.model.User;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InMemoryStore {

    private Map<String, User> allData = new ConcurrentHashMap<>();

    public Map<String, User> getAllData() {
        return allData;
    }

    public Collection<User> getUsers() {
        return allData.values();
    }
}
