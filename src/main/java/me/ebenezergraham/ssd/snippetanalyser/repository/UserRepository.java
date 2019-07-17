package me.ebenezergraham.ssd.snippetanalyser.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
	
	List<User> findByName(String name);
	
	@Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.username = ?#{ principal?.username }")
	@Modifying
	@Transactional
	public void updateLastLogin(@Param("lastLogin") Date lastLogin);
}
