package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Shot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShotService {

    Shot newShots(Shot shots);

    Shot getGamePeriodShots(Long gameId, Integer period);

    List<Shot> getGameShots(Long gameId);

    Shot addOneShot(Long gameId, Integer period);

    Shot removeOneShot(Long gameId, Integer period);
}
