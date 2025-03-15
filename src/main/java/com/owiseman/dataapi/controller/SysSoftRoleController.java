package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.BindRoleToUserDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.dto.SysSoftRoleDto;
import com.owiseman.dataapi.entity.SysSoftRole;
import com.owiseman.dataapi.entity.SysUserToSoftRole;
import com.owiseman.dataapi.service.SysSoftRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/soft-roles")
@PreAuthorize("hasRole('ADMIN')")
@RestController
public class SysSoftRoleController {
    @Autowired
    private SysSoftRoleService sysSoftRoleService;

    @PostMapping("/create")
    public SysSoftRole createRole(@RequestBody SysSoftRoleDto sysSoftRoleDto) {
        return sysSoftRoleService.createRole(sysSoftRoleDto);
    }

    @PostMapping("/list")
    public PageResult<SysSoftRole> findAllRoles(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestBody SysSoftRoleDto sysSoftRoleDto) {
        return sysSoftRoleService.findAllRoles(sysSoftRoleDto.getUserId(), page, size);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable String id) {
        sysSoftRoleService.deleteRole(id);
    }

    @PutMapping("/{id}/role-info")
    public SysSoftRole updateRole(@PathVariable String id, @RequestBody SysSoftRole role) {
        role.setId(id);
        return sysSoftRoleService.updateRole(role);
    }

    @PostMapping("/binding")
    public void bindingRoleToUser(@RequestBody SysUserToSoftRole sysUserToSoftRole) {
        sysSoftRoleService.bindingRoleToUser(sysUserToSoftRole.getUserId(), sysUserToSoftRole.getRoleCode());
    }

    @GetMapping("/binding/list")
    public PageResult<BindRoleToUserDto> findAllBinding(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return sysSoftRoleService.findAllBinding(page, size);
    }

    @DeleteMapping("/binding/{id}")
    public void unbindingRoleToUser( @PathVariable String id) {
        sysSoftRoleService.unBindingRoleToUser(id);
    }

}
