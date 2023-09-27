package com.bid.idearush.domain.user.repository;

import com.bid.idearush.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserAccountId(String userAccountId);
    Optional<Users> findByNickname(String nickname);
}
