/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Roles;
import org.springframework.stereotype.Service;
import repository.RolesRepository;
import services.RolesSvc;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class RolesSvcImpl implements RolesSvc {

    private final RolesRepository repository;

    public RolesSvcImpl(RolesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Roles> roles() {
        return this.repository.finRoles();
    }
}
