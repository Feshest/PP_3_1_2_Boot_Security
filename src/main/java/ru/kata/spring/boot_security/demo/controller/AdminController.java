package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id){
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "/editUser";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "nameRole") String nameRole) {

        Role role = new Role(nameRole);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute(new User());
        return "/newUser";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(required = false) String userRoles) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (userRoles != null && userRoles.equals(
                roleService.getRoleByName("ROLE_ADMIN").getName())) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        user.setRoles(roles);
        userService.saveUser(user);

        return "redirect:/admin";
    }

}
