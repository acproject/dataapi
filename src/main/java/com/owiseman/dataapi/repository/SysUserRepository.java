package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,String> {
    @Override
    @NotNull
    Optional<SysUser> findById(String s);
}
