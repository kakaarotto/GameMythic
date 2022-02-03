package com.gm.controller;

import com.gm.dto.ChannelsCountDTO;
import com.gm.dto.ContentDTO;
import com.gm.model.*;
import com.gm.repository.*;
import com.gm.service.ChannelsService;
import com.gm.dto.ChannelsDTO;
import com.gm.service.ContentService;
import com.gm.service.UserService;
import com.gm.service.mapper.ChannelsCountMapper;
import com.gm.service.mapper.ChannelsMapper;
import com.gm.query.ChannelsQuery;
import com.gm.service.mapper.ContentMapper;
import com.gm.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pujie
 */

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ChannelsController extends BaseController {

    private final ChannelsService channelsService;
    private final ChannelsCategoryRepository channelsCategoryRepository;
    private final UserService userService;
    private final ChannelsRepository channelsRepository;
    private final ChannelsSubscriptionRepository channelsSubscriptionRepository;
    private final ChannelsHistoryRepository channelsHistoryRepository;
    private final ContentService contentService;
    private final ContentRepository contentRepository;
    EntityManager entityManager;


    /**
     * Get all channels
     * @param pageable
     * @param query
     * @return all channels
     */
    @GetMapping("/channels")
    public ResponseEntity<?> getChannels(@PageableDefault(sort = {"addTime"}, direction = Sort.Direction.DESC) Pageable pageable, ChannelsQuery query) {
        // Get all channels
        Page<ChannelsDTO> page = channelsService.search(query, pageable).map(ChannelsMapper.INSTANCE::modelToDTO);
        Optional<User> user = userService.getCurrentUser();
        if (user.isPresent()) {
            // Get all channels by category and user subscribed channels
            List<Integer> cidList = page.getContent().stream().map(ChannelsDTO::getChannelsId).collect(Collectors.toList());
            List<ChannelsSubscription> csList = channelsSubscriptionRepository.findAllByUserIdAndChannelsIdIn(user.get().getId(), cidList);
            log.info("### cidList = {}, csList = {}", cidList, csList);
            for (ChannelsSubscription dto: csList) {
                page.getContent().stream().filter(m->m.getChannelsId().equals(dto.getChannelsId()))
                    .findFirst()
                    .ifPresent(channelsDTO -> channelsDTO.setSubscription(true));
            }
        }
        return ResponseUtil.renderSuccess(page);
    }

    /**
     * Get channel details info
     * @param id
     * @return channels
     */
    @GetMapping("/channels/{id}")
    public ResponseEntity<?> getInfo(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Channels channels = entityManager.find(Channels.class, id);
        if (channels == null || channels.getId() == null) {
            return ResponseUtil.renderError("Parameter exception");
        }
        ChannelsDTO channelsDTO = ChannelsMapper.INSTANCE.modelToDTO(channels);

        ChannelsSubscriptionKey key = new ChannelsSubscriptionKey();
        key.setUserId(user.get().getId());
        key.setChannelsId(id);
        ChannelsSubscription channelsSubscription = entityManager.find(ChannelsSubscription.class, key);
        log.info("##########{}", channelsSubscription);
        if(channelsSubscription != null && channelsSubscription.getChannelsId() > 0 && channelsSubscription.getUserId() > 0) {
            channelsDTO.setSubscription(true);
        }
        return ResponseUtil.renderSuccess("Get success", channelsDTO);
    }

    /**
     * GET channels content
     * @param size get 2
     * @return number of subscribed, number of likes on the channel
     */
    @GetMapping("/channels-contents")
    public ResponseEntity<?> getChannelsContents(@Valid @RequestParam Integer size) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Get the channels random content, and content likes, number of channel subscriptions
        String channelsSql = "SELECT c.*, " +
                "(SELECT count(*) FROM channels_subscription s WHERE s.channels_id=c.id) AS subscription_count, " +
                "(SELECT count(*) FROM channels_history h where h.channels_id=c.id) AS view_count, " +
                "IFNULL((SELECT 1 FROM channels_subscription s WHERE s.channels_id=c.id AND s.user_id=" + user.get().getId() + " LIMIT 1), 0) AS subscription " +
                "FROM channels c ORDER BY RAND() LIMIT " + size;
        Query query = entityManager.createNativeQuery(channelsSql, ChannelsCount.class);
        List<ChannelsCount> channels = query.getResultList();
        List<ChannelsCountDTO> results = new ArrayList<>();
        for (ChannelsCount count : channels) {
            results.add(ChannelsCountMapper.INSTANCE.modelToDTO(count));
        }
        // Get the 2 random contents by the channel
        for (ChannelsCountDTO dto: results) {
            List<Content> contents = contentRepository.getRandChannelContents(2, dto.getChannelsId());
            List<ContentDTO> list = new ArrayList<>();
            // Traversing the contents
            for (Content con : contents) {
                list.add(ContentMapper.INSTANCE.modelToDTO(con));
            }
            dto.setContents(list);
        }
        log.info("##########{}", results);
        return ResponseUtil.renderSuccess(results);
    }

    /**
     * Get channels by category ID
     * @param channelsCategoryId
     * @return all channels by cid
     */
    @GetMapping("/channels-by-channels-category/{channelsCategoryId}")
    public ResponseEntity<?> getDatasByChannelsCategory(@Valid @PathVariable Integer channelsCategoryId) {
        ChannelsCategory byId = channelsCategoryRepository.getById(channelsCategoryId);
        if(byId == null){
            return ResponseUtil.renderError("Parameter exception");
        }
        return ResponseUtil.renderSuccess(byId.getChannels());
    }

    /**
     * Get all channels subscribed by user
     * @param pageable
     * @return subscribed channels
     */
    @GetMapping("/channels-subscription")
    public ResponseEntity<?> getChannelsSubscription(@PageableDefault(sort = {"update_time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Get user subscription
        Page<ChannelsDTO> page = channelsService.getSubscriptionChannels(user.get().getId(), pageable).map(ChannelsMapper.INSTANCE::modelToDTO);
        return ResponseUtil.renderSuccess(page);
    }

    /**
     * Add channel subscription by id.
     * @param id
     * @return subscription results
     */
    @PostMapping(path = "/channels-subscription/{id}")
    public ResponseEntity<?> AddChannelsSubscription(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        ChannelsSubscription addObj = new ChannelsSubscription();
        addObj.setUserId(user.get().getId());
        addObj.setChannelsId(id);
        addObj.setAddTime(LocalDateTime.now());
        channelsSubscriptionRepository.save(addObj);
        return ResponseUtil.renderSuccess("Subscription success");
    }

    /**
     * Delete channel subscription by id.
     * @param id
     * @return remove subscription results
     */
    @DeleteMapping(path = "/channels-subscription/{id}")
    public ResponseEntity<?> deleteChannelsSubscription(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        ChannelsSubscriptionKey idKey = new ChannelsSubscriptionKey();
        // Delete user subscription channels based on user id and channel id
        idKey.setUserId(user.get().getId());
        idKey.setChannelsId(id);
        channelsSubscriptionRepository.deleteById(idKey);
        return ResponseUtil.renderSuccess("Remove subscription success");
    }


    /**
     * Get list of channel history
     * @param pageable
     * @return user channel history
     */
    @GetMapping("/channels-history")
    public ResponseEntity<?> getChannelsHistory(@PageableDefault(sort = {"update_time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Paging for current user history channels
        Page<ChannelsDTO> page = channelsService.getHistoryChannels(user.get().getId(), pageable).map(ChannelsMapper.INSTANCE::modelToDTO);
        return ResponseUtil.renderSuccess(page);
    }

    /**
     * Add channel browsing history
     * @param id
     * @return user browsing history
     */
    @PostMapping(path = "/channels-history/{id}")
    public ResponseEntity<?> AddChannelsHistory(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        ChannelsHistory addObj = new ChannelsHistory();
        addObj.setUserId(user.get().getId());
        addObj.setChannelsId(id);
        addObj.setAddTime(LocalDateTime.now());
        channelsHistoryRepository.save(addObj);
        return ResponseUtil.renderSuccess("Add history success");
    }

    /**
     * Delete user browsing history channel
     * @param id
     * @return
     */
    @DeleteMapping(path = "/channels-history/{id}")
    public ResponseEntity<?> deleteChannelsHistory(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "not login");
        }
        log.info("-----delete-------");
        ChannelsHistoryKey idKey = new ChannelsHistoryKey();
        idKey.setUserId(user.get().getId());
        idKey.setChannelsId(id);
        channelsHistoryRepository.deleteById(idKey);
        return ResponseUtil.renderSuccess("Remove history success");
    }
}
