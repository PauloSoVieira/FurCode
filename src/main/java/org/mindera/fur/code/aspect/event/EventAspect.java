package org.mindera.fur.code.aspect.event;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mindera.fur.code.model.Event;
import org.mindera.fur.code.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Aspect
public class EventAspect {

    private static final Logger logger = LoggerFactory.getLogger(EventAspect.class);
    private final EventService eventService;

    @Autowired
    public EventAspect(EventService eventService) {
        this.eventService = eventService;
    }

    @Around("@annotation(logEvent)")
    public Object logEvent(ProceedingJoinPoint joinPoint, LogEvent logEvent) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        Instant logTime = Instant.now();
        String logMessage = "Call to '%s' at %s took %s ms"
                .formatted(joinPoint.getSignature().getName(), logTime, totalTime);

        if (!logEvent.obs().isEmpty()) {
            logMessage += " - " + logEvent.obs();
        }

        logger.info(logMessage);
        if (logEvent.toDB()) {
            Event event = new Event();
            event.setDescription(logMessage);
            event.setCreatedAt(logTime);
            eventService.createEvent(event);
        }

        return result;
    }
}
