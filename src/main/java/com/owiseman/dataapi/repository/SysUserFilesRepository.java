package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysUserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SysUserFilesRepository extends JpaRepository<SysUserFile, UUID> {

    Optional<SysUserFile> findByIdAndUserId(String fid, String userId);

    List<SysUserFile> findByUserId(String userId);

    // 删除用户文件
    @Modifying
    @Query("DELETE FROM SysUserFile f WHERE f.fid = :fid AND f.userId =: userId")
    void deleteByIdAndUserId(@Param("fid") String fid, @Param("userId") String userId);
}
