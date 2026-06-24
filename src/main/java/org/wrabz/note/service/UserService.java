package org.wrabz.note.service;

import org.wrabz.note.model.Role;
import org.wrabz.note.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void updateUserRole(Long userId, String roleName);

    User getUserById(Long id);

    User findByUsername(String username);

    void updatePassword(Long userId, String password);

    void updateAccountLockStatus(Long userId, boolean lock);

    void updateAccountExpiryStatus(Long userId, boolean expire);

    void updateAccountEnabledStatus(Long userId, boolean enabled);

    void updateCredentialsExpiryStatus(Long userId, boolean expire);

    List<Role> getAllRoles();
}
