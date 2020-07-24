package com.arquitetodasgalaxias.chat.restapi;

import com.arquitetodasgalaxias.chat.coreapi.AllRoomsQuery;
import com.arquitetodasgalaxias.chat.coreapi.RoomMessagesQuery;
import com.arquitetodasgalaxias.chat.coreapi.RoomParticipantsQuery;
import com.arquitetodasgalaxias.chat.query.rooms.messages.ChatMessage;
import com.arquitetodasgalaxias.chat.query.rooms.summary.RoomSummary;
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.management.Query;
import java.util.List;
import java.util.concurrent.Future;

@RestController
public class QueryController {

  private final QueryGateway queryGateway;

  public QueryController(QueryGateway queryGateway) { this.queryGateway = queryGateway; }

  @GetMapping("rooms")
  public Future<List<RoomSummary>> listRooms() {
    return queryGateway.query(new AllRoomsQuery(), new MultipleInstancesResponseType<>(RoomSummary.class));
  }

  @GetMapping("/rooms/{roomId}/participants")
  public Future<List<String>> participantsInRoom(@PathVariable String roomId) {
    return queryGateway.query(new RoomParticipantsQuery(roomId), new MultipleInstancesResponseType<>(String.class));
  }

  @GetMapping("/rooms/{roomId}/messages")
  public Future<List<ChatMessage>> roomMessages(@PathVariable String roomId) {
    return queryGateway.query(new RoomMessagesQuery(roomId), new MultipleInstancesResponseType<>(ChatMessage.class));
  }

  @GetMapping(value = "/rooms/messages/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ChatMessage> subscribeRoomMessage(@PathVariable String roomId) {
    RoomMessagesQuery query = new RoomMessagesQuery(roomId);
    SubscriptionQueryResult<List<ChatMessage>, ChatMessage> result;
    result = queryGateway.subscriptionQuery(query, ResponseTypes.multipleInstancesOf(ChatMessage.class), ResponseTypes.instanceOf(ChatMessage.class));

    Flux<ChatMessage> initialReult = result.initialResult().flatMapMany(Flux::fromIterable);
    return Flux.concat(initialReult, result.updates());
  }
}
