package com.gege.ideas.websocketserver.user.service;

import com.gege.ideas.websocketserver.user.entity.User;
import com.gege.ideas.websocketserver.user.entity.UserToken;
import com.gege.ideas.websocketserver.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenService userTokenService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserTokenService userTokenService
    ) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
    }

    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    public Long getUserIdByToken(String token) {
        UserToken userToken = userTokenService.getUserTokenByToken(token);
        User user = userRepository.findByUserTokenId(userToken.getUserTokenId());

        return user.getUserId();
    }
}
