package tw.niq.api.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.niq.api.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(String userId);
	
	Optional<User> findByUsername(String username);

	void deleteByUserId(String userId);
	
	List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean accountNonLocked, Timestamp timestamp);
	
}
