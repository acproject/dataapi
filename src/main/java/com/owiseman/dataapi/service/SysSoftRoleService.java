package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.BindRoleToUserDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.dto.SysSoftRoleDto;
import com.owiseman.dataapi.entity.SysSoftRole;
import com.owiseman.dataapi.entity.SysUserToSoftRole;
import com.owiseman.dataapi.repository.SysSoftRoleRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import com.owiseman.dataapi.repository.SysUserToSoftRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysSoftRoleService {
    @Autowired
    private SysSoftRoleRepository sysSoftRoleRepository;
    @Autowired
    private SysUserToSoftRoleRepository sysUserToSoftRoleRepository;
    @Autowired
    private SysUserRepository sysUserRepository;

    @Transactional
    public SysSoftRole createRole(SysSoftRoleDto role) {
        String realmName = sysUserRepository.findById(role.getUserId()).get().getRealmName();
        role.getSysSoftRole().setRealm(realmName);
        return sysSoftRoleRepository.save(role.getSysSoftRole());
    }

    @Transactional
    public SysSoftRole updateRole(SysSoftRole role) {
        return sysSoftRoleRepository.update(role);
    }

    @Transactional
    public void deleteRole(String id) {
        var roleCode = sysSoftRoleRepository.findById(id).get().getRoleCode();
        // 先删除关联用户
        sysUserToSoftRoleRepository.deleteRoleUsers(roleCode);
        // 再删除角色
        sysSoftRoleRepository.deleteById(id);
    }

    public PageResult<SysSoftRole> findAllRoles(String userId, int pageNumber, int pageSize) {
        var realmName = sysUserRepository.findById(userId).get().getRealmName();
        return sysSoftRoleRepository.findAllWithPagination(realmName, pageNumber, pageSize);
    }

    @Transactional
    public void bindingRoleToUser(String userId, String roleCode) {
        sysUserToSoftRoleRepository.addUserRoleAssociation(userId, roleCode);
    }



    public PageResult<BindRoleToUserDto> findAllBinding(int page, int size) {
       return sysUserToSoftRoleRepository.findAll(page, size);
    }

    public void unBindingRoleToUser(String id) {
        sysUserToSoftRoleRepository.deleteById(id);
    }
}
