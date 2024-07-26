package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
