package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.Saves;
import cz.landspa.statsapp.model.Shot;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.repository.ShotRepository;
import cz.landspa.statsapp.service.ShotService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShotServiceImpl implements ShotService {

    private final ShotRepository shotRepository;

    public ShotServiceImpl(ShotRepository shotRepository) {
        this.shotRepository = shotRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#shot.game.id)")
    public Shot newShots(Shot shot) {
        return shotRepository.save(shot);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public Shot getGamePeriodShots(Long gameId, Integer period) {
        return shotRepository.getShotByGameIdAndPeriod(gameId, period);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<Shot> getGameShots(Long gameId) {
        return shotRepository.findAllByGameId(gameId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public Shot addOneShot(Long gameId, Integer period) {
        Shot foundShot = shotRepository.getShotByGameIdAndPeriod(gameId, period);

            foundShot.setShots(foundShot.getShots() + 1);


        return shotRepository.save(foundShot);

    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public Shot removeOneShot(Long gameId, Integer period) {
        Shot foundShot = shotRepository.getShotByGameIdAndPeriod(gameId, period);
        if(foundShot.getShots()>0){
            foundShot.setShots(foundShot.getShots() - 1);
        }

        return shotRepository.save(foundShot);
    }
}
