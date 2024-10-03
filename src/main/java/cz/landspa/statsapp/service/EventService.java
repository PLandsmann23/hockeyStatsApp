package cz.landspa.statsapp.service;


import cz.landspa.statsapp.model.DTO.event.EventDTO;
import cz.landspa.statsapp.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<EventDTO> getGameEvents(Long gameId);

    Long getScoredGoals(Long gameId);
    Long getConcededGoals(Long gameId);

    List<Event> getEventsNoDTO(Long gameId);

    EventDTO getById(Long eventId);
    void deleteEvent(Long eventId);

    boolean isEventOwner(Long eventId);
}
