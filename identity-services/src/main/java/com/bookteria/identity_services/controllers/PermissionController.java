package com.bookteria.identity_services.controllers;

import com.bookteria.identity_services.dto.request.PermissionRequest;
import com.bookteria.identity_services.dto.response.PermissionResponse;
import com.bookteria.identity_services.services.PermissionService;
import com.bookteria.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService _permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        _permissionService = permissionService;
    }

    @PostMapping
    public Result<PermissionResponse> CreatePermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = _permissionService.create(permissionRequest);
        return Result.success(permissionResponse);
    }

    @GetMapping
    public Result<List<PermissionResponse>> getUsers() {
        return Result.success(_permissionService.getAllPermissions());
    }

    @DeleteMapping("/{permissionName}")
    public Result<PermissionResponse> getUser(@PathVariable String permissionName) {
        return Result.success(_permissionService.deletePermission(permissionName));
    }
}
