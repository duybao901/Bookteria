package com.bookteria.identity_services.services;

import com.bookteria.identity_services.dto.request.PermissionRequest;
import com.bookteria.identity_services.dto.response.PermissionResponse;
import com.bookteria.identity_services.entities.Permission;
import com.bookteria.identity_services.exceptions.PermissionException;
import com.bookteria.identity_services.mapper.IPermissionMapper;
import com.bookteria.identity_services.repositories.IPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final IPermissionRepository _permissionRepository;
    private final IPermissionMapper _permissionMapper;

    @Autowired
    public PermissionService(IPermissionRepository _permissionRepository, IPermissionMapper _permissionMapper) {
        this._permissionRepository = _permissionRepository;
        this._permissionMapper = _permissionMapper;
    }

    public PermissionResponse create(PermissionRequest permissionRequest) {
        Permission permission = _permissionMapper.toPermission(permissionRequest);
        permission = _permissionRepository.save(permission);
        return _permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = _permissionRepository.findAll();
        return permissions.stream().map(_permissionMapper::toPermissionResponse).toList();
    }

    public PermissionResponse deletePermission(String permissionName) {
        Permission permission = _permissionRepository.findById(permissionName).orElse(null);
        if (permission == null) {
            throw new PermissionException.PermissionNotFoundException(permissionName);
        }

        _permissionRepository.delete(permission);

        return _permissionMapper.toPermissionResponse(permission);
    }
}
