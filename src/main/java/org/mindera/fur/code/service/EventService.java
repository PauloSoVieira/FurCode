package org.mindera.fur.code.service;

import org.mindera.fur.code.model.Event;
import org.mindera.fur.code.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }
}
