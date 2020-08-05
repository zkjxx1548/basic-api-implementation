package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
    rsEventList.add(new RsEvent("第一条事件", "无"));
    rsEventList.add(new RsEvent("第二条事件", "无"));
    rsEventList.add(new RsEvent("第三条事件", "无"));
    return rsEventList;
  }

  @GetMapping("/rs/list/{index}")
  public RsEvent getRsEventStringByIndex(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventStringByStartToEnd(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null && end == null) {
      return rsList;
    }
    if (start == null) {
      return rsList.subList(0, end);
    }
    if (end == null) {
      return rsList.subList(start - 1, rsList.size());
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!userController.getUsers().contains(rsEvent.getUser())) {
      userController.registerUser(rsEvent.getUser());
    }
    rsList.add(rsEvent);
  }

  @PatchMapping("/rs/event/{index}")
  public void patchRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    if (rsEvent.getEventName() != null) {
      rsList.get(index - 1).setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null) {
      rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
    }
  }

  @DeleteMapping("/rs/event")
  public void deleteRsEvent(@RequestParam int delete) {
    rsList.remove(delete - 1);
  }
}


