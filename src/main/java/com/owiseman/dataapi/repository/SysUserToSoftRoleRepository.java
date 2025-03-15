package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.BindRoleToUserDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysSoftRole;
import com.owiseman.dataapi.entity.SysUserToSoftRole;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.owiseman.dataapi.entity.Tables.SYSUSERTOSOFTROLE.*;
import static com.owiseman.dataapi.entity.Tables.SYSSOFTROLE;

@Repository
public class SysUserToSoftRoleRepository {
     private final DSLContext dslContext;

    @Autowired
    public SysUserToSoftRoleRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    @Autowired
    public SysUserRepository sysUserRepository;

    @Autowired
    public SysSoftRoleRepository sysSoftRoleRepository;

     // 添加用户与角色的关联
    @Transactional
    public void addUserRoleAssociation(String userId, String roseCode) {
        String id = UUID.randomUUID().toString();
        dslContext.insertInto(TABLE)
                .set(ID, id)
                .set(USERID, userId)
                .set(ROLECODE, roseCode)
                .execute();
    }

    // 删除用户与角色的关联
    @Transactional
    public void deleteUserRoleAssociation(String userId, String roseCode) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.eq(userId).and(ROLECODE.eq(roseCode)))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    // 删除某个用户的所有角色关联
    @Transactional
    public void deleteUserRoles(String userId) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.eq(userId))
                .execute();
    }

    // 删除某个角色的所有用户关联
    @Transactional
    public void deleteRoleUsers(String roseCode) {
        dslContext.deleteFrom(TABLE)
                .where(ROLECODE.eq(roseCode))
                .execute();
    }

    // 查询某个用户的所有角色ID
    public List<String> findRoleIdsByUserId(String userId) {
        return dslContext.select(ROLECODE)
                .from(TABLE)
                .where(USERID.eq(userId))
                .fetch(ROLECODE);
    }

    // 查询某个角色的所有用户ID
    public List<String> findUserIdsByRoleId(String roseCode) {
        return dslContext.select(USERID)
                .from(TABLE)
                .where(ROLECODE.eq(roseCode))
                .fetch(USERID);
    }

    public PageResult<BindRoleToUserDto> findAll(int pageNum, int pageSize) {
        Condition condition = DSL.noCondition();
        List<SysUserToSoftRole> info = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNum,
                SysUserToSoftRole.class
        );
        List<BindRoleToUserDto> bindsDtoList = new ArrayList<>();
        for (SysUserToSoftRole sysUserToSoftRole : info) {
            BindRoleToUserDto bindsDto = new BindRoleToUserDto();
            bindsDto.setId(sysUserToSoftRole.getId());
            bindsDto.setUserId(sysUserToSoftRole.getUserId());
            bindsDto.setRoleCode(sysUserToSoftRole.getRoleCode());
            bindsDto.setUserName(sysUserRepository.findById(bindsDto.getUserId()).get().getUsername());
            bindsDto.setRoleName(sysSoftRoleRepository.findByRoleCode(sysUserToSoftRole.getRoleCode()).get().getRoleName());
            bindsDtoList.add(bindsDto);
        }

        int total = dslContext.selectCount()
                .from(TABLE)
                .fetchOne(0, Integer.class);
        return new PageResult<>(bindsDtoList, pageNum, pageSize, total);
    }

    // 查询某个用户的所有角色信息
    public List<SysSoftRole> findRolesByUserId(String userId) {
    return dslContext.select()
            .from(SYSSOFTROLE.TABLE)
            .join(TABLE)
            .on(SYSSOFTROLE.ROLECODE.eq(ROLECODE)) // 中间表的 ROLECODE 和角色表的 ROLECODE 关联
            .where(USERID.eq(userId)) // 筛选中间表中与指定用户关联的记录
            .fetchInto(SysSoftRole.class);
}


    // 批量添加用户与角色的关联
    @Transactional
    public void batchAddUserRoleAssociations(List<String> userIds, String roseCode) {
        userIds.forEach(userId -> dslContext.insertInto(TABLE)
                .set(USERID, userId)
                .set(ROLECODE, roseCode)
                .execute());
    }

    // 批量删除用户与角色的关联
    @Transactional
    public void batchDeleteUserRoleAssociations(List<String> userIds, String roseCode) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.in(userIds).and(ROLECODE.eq(roseCode)))
                .execute();
    }
}
