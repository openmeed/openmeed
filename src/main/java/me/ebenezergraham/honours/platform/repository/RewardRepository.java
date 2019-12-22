package me.ebenezergraham.honours.platform.repository;

import me.ebenezergraham.honours.platform.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<Reward> findRewardByIssueId(String issueId);
}
