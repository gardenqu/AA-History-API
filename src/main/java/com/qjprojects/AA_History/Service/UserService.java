package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.Role;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityLogService securityLogService;

    public UserService(AppUserRepository userRepository, RoleRepository roleRepository, SecurityLogService securityLogService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityLogService = securityLogService;
    }

    public AppUser getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<AppUser> getAll() {
        return userRepository.findAll();
    }

    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    public AppUser ban(String id) {
        AppUser user = getById(id);
        user.setBanned(true);
        user.setActive(false);
        securityLogService.log(user, "USER_BANNED");
        return userRepository.save(user);
    }

    public AppUser unban(String id) {
        AppUser user = getById(id);
        user.setBanned(false);
        user.setActive(true);
        securityLogService.log(user, "USER_UNBANNED");
        return userRepository.save(user);
    }

    public AppUser deactivate(String id) {
        AppUser user = getById(id);
        user.setActive(false);
        securityLogService.log(user, "USER_DEACTIVATED");
        return userRepository.save(user);
    }


    public AppUser assignRoles(String id, Set<String> roleNames) {
        AppUser user = getById(id);
        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        securityLogService.log(user, "ROLES_ASSIGNED", String.join(", ", roleNames));
        return userRepository.save(user);
    }
}