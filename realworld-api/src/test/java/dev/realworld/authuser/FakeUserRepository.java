package dev.realworld.authuser;

import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class FakeUserRepository implements UserRepository {
    final Map<String, User> users = new LinkedHashMap<>();

    @Override
    public <S extends User> S save(S entity) {
        users.put(entity.username(), entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(List<S> entities) {
        entities.forEach(this::save);
        return entities;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.email().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Stream<User> findAll() {
        return users.values().stream();
    }

    @Override
    public Page<User> findAll(PageRequest pageRequest, Order<User> order) {
        throw new UnsupportedOperationException("Paging is not needed by these tests");
    }

    @Override
    public void deleteById(String id) {
        users.remove(id);
    }

    @Override
    public void delete(User entity) {
        users.remove(entity.username());
    }

    @Override
    public void deleteAll(List<? extends User> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public <S extends User> S insert(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> insertAll(List<S> entities) {
        return saveAll(entities);
    }

    @Override
    public <S extends User> S update(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> updateAll(List<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public long countBy() {
        return users.size();
    }

    @Override
    public boolean existsById(String id) {
        return users.containsKey(id);
    }

    @Override
    public Stream<User> findByIdIn(Iterable<String> ids) {
        var found = new java.util.ArrayList<User>();
        ids.forEach(id -> findById(id).ifPresent(found::add));
        return found.stream();
    }

    @Override
    public void deleteByIdIn(Iterable<String> ids) {
        ids.forEach(this::deleteById);
    }
}
