package com.elice.iliceworksbe.team.repository;

import com.elice.iliceworksbe.team.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
