package com.calendar.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataEventRepository extends JpaRepository<EventEntity, String> {

    List<EventEntity> findByNotifiedFalse();
}