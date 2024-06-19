package aiss.gitminer.repository;

import aiss.gitminer.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
    Page<Issue> findByAuthorId(String authorId, Pageable pageable);
    Page<Issue> findByState(String state, Pageable pageable);
    Page<Issue> findByAuthorIdAndState(String authorId, String state, Pageable pageable);
}
