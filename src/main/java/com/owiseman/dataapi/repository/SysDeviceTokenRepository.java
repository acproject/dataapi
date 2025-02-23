package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysDeviceToken;
import com.owiseman.dataapi.dto.Platform;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSDEVICETOKEN.*;

@Repository
public class SysDeviceTokenRepository {

    @Autowired
    private DSLContext dslContext;

    // 基础CRUD操作
    public SysDeviceToken save(SysDeviceToken token) {
        dslContext.insertInto(TABLE)
                .set(ID, token.getId())
                .set(USERID, token.getUserId())
                .set(TOKEN, token.getToken())
                .set(PLATFORM, token.getPlatform().name()) // 枚举转字符串
                .set(LASTACTIVE, token.getLastActive())
                .set(ACTIVE, token.getActive())
                .execute();
        return token;
    }

    public void update(SysDeviceToken token) {
        dslContext.update(TABLE)
                .set(USERID, token.getUserId())
                .set(TOKEN, token.getToken())
                .set(PLATFORM, token.getPlatform().name())
                .set(LASTACTIVE, token.getLastActive())
                .set(ACTIVE, token.getActive())
                .where(ID.eq(token.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysDeviceToken> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysDeviceToken.class);
    }

    public List<SysDeviceToken> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysDeviceToken.class);
    }

    // 原JPA方法转换
    public Boolean isValidToken(String token) {
        return (Boolean) dslContext.select(ACTIVE)
                .from(TABLE)
                .where(TOKEN.eq(token))
                .fetchOptional(0, Boolean.class)
                .orElse(false);
    }

    @Transactional
    public void invalidateToken(String token) {
        dslContext.update(TABLE)
                .set(ACTIVE, false)
                .where(TOKEN.eq(token))
                .execute();
    }

    @Transactional
    public void updateLastUsed(String token) {
        dslContext.update(TABLE)
                .set(LASTACTIVE, LocalDateTime.now())
                .where(TOKEN.eq(token))
                .execute();
    }

    // 新增分页查询方法
    public PageResult<SysDeviceToken> findByUserIdWithPagination(String userId,
                                                                int pageNumber,
                                                                int pageSize,
                                                                SortField<?>... sortFields) {
        Condition condition = USERID.eq(userId);

        List<SysDeviceToken> tokens = dslContext.selectFrom(TABLE)
                .where(condition)
                .orderBy(sortFields)
                .limit(pageSize)
                .offset((pageNumber - 1) * pageSize)
                .fetchInto(SysDeviceToken.class);

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(tokens, pageNumber, pageSize, total);
    }

    // 扩展方法：按平台查询有效令牌
    public List<SysDeviceToken> findActiveTokensByPlatform(Platform platform) {
        return dslContext.selectFrom(TABLE)
                .where(PLATFORM.eq(platform.name()))
                .and(ACTIVE.eq(true))
                .fetchInto(SysDeviceToken.class);
    }

    // 扩展方法：批量失效过期令牌
    @Transactional
    public int invalidateExpiredTokens(LocalDateTime expireTime) {
        return dslContext.update(TABLE)
                .set(ACTIVE, false)
                .where(LASTACTIVE.lt(expireTime))
                .execute();
    }



}
