package com.danzir.scambio.figurine.data.repository;

import com.danzir.scambio.figurine.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
