package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.model.VerificationToken;
import cz.landspa.statsapp.repository.VerificationTokenRepository;
import cz.landspa.statsapp.service.VerificationTokenService;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public VerificationToken getTokenByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }
}
