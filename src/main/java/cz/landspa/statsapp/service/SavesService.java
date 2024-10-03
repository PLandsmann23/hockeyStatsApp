package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Saves;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavesService {
    Saves newSaves(Saves saves);

    Saves addOneSave(Long gameId, Long goalkeeperId, Integer period);

    Saves removeOneSave(Long gameId, Long goalkeeperId, Integer period);

    List<Saves> getGameSaves(Long gameId);

    List<Saves> getGameGoalieSaves(Long gameId, Long gkId);

    Saves getByGameGoalkeeperPeriod(Long gameId, Long goalkeeperId, Integer period);
}
