package com.bookteria.identity_services.mapper;

import com.bookteria.identity_services.dto.request.RoleRequest;
import com.bookteria.identity_services.dto.response.RoleResponse;
import com.bookteria.identity_services.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
