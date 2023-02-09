package com.group04.tgdd.service;


import com.group04.tgdd.dto.CurrentEventResp;
import com.group04.tgdd.dto.EventReq;
import com.group04.tgdd.model.Event;

import java.util.List;

public interface EventService {
    Event save(EventReq eventReq);

    List<Event> get(String name);

    void delete(String name);

    Event update(EventReq eventReq);

    List<CurrentEventResp> getCurrentEvent();

    Event addProductToEvent(Long eventId, Long productReq);
}
