package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.JsonMapConverter;
import com.owiseman.jpa.util.PaginationHelper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSUSER.*;

import java.util.List;

@Repository
public class SysUserRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysUserRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    @Autowired
    SysUserConfigRepository sysUserConfigRepository;

    public SysUser save(SysUser user) {
        String jsonString = JsonMapConverter.MapToJsonString(user.getAttributes());
        dslContext.insertInto(TABLE)
                .set(ID, user.getId())
                .set(USERNAME, user.getUsername())
                .set(FIRSTNAME, user.getFirstName())
                .set(LASTNAME, user.getLastName())
                .set(EMAIL, user.getEmail())
                .set(EMAILVERIFIED, user.getEmailVerified())
                .set(ATTRIBUTES, DSL.field("?::jsonb", Object.class, jsonString))
                .set(CREATEDTIMESTAMP, user.getCreatedTimestamp())
                .set(ENABLED, user.getEnabled())
                .set(REALMNAME, user.getRealmName())
                .set(CLIENTID, user.getClientId())
                .set(PROJECTID, user.getProjectId())
                .execute();
        return user;
    }

    public void update(SysUser user) {
        String jsonString = JsonMapConverter.MapToJsonString(user.getAttributes());

        dslContext.update(TABLE)
                .set(FIRSTNAME, user.getFirstName())
                .set(LASTNAME, user.getLastName())
                .set(EMAIL, user.getEmail())
                .set(EMAILVERIFIED, user.getEmailVerified())
                .set(ATTRIBUTES, DSL.field("?::jsonb", Object.class, jsonString))
                .set(ENABLED, user.getEnabled())
                .where(ID.eq(user.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public void deleteByRealmName(String realmName) {
        dslContext.deleteFrom(TABLE)
                .where(REALMNAME.eq(realmName))
                .execute();
    }

    /**
     * 用于查找组织用户
     *
     * @param email
     * @return
     */
    public Optional<SysUser> findByEmail(String email) {
        return dslContext.selectFrom(TABLE)
                .where(EMAIL.eq(email).and(PROJECTID.isNull()))
                .fetchOptionalInto(SysUser.class);
    }

    /**
     * 用于查找组织用户
     *
     * @param username
     * @return
     */
    public Optional<SysUser> findByUsername(String username) {
        return dslContext.selectFrom(TABLE)
                .where(USERNAME.eq(username).and(PROJECTID.isNull()))
                .fetchOptionalInto(SysUser.class);
    }

    public Optional<SysUser> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysUser.class);
    }

    /**
     * 用于判断组织用户是否为存在
     *
     * @param username
     * @param realmName
     * @return
     */
    public Optional<SysUser> findByUsernameAndRealmName(String username, String realmName) {
        return dslContext.selectFrom(TABLE)
                .where(USERNAME.eq(username).and(REALMNAME.eq(realmName)))
                .fetchOptionalInto(SysUser.class);
    }


    /**
     * 用于判断项目用户是否为存在
     *
     * @param username
     * @param projectId
     * @return
     */
    public Optional<SysUser> findByUsernameAndProjectId(String username, String projectId) {
        return dslContext.selectFrom(TABLE).where(USERNAME.eq(username).and(PROJECTID.eq(projectId)))
                .fetchOptionalInto(SysUser.class);
    }

    public Optional<SysUser> findByProjectIdAndUsername(String username, String projectId) {
        return dslContext.selectFrom(TABLE).where(USERNAME.eq(username).and(PROJECTID.eq(projectId)))
                .fetchOptionalInto(SysUser.class);
    }

    public List<SysUser> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysUser.class);
    }

    public List<SysUser> findByRealmName(String realmName) {
        return dslContext.selectFrom(TABLE)
                .where(REALMNAME.eq(realmName))
                .fetchInto(SysUser.class);
    }

    public List<SysUser> findByRealmNameAndProjectId(String realmName, String projectId) {
        return dslContext.selectFrom(TABLE)
                .where(REALMNAME.eq(realmName).and(PROJECTID.eq(projectId)))
                .fetchInto(SysUser.class);
    }

    // 在 SysUserRepository 类中新增分页方法
    public PageResult<SysUser> findAllWithPagination(int pageNumber, int pageSize) {
        // 获取分页数据
        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(), // 确认 TABLE.getName() 返回正确的表名（如 "sys_user"）
                pageSize,
                pageNumber,
                SysUser.class
        );

        // 获取总记录数（直接查询代替 getTotalPages）
        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    // 带条件的分页查询（扩展方法）
    public PageResult<SysUser> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUser.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    /**
     * 查找和realm和project相关的用户
     * @param apikey
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<SysUser> findByRealmNameWithPagination(String apikey, int pageNumber, int pageSize) {

        SysUserConfig config = sysUserConfigRepository.findByProjectApiKey(apikey).get();
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }
        // 防止输入负数
        if (pageNumber < 0) {
            pageNumber = 0;
        }

        if (pageSize < 0) {
            pageSize = 1;
        }

        Condition condition = PROJECTID.eq(config.getId());

        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUser.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    public void updateUserStatus(String userId, boolean enabled) {
        dslContext.update(TABLE)
                .set(ENABLED, enabled)
                .where(ID.eq(userId))
                .execute();
    }
}
