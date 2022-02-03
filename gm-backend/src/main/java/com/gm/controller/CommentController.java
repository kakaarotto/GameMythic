package com.gm.controller;

import com.gm.config.Constants;
import com.gm.model.Comment;
import com.gm.model.User;
import com.gm.query.CommentQuery;
import com.gm.repository.CommentRepository;
import com.gm.repository.UserRepository;
import com.gm.service.CommentService;
import com.gm.service.MessageService;
import com.gm.service.UserService;
import com.gm.dto.CommentDTO;
import com.gm.service.mapper.CommentMapper;
import com.gm.util.ResponseUtil;
import com.gm.vm.CommentVM;
import com.gm.vm.MessageVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class CommentController extends BaseController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MessageService messageService;

    /**
     * Create comment
     * @param vm
     * @return
     */
    @PostMapping(path = "/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentVM vm) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Comment comment = commentService.create(vm, user.get());

        // Get the user info based on the user id of the created content
        User contentUser = userRepository.findUserByUserId(comment.getGoods().getUser().getId());
        if(contentUser.getNotificationEnabled()) {
            // Create message
            MessageVM message = new MessageVM();
            message.setType(Constants.MESSAGE_commented);
            message.setFromUserId(user.get().getId());
            message.setFromTypeId(comment.getId());
            message.setTitle("Some one commented your content");
            message.setContent("Some one commented your content");
            message.setReadState(0);
            message.setAddTime(LocalDateTime.now());
            messageService.create(message, contentUser);
        }
        return ResponseUtil.renderSuccess("Created successfully", CommentMapper.INSTANCE.modelToDTO(comment));
    }

}
