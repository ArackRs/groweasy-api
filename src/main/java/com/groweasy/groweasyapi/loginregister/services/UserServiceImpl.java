package com.groweasy.groweasyapi.loginregister.services;

import com.groweasy.groweasyapi.loginregister.model.dto.request.SignupRequest;
import com.groweasy.groweasyapi.loginregister.model.dto.response.UserResponse;
import com.groweasy.groweasyapi.loginregister.model.entities.RoleEntity;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.loginregister.model.enums.RoleEnum;
import com.groweasy.groweasyapi.loginregister.repository.RoleRepository;
import com.groweasy.groweasyapi.loginregister.repository.UserRepository;
import com.groweasy.groweasyapi.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository rolePersistence;

    @Override
    public void createUser(SignupRequest signupRequest, RoleEnum role) {
        RoleEntity roleUser = getRole(role);

        UserEntity user = UserEntity.builder()
                .username(signupRequest.username())
                .password(signupRequest.password())
                .roles(Set.of(roleUser))
                .build();

//        user.setUserDetails(UserDetailsEntity.builder()
//                .enabled(true)
//                .accountNoExpired(true)
//                .accountNoLocked(false)
//                .credentialNoExpired(true)
//                .build());

        UserEntity userSaved = userRepository.save(user);
    }

    private RoleEntity getRole(RoleEnum role) {
        return rolePersistence.findByRoleName(role)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<UserEntity> userEntities = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            userResponseList.add(UserResponse.fromEntity(userEntity));
        }
        return userResponseList;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.fromEntity(userEntity);
    }

    @Override
    public void updateUsername(Long userId, String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException("Username already exists");
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userEntity.setUsername(username);
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));
        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        log.info("userEntity username: {}", userEntity.getUsername());


        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                userEntity.getRoles().stream().map(role -> role.getRoleName().name()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
    }
}
