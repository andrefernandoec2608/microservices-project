package com.andre.account.repository;

import com.andre.account.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountId(Long accountId);
}