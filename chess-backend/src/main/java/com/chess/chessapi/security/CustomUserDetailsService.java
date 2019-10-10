package com.chess.chessapi.security;

import com.chess.chessapi.entities.User;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.repositories.UserRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Object object =  userRepository.findByEmailCustom(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );
        User user = ManualCastUtils.castObjectToUserByFindCustom(object);
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Object object = userRepository.findByIdCustom(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        User user = ManualCastUtils.castObjectToUserByFindCustom(object);
        return UserPrincipal.create(user);
    }
}
