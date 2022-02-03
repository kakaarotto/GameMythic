package com.gm.controller;

import com.alibaba.fastjson.JSONObject;
import com.gm.config.Constants;
import com.gm.dto.UserFollowDTO;
import com.gm.model.Collect;
import com.gm.model.User;
import com.gm.repository.CollectRepository;
import com.gm.repository.UserRepository;
import com.gm.service.MessageService;
import com.gm.service.UserService;
import com.gm.service.mapper.UserMapper;
import com.gm.exception.ExceptionResponse;
import com.gm.util.JacksonUtil;
import com.gm.util.ResponseUtil;
import com.gm.vm.ChangePasswordVM;
import com.gm.vm.MessageVM;
import com.gm.vm.PasswordVM;
import com.gm.vm.UserVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final CollectRepository collectRepository;
    private final MessageService messageService;
    EntityManager entityManager;

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    public ResponseEntity<?> getAccount() {
        Optional<User> optional = userService.getCurrentUser();
        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(-9, "User information does not exist"));
        }
        log.info("{}", optional.get());
        log.info("{}", UserMapper.INSTANCE.modelToDTO(optional.get()));
        return ResponseUtil.renderSuccess("Acquired success", UserMapper.INSTANCE.modelToDTO(optional.get()));
    }

    /**
     * Find Account
     * @param username
     * @return
     */
    @RequestMapping(path = "/account/findAccount")
    public ResponseEntity<?> findAccount(String username) {
        Optional<User> result = userRepository.findOneByUsernameIgnoreCase(username);
        return result
                .map(user -> new ResponseEntity<>(new ExceptionResponse(1, "The username already exists"), HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ExceptionResponse(0, "Username is available"), HttpStatus.OK));
    }

    /**
     * Find Email
     * @param email
     * @return
     */
    @RequestMapping(path = "/account/findEmail")
    public ResponseEntity<?> findEmail(String email) {
        Optional<User> result = userRepository.findOneByEmailIgnoreCase(email);
        return result
                .map(user -> new ResponseEntity<>(new ExceptionResponse(1, "The email already exists"), HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ExceptionResponse(0, "Email is available"), HttpStatus.OK));
    }


    /**
     * Update the current user information.
     * @param vm the current user information
     * @return
     */
    @PutMapping("/account")
    public ResponseEntity updateAccount(@Valid @RequestBody UserVM vm) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.expires();
        }
        return userRepository.findOneByUsernameIgnoreCase(userOptional.get().getUsername())
                .map(u -> {
                    u = userService.update(u, vm);
                    String response = "{\"code\":1,\"message\":\"update completed\",\"" + "content" + "\":" + JacksonUtil.toJSON(UserMapper.INSTANCE.modelToDTO(u)) + "}";
                    return new ResponseEntity(response, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseUtil.renderError("Error"));
    }


    /**
     * Match password
     * @param passwordVM
     * @return
     */
    @PostMapping("/account/matchPassword")
    public ResponseEntity<?> matchPassword(@RequestBody PasswordVM passwordVM) {
        Optional<User> optional = userService.getCurrentUser();
        if (!optional.isPresent()) {
            return ResponseUtil.renderError("Login error");
        }
        if (passwordVM == null || StringUtils.isEmpty(passwordVM.getPassword())) {
            return ResponseUtil.renderError("Please enter password");
        }
        Boolean result = userService.matches(passwordVM.getPassword(), optional.get().getPassword());
        return ResponseUtil.renderSuccess("Test results", result);
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= UserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= UserVM.PASSWORD_MAX_LENGTH);
    }

    /**
     *  Account registration
     * @param userVM
     * @return registration results
     */
    @PostMapping(path = "/account/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserVM userVM) {
        Optional<User> user = userRepository.findOneByUsername(userVM.getUsername());
        if (user.isPresent()) {
            return ResponseUtil.renderError("Users are registered");
        }
        userService.create(userVM);
        return ResponseUtil.renderSuccess("Registration successful");
    }


    /**
     * Change username
     * @param username
     * @return
     */
    @PostMapping(path = "/account/updateUsername")
    public ResponseEntity<?> updateUsername(@Valid @RequestParam String username) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        // Whether the matching username exists
        Optional<User> exists = userRepository.findOneByUsername(username);
        if (exists.isPresent()) {
            return ResponseUtil.renderError("The username already exist");
        }

        User user  = userOptional.get();
        user.setUsername(username);
        userRepository.save(user);

        return ResponseUtil.renderSuccess("Update username success");
    }

    /**
     * Changes the current user's password
     * @param vm the new password
     * @return
     */
    @PostMapping(path = "/account/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordVM vm) {
        if (!checkPasswordLength(vm.getPassword())) {
            return ResponseUtil.renderError("Password must be between 6 and 16 digits");
        }
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.expires();
        }
        if (!userService.matches(vm.getOldPassword(), userOptional.get().getPassword())) {
            return ResponseUtil.renderError("Old password is incorrect");
        }
        userService.changePassword(userOptional.get(), vm.getPassword());
        return new ResponseEntity<>(new ExceptionResponse(1, "Password change successful"), HttpStatus.OK);
    }

    /**
     * Update user avatar
     * @param avatar
     * @return
     */
    @PostMapping(path = "/account/updateAvatar")
    public ResponseEntity<?> updateAvatar(@Valid @RequestParam String avatar) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        User user  = userOptional.get();
        user.setAvatar(avatar);
        userRepository.save(user);

        return ResponseUtil.renderSuccess("Update avatar success");
    }

    /**
     * Update notification activation status
     * @param enabled
     * @return
     */
    @PostMapping(path = "/account/updateNotificationEnabled")
    public ResponseEntity<?> updateNotificationEnabled(@Valid @RequestParam Boolean enabled) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.renderError(-9, "Not Login");
        }

        User user  = userOptional.get();
        user.setNotificationEnabled(enabled);
        userRepository.save(user);

        return ResponseUtil.renderSuccess("Update notification enabled success");
    }

    /**
     * Follow other users
     * @param userId
     * @return
     */
    @PostMapping(path = "/account/{userId}/collect")
    public ResponseEntity<?> follow(@Valid @PathVariable Integer userId) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Query the following user id and the followed user id
        Collect followUser = collectRepository.findFirstByCollectUserIdAndUserId(userId,user.get().getId());
        log.info("followUser=" + followUser);
        // If you have not followed the user, set to following
        if(followUser == null){
            Collect collect = new Collect();
            collect.setCollectUserId(userId);
            collect.setUser(user.get());
            collectRepository.save(collect);

            User toUser = userRepository.findUserByUserId(userId);
            // Create a message to notify the user if the user notification is turned on
            if(toUser.getNotificationEnabled()) {
                // Create a message
                MessageVM message = new MessageVM();
                message.setType(Constants.MESSAGE_followed);
                message.setFromUserId(user.get().getId());
                // Set message type
                message.setFromTypeId(collect.getId());
                message.setTitle("Some one followed you");
                message.setContent("Some one followed you");
                message.setReadState(0);
                message.setAddTime(LocalDateTime.now());
                messageService.create(message, toUser);
            }
        }
        return ResponseUtil.renderSuccess("Created successfully");
    }

    /**
     * Unfollow other user
     * @param userId
     * @return
     */
    @PostMapping(path = "/account/{userId}/un-collect")
    public ResponseEntity<?> unfollow(@Valid @PathVariable Integer userId) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Delete data based on user id and followed user id
        userService.deleteByCollectUserIdAndUserId(userId, user.get());
        return ResponseUtil.renderSuccess("Unfollow successful");
    }


    /**
     * Get my following
     * @return
     */
    @GetMapping(path = "/account/myFollowing")
    public ResponseEntity<?> getMyFollowing() {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Query all my following information based on user id
        String collectSql = "SELECT * FROM `collect` WHERE `collect_user_id`>0 AND `user_id`=" + user.get().getId() + " ORDER BY add_time desc";
        Query collectQuery = entityManager.createNativeQuery(collectSql, Collect.class);
        List<Collect> collects = collectQuery.getResultList();

        // Query all the user information I follow according to the user id
        String userSql = "SELECT * FROM `user` WHERE `id` IN (SELECT `collect_user_id` FROM `collect` WHERE `collect_user_id`>0 AND `user_id`=" + user.get().getId() + ")";
        Query userQuery = entityManager.createNativeQuery(userSql, User.class);
        List<User> data = userQuery.getResultList();

        // Traverse all the information I follow and set the basic information of the follower
        List<UserFollowDTO> results = new ArrayList<>();
        for(Collect collect : collects) {
            data.stream().filter(m->m.getId().equals(collect.getCollectUserId())).findFirst().ifPresent(u->{
                UserFollowDTO dto = new UserFollowDTO();
                // Set the user information of the follower
                dto.setUserId(u.getId());
                dto.setAvatar(u.getAvatar());
                dto.setEmail(u.getEmail());
                dto.setUsername(u.getUsername());
                dto.setFollowTime(collect.getAddTime());
                dto.setFollowing(true);
                results.add(dto);
            });
        }
        return ResponseUtil.renderSuccess(results);
    }

    /**
     * Get me followers
     * @return
     */
    @GetMapping(path = "/account/myFollowers")
    public ResponseEntity<?> getMyFollowers() {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        //Query all the information followers me based on user id
        String collectSql = "SELECT * FROM `collect` WHERE `collect_user_id`>0 AND `collect_user_id`=" + user.get().getId() + " ORDER BY add_time desc";
        Query collectQuery = entityManager.createNativeQuery(collectSql, Collect.class);
        List<Collect> collects = collectQuery.getResultList();

        // Query all the user information I follow according to the user id
        String userSql ="SELECT * FROM `user` WHERE `id` IN (SELECT `user_id` FROM `collect` WHERE `collect_user_id`=" + user.get().getId() + ")";
        Query userQuery = entityManager.createNativeQuery(userSql, User.class);
        List<User> data = userQuery.getResultList();

        // Get my follow list, whether followed each other
        String myCollectSql = "SELECT * FROM `collect` WHERE `collect_user_id`>0 AND `user_id`=" + user.get().getId();
        Query myCollectQuery = entityManager.createNativeQuery(myCollectSql, Collect.class);
        List<Collect> myCollects = myCollectQuery.getResultList();

        // raverse all the information I follow and set the basic information of the follower
        List<UserFollowDTO> results = new ArrayList<>();
        for(Collect collect : collects) {
            data.stream().filter(m->m.getId().equals(collect.getUser().getId())).findFirst().ifPresent(u -> {
                UserFollowDTO dto = new UserFollowDTO();
                // Set the user information of the follower
                dto.setUserId(u.getId());
                dto.setAvatar(u.getAvatar());
                dto.setEmail(u.getEmail());
                dto.setUsername(u.getUsername());
                dto.setFollowTime(collect.getAddTime());
                // Find if the user has been followed
                dto.setFollowing(myCollects.stream().anyMatch(m->m.getCollectUserId().equals(u.getId())));

                results.add(dto);
            });
        }
        return ResponseUtil.renderSuccess(results);
    }


    /**
     * Get follow count
     * @return
     */
    @GetMapping("/account/follow-count")
    public ResponseEntity<?> getFollowCount() {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        //Query following information based on user id
        String collectSql = "SELECT * FROM `collect` WHERE `collect_user_id`>0 AND `user_id`=" + user.get().getId();
        Query collectQuery = entityManager.createNativeQuery(collectSql, Collect.class);
        List<Collect> collects = collectQuery.getResultList();

        int followingCount = collects.size();

        //Query follower information based on user information
        String collectSql2 = "SELECT * FROM `collect` WHERE `collect_user_id`>0 AND `collect_user_id`=" + user.get().getId();
        Query collectQuery2 = entityManager.createNativeQuery(collectSql2, Collect.class);
        List<Collect> collects2 = collectQuery2.getResultList();

        int followerCount = collects2.size();

        JSONObject object = new JSONObject();
        // following number
        object.put("followingCount", followingCount);
        // follower number
        object.put("followerCount", followerCount);

        String response = "{\"code\":1,\"message\":\"success\",\"" + "content" + "\":" + object.toJSONString() + "}";
        return ResponseEntity.ok().body(response);
    }

}
