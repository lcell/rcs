package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteOffRepository extends JpaRepository<WriteOffEntity, Long> {
}
