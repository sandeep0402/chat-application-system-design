package gchat.group;

import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("group")
@PreAuthorize("isAuthenticated()")
public class ChatGroupController {
    public static final String SUCCESS = "Success";

    private final ChatGroupService groupService;

    @PostMapping("/")
    public ResponseEntity<Long> create(@ApiParam("name") @RequestParam String name) {
        return ResponseEntity.ok(groupService.create(name));
    }

    @DeleteMapping("/")
    public ResponseEntity<String> leave(@ApiParam("name") @RequestParam String name) {
         groupService.leave(name);
         return ResponseEntity.ok(SUCCESS);
    }

    @PutMapping("/")
    public ResponseEntity<String> join(@ApiParam("name") @RequestParam String name) {
        groupService.join(name);
        return ResponseEntity.ok(SUCCESS);
    }

    @GetMapping("/")
    public ResponseEntity<List<NameIdPair>> myGroups() {
        return ResponseEntity.ok(groupService.listGroups(true));
    }

    @GetMapping("/list")
    public ResponseEntity<List<NameIdPair>> listAllGroups() {
        return ResponseEntity.ok(groupService.listGroups(false));
    }
}
