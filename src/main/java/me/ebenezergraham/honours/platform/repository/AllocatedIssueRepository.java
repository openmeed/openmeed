package me.ebenezergraham.honours.platform.repository;

import me.ebenezergraham.honours.platform.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Repository
public interface AllocatedIssueRepository extends JpaRepository<Issue, Long> {

    Optional<Issue> findIssueByUrl(String issueUrl);

    Optional<List<Issue>> findIssuesByAssigneeName(String assignee);
}
