package com.gm.controller;

import com.alibaba.fastjson.JSONObject;
import com.gm.config.Constants;
import com.gm.dto.ContentDTO;
import com.gm.dto.MessageDTO;
import com.gm.model.Comment;
import com.gm.model.Content;
import com.gm.model.Message;
import com.gm.model.User;
import com.gm.query.IdsQuery;
import com.gm.query.MessageQuery;
import com.gm.repository.CommentRepository;
import com.gm.repository.ContentRepository;
import com.gm.repository.MessageRepository;
import com.gm.repository.UserRepository;
import com.gm.service.MessageService;
import com.gm.service.UserService;
import com.gm.service.mapper.ContentMapper;
import com.gm.service.mapper.MessageMapper;
import com.gm.service.mapper.UserMapper;
import com.gm.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class MessageController  extends BaseController{
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    EntityManager entityManager;

    /**
     * Get message list
     * @param pageable
     * @param query
     * @return
     */
    @GetMapping("/messages")
    public ResponseEntity<?> getList(@PageableDefault(sort = {"addTime"}, direction = Sort.Direction.DESC) Pageable pageable, MessageQuery query) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.expires();
        }
        query.setUserId(userOptional.get().getId());
        log.info("query={}",query);
        // Paging query message notification information
        Page<MessageDTO> page = messageService.search(query, pageable).map(MessageMapper.INSTANCE::modelToDTO);
        log.info("page.getContent()={}",page.getContent());

        // Get the id list of the user who left the message
        List<Integer> userIds = page.getContent().stream().map(MessageDTO::getFromUserId).collect(Collectors.toList());
        //Query user information according to the above list
        List<User> userList = userRepository.findAllById(userIds);
        for (MessageDTO dto : page.getContent()) {
            userList.stream().filter(m->m.getId().equals(dto.getFromUserId()))
                    .findFirst()
                    .ifPresent(user -> dto.setFromUser(UserMapper.INSTANCE.modelToDTO(user)));
            // If it’s like, get content
            if(dto.getType().equals(Constants.MESSAGE_liked)) {
                Content content = contentRepository.getById(dto.getFromTypeId());
                if(content != null) {
                    ContentDTO contentDTO = ContentMapper.INSTANCE.modelToDTO(content);
                    dto.setFromContent(contentDTO);
                }
            }
            // If it is a comment, get the content of the comment
            if(dto.getType().equals(Constants.MESSAGE_commented)) {
                Comment comment = commentRepository.getById(dto.getFromTypeId());
                if(comment != null) {
                    dto.setFromCommentContent(comment.getContent());
                    ContentDTO contentDTO = ContentMapper.INSTANCE.modelToDTO(comment.getGoods());
                    dto.setFromContent(contentDTO);
                }
            }
        }

        return ResponseUtil.renderSuccess(page);
    }

    /**
     * 修改状态
     */
    @PutMapping(path = "/message-read/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Integer id) {
        // The id of each notification message
        if (id == null) {
            return ResponseUtil.renderError("Missing parameters:id");
        }
        // Query the message according to the message id
        Message old = messageService.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Illegal parameter:id");
        }
        // Update reading status
        old = messageService.updateReadState(id, 1);
        return ResponseUtil.renderSuccess("Successfully modified", MessageMapper.INSTANCE.modelToDTO(old));
    }

    /**
     * Multiple change message read state
     * @param query
     * @return
     */
    @PostMapping(path = "/message-read-multiple")
    public ResponseEntity<?> updateMultipleReadState(@RequestBody IdsQuery query) {
        //  Query message notification id
        if (query.getIds() == null || query.getIds().size() == 0) {
            return ResponseUtil.renderError("Missing parameters:ids");
        }
        // Iterate to update multiple message reading status
        List<Message> messages = messageRepository.findAllById(query.getIds());
        for (Message object : messages) {
            object.setReadState(1);
            messageRepository.save(object);
        }

        return ResponseUtil.renderSuccess("Successfully modified");
    }

    /**
     * Delete message
     * @param id
     * @return
     */
    @DeleteMapping(path = "/message/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Message old = messageService.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Missing parameters:id");
        }
        messageService.delete(old.getId());
        return ResponseUtil.renderSuccess("successfully deleted");
    }

    /**
     * Get the number of messages
     * @return
     */
    @GetMapping("/message-count")
    public ResponseEntity<?> getMessageCount() {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Query notification messages based on user id
        String sql = "SELECT * FROM `message` WHERE `read_state`=0 and `user_id`=" + user.get().getId();
        Query query = entityManager.createNativeQuery(sql, Message.class);
        List<Message> messages = query.getResultList();

        // Get these 3 types of count and assign values
        long followerCount = messages.stream().filter(m->m.getType() == Constants.MESSAGE_followed).count();
        long likeCount = messages.stream().filter(m->m.getType() == Constants.MESSAGE_liked).count();
        long commentCount = messages.stream().filter(m->m.getType() == Constants.MESSAGE_commented).count();

        // Insert into these 3 counts
        JSONObject object = new JSONObject();
        object.put("followerCount", followerCount);
        object.put("likeCount", likeCount);
        object.put("commentCount", commentCount);

        String response = "{\"code\":1,\"message\":\"success\",\"" + "content" + "\":" + object.toJSONString() + "}";
        return ResponseEntity.ok().body(response);
    }
}
