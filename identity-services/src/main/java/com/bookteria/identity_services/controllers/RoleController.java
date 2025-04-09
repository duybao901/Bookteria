package com.bookteria.identity_services.controllers;

import com.bookteria.identity_services.dto.request.RoleRequest;
import com.bookteria.identity_services.dto.response.RoleResponse;
import com.bookteria.identity_services.services.RoleService;
import com.bookteria.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService _roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        _roleService = roleService;
    }

    @PostMapping
    public com.bookteria.shared.Result<RoleResponse> CreateRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse role = _roleService.createRole(roleRequest);
        return Result.success(role);
    }

    @GetMapping
    public Result<List<RoleResponse>> getRoles() {
        return Result.success(_roleService.getAllRoles());
    }

    @DeleteMapping("/{role}")
    public Result<RoleResponse> deleteRole(@PathVariable String role) {
        return Result.success(_roleService.deleteRole(role));
    }
}
