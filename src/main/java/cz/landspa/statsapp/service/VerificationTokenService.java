package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.model.VerificationToken;
import org.springframework.stereotype.Service;

@Service
public interface VerificationTokenService {
    VerificationToken getTokenByUser(User user);
}
