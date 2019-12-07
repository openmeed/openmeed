package me.ebenezergraham.honours.platform.repository;

import me.ebenezergraham.honours.platform.model.Project;
import me.ebenezergraham.honours.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Repository
public interface ActivatedRepository extends JpaRepository<Project, Long> {
  List<Project> findAll();
}
