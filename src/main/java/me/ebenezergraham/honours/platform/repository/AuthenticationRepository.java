package me.ebenezergraham.honours.platform.repository;

import me.ebenezergraham.honours.platform.model.TwoFactor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
public interface AuthenticationRepository extends CrudRepository<TwoFactor, Long> {
    Optional<TwoFactor> findTwoFactorByUsername(String username);

}
