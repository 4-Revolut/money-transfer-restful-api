package io.moneytransfer.store;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class InMemoryStoreProvider implements ContextResolver<InMemoryStore> {

    private InMemoryStore users = new InMemoryStore();

    @Override
    public InMemoryStore getContext(Class klass) {
        return users;
    }
}
