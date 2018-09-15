package jeff.cutigram.controller;

import jeff.cutigram.database.model.User;
import jeff.cutigram.payload.request.UserModifyRequest;
import jeff.cutigram.payload.response.ApiResponse;
import jeff.cutigram.payload.response.User.UserInfoResponse;
import jeff.cutigram.security.CurrentUser;
import jeff.cutigram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyAccount(@CurrentUser User currentUser) {
        User user = (User) userService.loadUserById(currentUser.getId());
        return ResponseEntity.ok(new UserInfoResponse(
                user.getId(),
                user.getDisplayName(),
                user.getPhotoSrc()
        ));
    }

    @PutMapping("/me/profile")
    public ResponseEntity<?> updateMyProfile(@CurrentUser User currentUser,
                                             @Valid @RequestBody UserModifyRequest userModifyRequest) {
        User user = (User) userService.updateUserProfile(currentUser.getId(), userModifyRequest.getPhotoSrc());
        return ResponseEntity.ok(new ApiResponse(true, "success"));
    }
}
