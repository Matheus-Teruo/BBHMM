package com.BBHMM.backend.BBHMM.infra.auth;

import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = repository.findByUsernameReturnDetails(username);

    if (userDetails == null) {
        throw new UsernameNotFoundException("Usuário não encontrado");
    }

    return userDetails;
  }
}