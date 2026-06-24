package org.wrabz.note.security.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wrabz.note.exception.ResourceNotFoundException;
import org.wrabz.note.model.AppRole;
import org.wrabz.note.model.Role;
import org.wrabz.note.model.User;
import org.wrabz.note.repository.RoleRepository;
import org.wrabz.note.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
}
