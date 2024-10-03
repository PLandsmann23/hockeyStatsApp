package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.Saves;
import cz.landspa.statsapp.repository.SavesRepository;
import cz.landspa.statsapp.service.SavesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavesServiceImpl implements SavesService {
    private final SavesRepository savesRepository;

    public SavesServiceImpl(SavesRepository savesRepository) {
        this.savesRepository = savesRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#saves.game.id)&& @rosterServiceImpl.isRosterOwner(#saves.goalkeeper.id)")
    public Saves newSaves(Saves saves) {
        return savesRepository.save(saves);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)&& @rosterServiceImpl.isRosterOwner(#goalkeeperId)")
    public Saves addOneSave(Long gameId, Long goalkeeperId, Integer period) {
        Saves foundSave = savesRepository.getSavesByGameIdAndGoalkeeperIdAndPeriod(gameId, goalkeeperId, period);
        foundSave.setSaves(foundSave.getSaves()+1);

        return savesRepository.save(foundSave);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)&& @rosterServiceImpl.isRosterOwner(#goalkeeperId)")
    public Saves removeOneSave(Long gameId, Long goalkeeperId, Integer period) {
        Saves foundSave = savesRepository.getSavesByGameIdAndGoalkeeperIdAndPeriod(gameId, goalkeeperId, period);
        if(foundSave.getSaves()>0){
            foundSave.setSaves(foundSave.getSaves() - 1);
        }

        return savesRepository.save(foundSave);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<Saves> getGameSaves(Long gameId) {
        return savesRepository.findAllByGameId(gameId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)&& @rosterServiceImpl.isRosterOwner(#goalkeeperId)")
    public Saves getByGameGoalkeeperPeriod(Long gameId, Long goalkeeperId, Integer period) {
        return savesRepository.getSavesByGameIdAndGoalkeeperIdAndPeriod(gameId, goalkeeperId,period);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)&& @rosterServiceImpl.isRosterOwner(#gkId)")
    public List<Saves> getGameGoalieSaves(Long gameId, Long gkId) {
        return savesRepository.getSavesByGameIdAndGoalkeeperId(gameId, gkId);
    }


}
