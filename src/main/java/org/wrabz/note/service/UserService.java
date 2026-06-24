package org.wrabz.note.security.service;

import org.wrabz.note.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void updateUserRole(Long userId, String roleName);

    User getUserById(Long id);

    User findByUsername(String username);
}
