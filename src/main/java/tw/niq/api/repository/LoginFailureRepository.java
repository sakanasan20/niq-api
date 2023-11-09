package tw.niq.api.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.api.domain.LoginFailure;
import tw.niq.api.domain.User;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Long> {
	
	List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);

}
