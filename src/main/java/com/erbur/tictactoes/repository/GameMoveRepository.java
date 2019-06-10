package com.erbur.tictactoes.repository;

import com.erbur.tictactoes.model.entities.GameMoveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMoveRepository extends JpaRepository<GameMoveEntity, Long> {
}
