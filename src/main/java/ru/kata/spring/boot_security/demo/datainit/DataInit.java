package ru.kata.spring.boot_security.demo.datainit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInit {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        roleService.saveRole(new Role("USER"));
        roleService.saveRole(new Role("ADMIN"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("USER"));
        userService.saveUser(new User("user", "100", "user@mail.ru", roles));
        roles.add(roleService.getRoleByName("ADMIN"));
        userService.saveUser(new User("adin", "100", "admin@mail.ru", roles));
    }



}
