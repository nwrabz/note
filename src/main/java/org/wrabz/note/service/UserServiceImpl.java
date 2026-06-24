package org.wrabz.note.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wrabz.note.exception.ResourceNotFoundException;
import org.wrabz.note.model.AppRole;
import org.wrabz.note.model.Role;
import org.wrabz.note.model.User;
import org.wrabz.note.repository.RoleRepository;
import org.wrabz.note.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        AppRole appRole = AppRole.valueOf(roleName);

        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found."));
        user.setRole(role);

        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public void updatePassword(Long userId, String password) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }


    @Override
    public void updateAccountLockStatus(Long userId, boolean lock) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setCredentialNonexpired(!expire);
        userRepository.save(user);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
