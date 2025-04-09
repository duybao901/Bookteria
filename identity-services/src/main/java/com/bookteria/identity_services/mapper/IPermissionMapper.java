package com.bookteria.identity_services.mapper;

import com.bookteria.identity_services.dto.request.PermissionRequest;
import com.bookteria.identity_services.dto.response.PermissionResponse;
import com.bookteria.identity_services.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
