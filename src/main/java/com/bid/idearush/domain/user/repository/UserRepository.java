package com.bid.idearush.domain.user.repository;

import com.bid.idearush.domain.user.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
