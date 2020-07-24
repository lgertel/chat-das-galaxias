package com.arquitetodasgalaxias.chat.query.rooms.participants;

import com.arquitetodasgalaxias.chat.coreapi.ParticipantJoinedRoomEvent;
import com.arquitetodasgalaxias.chat.coreapi.ParticipantLeftRoomEvent;
import com.arquitetodasgalaxias.chat.coreapi.RoomParticipantsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomParticipantsProjection {

  private final RoomParticipantsRepository repository;

  public RoomParticipantsProjection(RoomParticipantsRepository repository) { this.repository = repository; }

  @QueryHandler
  public List<String> handle(RoomParticipantsQuery query) {
    return repository.findRoomParticipantsByRoomId(query.getRoomId())
        .stream()
        .map(RoomParticipant::getParticipant).sorted().collect(Collectors.toList());
  }

  @EventHandler
  public void on(ParticipantJoinedRoomEvent event) {
    repository.save(new RoomParticipant(event.getRoomId(), event.getParticipant()));
  }

  @EventHandler
  public void on(ParticipantLeftRoomEvent event) {
    repository.deleteByParticipantAndRoomId(event.getParticipant(), event.getRoomId());
  }
}
