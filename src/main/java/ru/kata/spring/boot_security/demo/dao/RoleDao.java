package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    public Set<Role> getAllRoles();

    public Role getRoleByName(String name);

    public Set<Role> getSetOfRoles(String[] roleNames);

    public void add(Role role);

    public void edit(Role role);

    public Role getById(Long id);
}