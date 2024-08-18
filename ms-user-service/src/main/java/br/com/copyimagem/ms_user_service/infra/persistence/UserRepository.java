package br.com.copyimagem.ms_user_service.infra.persistence;

import br.com.copyimagem.ms_user_service.core.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail( String email);
}
