package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketRequesterAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private final List<RsEvent> rsList = initRsList();
  private final UserController userController = new UserController();

  List<RsEvent> initRsList() {
    ArrayList<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("Tom", "male", 18, "1234678@tw.com", "12345678910");
    rsEventList.add(new RsEvent("第一条事件", "无", user));
    rsEventList.add(new RsEvent("第二条事件", "无", user));
    rsEventList.add(new RsEvent("第三条事件", "无", user));
    return rsEventList;
  }

  @GetMapping("/rs/list/{index}")
  public ResponseEntity getRsEventStringByIndex(@PathVariable int index) {
    if (index < 1) {
      throw new RsEventNotValidException();
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventStringByStartToEnd(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null && end == null) {
      return ResponseEntity.ok(rsList);
    }
    if (start == null) {
      return ResponseEntity.ok(rsList.subList(0, end));
    }
    if (end == null) {
      return ResponseEntity.ok(rsList.subList(start - 1, rsList.size()));
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!userController.getUsers().contains(rsEvent.getUser())) {
      userController.registerUser(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/event/{index}")
  public ResponseEntity patchRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    if (rsEvent.getEventName() != null) {
      rsList.get(index - 1).setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null) {
      rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
    }
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping("/rs/event")
  public ResponseEntity deleteRsEvent(@RequestParam int delete) {
    rsList.remove(delete - 1);
    return ResponseEntity.created(null).build();
  }


}


