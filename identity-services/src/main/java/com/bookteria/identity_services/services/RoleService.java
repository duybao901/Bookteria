package com.bookteria.identity_services.services;

import com.bookteria.identity_services.dto.request.RoleRequest;
import com.bookteria.identity_services.dto.response.RoleResponse;
import com.bookteria.identity_services.entities.Permission;
import com.bookteria.identity_services.entities.Role;
import com.bookteria.identity_services.exceptions.RoleException;
import com.bookteria.identity_services.mapper.IRoleMapper;
import com.bookteria.identity_services.repositories.IPermissionRepository;
import com.bookteria.identity_services.repositories.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final IRoleRepository _roleRepository;
    private final IRoleMapper _roleMapper;
    private final IPermissionRepository _permissionRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository, IRoleMapper roleMapper, IPermissionRepository permissionRepository) {
        this._roleRepository = roleRepository;
        this._roleMapper = roleMapper;
        this._permissionRepository = permissionRepository;
    }

    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = _roleMapper.toRole(roleRequest);

        Set<String> rolePermissions = roleRequest.getPermissions();
        List<Permission> permissions = _permissionRepository.findAllById(rolePermissions);
        role.setPermissions(new HashSet<>(permissions));

        role = _roleRepository.save(role);
        return _roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        var roles = _roleRepository.findAll();
        return roles.stream()
                .map(_roleMapper::toRoleResponse)
                .toList();
    }

   @Transactional
    public RoleResponse deleteRole(String roleName) {
        Role role = _roleRepository.findById(roleName).orElse(null);
        if (role == null) {
            throw new RoleException.RoleNotFoundException(roleName);
        }

        _roleRepository.delete(role);

        return _roleMapper.toRoleResponse(role);
    }
}
