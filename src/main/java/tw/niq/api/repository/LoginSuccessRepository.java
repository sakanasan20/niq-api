package tw.niq.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.api.domain.LoginSuccess;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Long> {

}
