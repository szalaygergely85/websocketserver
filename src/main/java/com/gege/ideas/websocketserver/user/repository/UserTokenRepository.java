package com.gege.ideas.websocketserver.user.repository;

import com.gege.ideas.websocketserver.user.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findUserTokenByToken(String token);

    UserToken findByUserTokenId(Long id);
}
