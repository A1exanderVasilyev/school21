package com.catinasw.T03.datasource;

import com.catinasw.T03.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    boolean existsUserByLogin(String login);
    Optional<User> getUserByLogin(String login);
}
