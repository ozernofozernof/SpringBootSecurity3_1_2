package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {


    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String homeAdmin() {
        return "redirect:/admin/users";
    }

    @GetMapping("users")
    public String printUsers(Model model) {
        model.addAttribute("userSet", userService.listUsers());
        return "allUsers";
    }

    @GetMapping(value = "users/add")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping(value = "users/add")
    public String createNewUser(
            @RequestParam String name,
            @RequestParam String lastName,
            @RequestParam int age,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam List<String> roles,
            RedirectAttributes redirectAttributes) {

        try {
            User user = new User(name, lastName, age, email, password);
            user.setRoles(roleService.getSetOfRoles(roles.toArray(new String[0])));
            userService.addUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Удаляем GET-метод для отдельной страницы редактирования
    // @GetMapping("users/{id}/edit") - больше не нужен

    @PatchMapping("users/{id}/edit")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam String name,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam String[] roles) {

        User user = userService.getUserById(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        user.setRoles(roleService.getSetOfRoles(roles));
        userService.updateUser(user);

        return "redirect:/admin/users";
    }

    @DeleteMapping("users/{id}/delete")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }
}