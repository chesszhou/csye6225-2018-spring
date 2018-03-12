package com.csye6225.spring2018.Repo;

import com.csye6225.spring2018.entity.User;
import org.springframework.data.repository.CrudRepository;

// This is the repository interface, this will be automatically implemented by Spring in a bean with the same name with changing case.
// The bean name will be userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
// By extending CrudRepository, UserRepository inherits several methods for working with User persistence,
// including methods for saving, deleting, and finding User entities.

    User findByUsername(String username);
}
