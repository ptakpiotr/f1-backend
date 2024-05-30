package zti.f1backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import zti.f1backend.models.RaceScore;

public interface RaceScoreRepository extends JpaRepository<RaceScore, Integer> {

}
