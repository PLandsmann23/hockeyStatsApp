package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.*;
import cz.landspa.statsapp.model.DTO.event.*;
import cz.landspa.statsapp.repository.EventRepository;
import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final SecurityUtil securityUtil;

    public EventServiceImpl(EventRepository eventRepository, SecurityUtil securityUtil) {
        this.eventRepository = eventRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#eventId)")
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#eventId)")
    public EventDTO getById(Long eventId){
        Event event = eventRepository.findById(eventId).orElse(null);

        if(event != null) {
            if (event instanceof GoalScored) {
                return GoalScoredDTO.fromGoalScored((GoalScored) event);
            }if (event instanceof GoalConceded) {
                return GoalConcededDTO.fromGoalConceded((GoalConceded) event);
            } if (event instanceof Penalty) {
                return PenaltyDTO.fromPenalty((Penalty) event);
            }if (event instanceof OpponentPenalty) {
                return OpponentPenaltyDTO.fromOpponentPenalty((OpponentPenalty) event);
            }
            else {
                return EventDTO.fromEvent(event);
            }
        } else {
            return null;
        }
    }

    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<EventDTO> getGameEvents(Long gameId) {
        List<Event> events = eventRepository.findAllByGameIdOrderByTimeAsc(gameId);

        // Převeď každou entitu Event na DTO
        return events.stream()
                .map(event -> {
                    if (event instanceof GoalScored) {
                        return GoalScoredDTO.fromGoalScored((GoalScored) event);
                    }if (event instanceof GoalConceded) {
                        return GoalConcededDTO.fromGoalConceded((GoalConceded) event);
                    } if (event instanceof Penalty) {
                        return PenaltyDTO.fromPenalty((Penalty) event);
                    }if (event instanceof OpponentPenalty) {
                        return OpponentPenaltyDTO.fromOpponentPenalty((OpponentPenalty) event);
                    }
                    else {
                        return EventDTO.fromEvent(event);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<Event> getEventsNoDTO(Long gameId){
    return eventRepository.findAllByGameIdOrderByTimeAsc(gameId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public Long getScoredGoals(Long gameId) {
        return eventRepository.scoredGoalsInGame(gameId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public Long getConcededGoals(Long gameId) {
        return eventRepository.concededGoalsInGame(gameId);
    }

    @Override
    public boolean isEventOwner(Long eventId) {
        Team team = Objects.requireNonNull(eventRepository.findById(eventId).orElse(null)).getGame().getTeam();

        if (team == null) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }
}
