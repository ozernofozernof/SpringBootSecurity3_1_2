package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
    public String newUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping(value = "users/add")
    public String createNewUser(@ModelAttribute("user") User user
            , @RequestParam(value = "roles") String[] roles) {
        user.setRoles(roleService.getSetOfRoles(roles));
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("users/{id}/edit")
    public String editUserForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getUserById(id));
        return "editUser";
    }

    @PatchMapping("users/{id}/edit")
    public String update(@ModelAttribute("user") User user
            , @RequestParam(value = "roles") String[] roles) {
        user.setRoles(roleService.getSetOfRoles(roles));
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("users/{id}/delete")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("users/{id}")
    public String show(@PathVariable("id") Long id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getUserById(id));
        return "userInfoById";
    }
}
