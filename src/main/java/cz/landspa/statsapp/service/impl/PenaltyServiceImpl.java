package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.Penalty;
import cz.landspa.statsapp.repository.PenaltyRepository;
import cz.landspa.statsapp.service.PenaltyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class PenaltyServiceImpl implements PenaltyService {
    private final PenaltyRepository penaltyRepository;


    public PenaltyServiceImpl(PenaltyRepository penaltyRepository) {
        this.penaltyRepository = penaltyRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#penalty.game.id)")
    public Penalty addPenalty(Penalty penalty) {
        return penaltyRepository.save(penalty);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#id) && @gameServiceImpl.isGameOwner(#penalty.game.id)")
    public Penalty updatePenalty(Penalty penalty, Long id) {
        Penalty oldPenalty = penaltyRepository.findById(id).orElse(null);

        if(oldPenalty != null){
            if(penalty.getTime()!=null){
                oldPenalty.setTime(penalty.getTime());
            }
            if(penalty.getMinutes()!=null){
                oldPenalty.setMinutes(penalty.getMinutes());
            }
            if(penalty.getPlayer()!=null){
                oldPenalty.setPlayer(penalty.getPlayer());
            }
            if(penalty.getReason()!=null){
                oldPenalty.setReason(penalty.getReason());
            }
            if(penalty.getCoincidental()!=null){
                oldPenalty.setCoincidental(penalty.getCoincidental());
            }

            return penaltyRepository.save(oldPenalty);
        } else {
            return null;
        }
    }
}
