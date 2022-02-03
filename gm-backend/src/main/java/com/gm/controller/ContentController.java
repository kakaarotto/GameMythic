package com.gm.controller;

import com.gm.config.Constants;
import com.gm.model.*;
import com.gm.repository.CollectRepository;
import com.gm.repository.CommentRepository;
import com.gm.repository.ContentRepository;
import com.gm.service.ContentService;
import com.gm.service.MessageService;
import com.gm.service.UserService;
import com.gm.dto.ContentDTO;
import com.gm.service.mapper.CommentMapper;
import com.gm.service.mapper.ContentMapper;
import com.gm.query.ContentQuery;
import com.gm.util.ResponseUtil;
import com.gm.vm.ContentVM;
import com.gm.vm.MessageVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.moderation.v2.region.ModerationRegion;
import com.huaweicloud.sdk.moderation.v2.*;
import com.huaweicloud.sdk.moderation.v2.model.*;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ContentController extends BaseController {

    @Value("${contentModeration.ak}")
    private String moderationAk;
    @Value("${contentModeration.sk}")
    private String moderationSk;
    @Value("${contentModeration.regionId}")
    private String moderationRegionId;

    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    EntityManager entityManager;

    /**
     * Get content info by id
     * @param id
     * @return
     */
    @GetMapping("/content/{id}")
    public ResponseEntity<?> getContentInfo(@Valid @PathVariable Integer id) {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.expires();
        }
        Content old = contentRepository.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Parameter exception");
        }
        ContentDTO contentDTO = ContentMapper.INSTANCE.modelToDTO(old);
        // Return all comment data
        contentDTO.setCommentDTOS(commentRepository.findByGoodsId(id).stream().map(x -> CommentMapper.INSTANCE.modelToDTO(x)).collect(Collectors.toList()));
        // Query whether the user has become a follower
        Collect collectUser = collectRepository.findFirstByCollectUserIdAndUserId(contentDTO.getUser().getUserId(), userOptional.get().getId());
        if(collectUser != null){
            contentDTO.setCollectUser(true);
        }
        // Check whether the content has been liked
        Collect collectLiked = collectRepository.findFirstByGoodContentIdAndUserId(contentDTO.getContentId(), userOptional.get().getId());
        if(collectLiked != null){
            contentDTO.setCollectLiked(true);
        }else {
            contentDTO.setCollectLiked(false);
        }
        // Check whether the content has been collected
        Collect collectCollected = collectRepository.findFirstByContentIdAndUserId(contentDTO.getContentId(), userOptional.get().getId());
        if(collectCollected != null){
            contentDTO.setCollectCollected(true);
        } else {
            contentDTO.setCollectCollected(false);
        }

        return ResponseUtil.renderSuccess("Get success", contentDTO);
    }

    /**
     * Get all contents
     * @param pageable
     * @param query
     * @return
     */
    @GetMapping("/contents")
    public ResponseEntity<?> getContents(@PageableDefault(sort = {"addTime"}, direction = Sort.Direction.DESC) Pageable pageable, ContentQuery query) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Query the content posted by the followed user, if it is true, query the content of the followed user
        if(query.getCollect()){
            List<Collect> collects = collectRepository.findByUserId(user.get().getId());
            query.setUserIds(collects.stream().map(x -> x.getCollectUserId()).collect(Collectors.toList()));
        }
        // Get all content data
        Page<ContentDTO> page = contentService.search(query, pageable).map(object -> ContentMapper.INSTANCE.modelToDTO(object));
        return ResponseUtil.renderSuccess(page);
    }

    @PostMapping("/contents-nextPage")
    public ResponseEntity<?> nextPage(@RequestBody ContentQuery queryParams) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        //Assemble paging query SQL statement
        String where = " WHERE 1=1";
        if (queryParams.getCategoryId() != null) {
            where += " AND category_id=" + queryParams.getCategoryId();
        }
        if(queryParams.getCollect()){
            List<Collect> collects = collectRepository.findByUserIdAndCollectUserIdIsNotNull(user.get().getId());
            if (collects != null && collects.size() > 0) {
                where += " AND user_id in(" + collects.stream().map(m->m.getCollectUserId().toString()).collect(Collectors.joining(",")) + ")";
            }
        }
        if (queryParams.getExcludeIds() != null && queryParams.getExcludeIds().size() > 0) {
            where += " AND id not in(" + queryParams.getExcludeIds().stream().map(Object::toString).collect(Collectors.joining(",")) + ")";
        }

        // 8 per page
        int size = 8;
        if(queryParams.getPageSize() != null) {
            size = queryParams.getPageSize();
        }

        // Get random list
        String sql = "SELECT * FROM content "+ where + " ORDER BY RAND() LIMIT " + size;

        log.info("sql = {}", sql);
        Query query = entityManager.createNativeQuery(sql, Content.class);
        List<Content> contents = query.getResultList();
        List<ContentDTO> results = new ArrayList<>();
        for (Content count : contents) {
            results.add(ContentMapper.INSTANCE.modelToDTO(count));
        }

        return ResponseUtil.renderSuccess(results);
    }

    /**
     * Creat content and content moderation
     * @param vm
     * @return
     */
    @PostMapping(path = "/content")
    public ResponseEntity<?> create(@RequestBody ContentVM vm) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        log.info("ContentVM = {}", vm);

        // invoke Huawei Cloud SDK for content moderation
        if(StringUtils.isNotEmpty(moderationAk)
                && StringUtils.isNotEmpty(moderationSk)
                && StringUtils.isNotEmpty(moderationRegionId)) {

            // Set authentication information
            ICredential auth = new BasicCredentials()
                    .withAk(moderationAk)
                    .withSk(moderationSk);

            // invoke Region
            ModerationClient client = ModerationClient.newBuilder()
                    .withCredential(auth)
                    .withRegion(ModerationRegion.valueOf(moderationRegionId))
                    .build();

            // Mount content moderation object
            RunTextModerationRequest request = new RunTextModerationRequest();
            TextDetectionReq body = new TextDetectionReq();
            List<TextDetectionItemsReq> listbodyItems = new ArrayList<>();

            // Add title for text moderation object
            listbodyItems.add(
                    new TextDetectionItemsReq()
                            .withText(vm.getTitle())
                            .withType("content")
            );

            // Add main content for content moderation object
            listbodyItems.add(
                    new TextDetectionItemsReq()
                            .withText(vm.getContent())
                            .withType("content")
            );

            List<String> listbodyCategories = new ArrayList<>();
            // Text moderation Categories
            listbodyCategories.add("politics");
            listbodyCategories.add("porn");
            listbodyCategories.add("ad");
            listbodyCategories.add("abuse");
            listbodyCategories.add("contraband");
            listbodyCategories.add("flood");

            body.withItems(listbodyItems);
            body.withCategories(listbodyCategories);
            request.withBody(body);

            // Mount image moderation
            RunImageBatchModerationRequest imageBatchModerationRequest = new RunImageBatchModerationRequest();
            ImageBatchModerationReq imageBatchModerationReq = new ImageBatchModerationReq();

            List<ImageBatchModerationReq.CategoriesEnum> categoriesEnums = new ArrayList<>();
            // Image moderation categories
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.POLITICS);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.TERRORISM);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.PORN);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.AD);

            imageBatchModerationReq.setCategories(categoriesEnums);
            for (String img : vm.getImageList()) {
                imageBatchModerationReq.addUrlsItem(img);
            }
            imageBatchModerationRequest.withBody(imageBatchModerationReq);

            try {
                RunTextModerationResponse response = client.runTextModeration(request);
                log.info("RunTextModerationResponse = {}", response.toString());
                // Start text moderation
                if(response.getResult().getSuggestion().equals("pass")) {
                    // Start image moderation
                    RunImageBatchModerationResponse runImageBatchModerationResponse = client.runImageBatchModeration(imageBatchModerationRequest);
                    log.info("RunImageBatchModerationResponse = {}", runImageBatchModerationResponse.toString());
                    if(runImageBatchModerationResponse == null ||
                            runImageBatchModerationResponse.getResult() == null ||
                            runImageBatchModerationResponse.getResult().stream().allMatch(m -> m.getSuggestion() == null) ||
                            runImageBatchModerationResponse.getResult().stream().allMatch(m -> m.getSuggestion() != null && m.getSuggestion().equals("pass"))) {
                        Content activity = contentService.create(vm, user.get());
                        log.info("RunTextModerationResponse = Created successfully");
                        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(activity));
                    }
                    else {
                        log.info("RunTextModerationResponse = Failed to publish, image contains illegal information");

                        // Create content review notification (image content) failed to review notification
                        MessageVM message = new MessageVM();
                        message.setType(Constants.MESSAGE_content);
                        message.setFromUserId(0);
                        message.setFromTypeId(0);
                        message.setTitle("Content review failed");
                        message.setContent("Failed to publish, content photos contains illegal information");
                        message.setReadState(0);
                        message.setAddTime(LocalDateTime.now());

                        messageService.create(message, user.get());

                        return ResponseUtil.renderError(401, "Failed to publish, image contains illegal information", message);
                    }
                }
                else {
                    log.info("RunTextModerationResponse = Failed to publish, Text contains illegal information");

                    // Create content review notification (text content) failed to review notification
                    MessageVM message = new MessageVM();
                    message.setType(Constants.MESSAGE_content);
                    message.setFromUserId(0);
                    message.setFromTypeId(0);
                    message.setTitle("Content review failed");
                    message.setContent("Failed to publish, content contains illegal information");
                    message.setReadState(0);
                    message.setAddTime(LocalDateTime.now());

                    messageService.create(message, user.get());

                    return ResponseUtil.renderError(401, "Failed to publish, content contains illegal information", message);
                }
            } catch (ConnectionException | RequestTimeoutException e) {
                e.printStackTrace();
            } catch (ServiceResponseException e) {
                e.printStackTrace();
                System.out.println(e.getHttpStatusCode());
                System.out.println(e.getErrorCode());
                System.out.println(e.getErrorMsg());
            }
            return ResponseUtil.renderError(402, "Failed to create, content review error");
        }
        else {
            log.info("{},{},{}", moderationAk, moderationSk, moderationRegionId);
        }

        Content activity = contentService.create(vm, user.get());
        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(activity));
    }

    /**
     * Edit content
     * @param id
     * @param vm
     * @return
     */
    @PutMapping(path = "/content/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Integer id, @RequestBody ContentVM vm) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }

        if (id == null) {
            return ResponseUtil.renderError("Missing parameters:id");
        }
        Content old = contentRepository.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Illegal parameter:id");
        }
        log.info("ContentVM = {}", vm);
        // invoke Huawei Cloud SDK for content moderation
        if(StringUtils.isNotEmpty(moderationAk)
                && StringUtils.isNotEmpty(moderationSk)
                && StringUtils.isNotEmpty(moderationRegionId)) {
            // Set authentication information
            ICredential auth = new BasicCredentials()
                    .withAk(moderationAk)
                    .withSk(moderationSk);

            ModerationClient client = ModerationClient.newBuilder()
                    .withCredential(auth)
                    .withRegion(ModerationRegion.valueOf(moderationRegionId))
                    .build();

            RunTextModerationRequest request = new RunTextModerationRequest();

            TextDetectionReq body = new TextDetectionReq();
            List<TextDetectionItemsReq> listbodyItems = new ArrayList<>();

            // title + content
            listbodyItems.add(
                    new TextDetectionItemsReq()
                            .withText(vm.getTitle())
                            .withType("content")
            );

            listbodyItems.add(
                    new TextDetectionItemsReq()
                            .withText(vm.getContent())
                            .withType("content")
            );

            List<String> listbodyCategories = new ArrayList<>();
            listbodyCategories.add("politics");
            listbodyCategories.add("porn");
            listbodyCategories.add("ad");
            listbodyCategories.add("abuse");
            listbodyCategories.add("contraband");
            listbodyCategories.add("flood");

            body.withItems(listbodyItems);
            body.withCategories(listbodyCategories);
            request.withBody(body);


            // Mount image moderation
            RunImageBatchModerationRequest imageBatchModerationRequest = new RunImageBatchModerationRequest();
            ImageBatchModerationReq imageBatchModerationReq = new ImageBatchModerationReq();

            List<ImageBatchModerationReq.CategoriesEnum> categoriesEnums = new ArrayList<>();
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.POLITICS);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.TERRORISM);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.PORN);
            categoriesEnums.add(ImageBatchModerationReq.CategoriesEnum.AD);


            imageBatchModerationReq.setCategories(categoriesEnums);
            for (String img : vm.getImageList()) {
                imageBatchModerationReq.addUrlsItem(img);
            }
            imageBatchModerationRequest.withBody(imageBatchModerationReq);

            try {
                RunTextModerationResponse response = client.runTextModeration(request);
                log.info("RunTextModerationResponse = {}", response.toString());

                if(response.getResult().getSuggestion().equals("pass")) {
                    // Run image moderation
                    RunImageBatchModerationResponse runImageBatchModerationResponse = client.runImageBatchModeration(imageBatchModerationRequest);
                    log.info("RunImageBatchModerationResponse = {}", runImageBatchModerationResponse.toString());
                    if(runImageBatchModerationResponse == null ||
                            runImageBatchModerationResponse.getResult() == null ||
                            runImageBatchModerationResponse.getResult().stream().allMatch(m -> m.getSuggestion() == null) ||
                            runImageBatchModerationResponse.getResult().stream().allMatch(m -> m.getSuggestion() != null && m.getSuggestion().equals("pass"))) {

                        old = contentService.update(old, vm);
                        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(old));
                    }
                    else {
                        log.info("RunTextModerationResponse = Failed to publish, image contains illegal information");

                        // Creat message
                        MessageVM message = new MessageVM();
                        message.setType(Constants.MESSAGE_content);
                        message.setFromUserId(0);
                        message.setFromTypeId(0);
                        message.setTitle("Content review failed");
                        message.setContent("The content photos contains sensitive information");
                        message.setReadState(0);
                        message.setAddTime(LocalDateTime.now());

                        messageService.create(message, user.get());


                        return ResponseUtil.renderError(401, "Failed to publish, image contains illegal information", message);
                    }
                }
                else {
                    log.info("RunTextModerationResponse = Failed to publish, image contains illegal information");

                    // Creat message
                    MessageVM message = new MessageVM();
                    message.setType(Constants.MESSAGE_content);
                    message.setFromUserId(0);
                    message.setFromTypeId(0);
                    message.setTitle("Content review failed");
                    message.setContent("The content contains sensitive information");
                    message.setReadState(0);
                    message.setAddTime(LocalDateTime.now());

                    messageService.create(message, user.get());

                    return ResponseUtil.renderError(401, "Failed to publish, image contains illegal information", message);
                }
            } catch (ConnectionException | RequestTimeoutException e) {
                e.printStackTrace();
            } catch (ServiceResponseException e) {
                e.printStackTrace();
                System.out.println(e.getHttpStatusCode());
                System.out.println(e.getErrorCode());
                System.out.println(e.getErrorMsg());
            }
            return ResponseUtil.renderError(402, "Content moderation error");
        }
        else {
            log.info("{},{},{}", moderationAk, moderationSk, moderationRegionId);
        }

        old = contentService.update(old, vm);
        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(old));
    }

    /**
     * Delete cotnet
     * @param id
     * @return
     */
    @DeleteMapping(path = "/content/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Content old = contentRepository.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Illegal parameter:id");
        }
        contentService.delete(old);
        return ResponseUtil.renderSuccess("successfully deleted");
    }

    /**
     * Like content
     * @param id
     * @return
     */
    @PostMapping(path = "/content/{id}/good")
    public ResponseEntity<?> good(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        // Get likes based on content id
        Content content = contentRepository.getById(id);
        if (content == null) {
            return ResponseUtil.renderError("Missing parameter:id");
        }
        Content collect = contentService.goodContent(content, user.get());
        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(collect));
    }

    /**
     * Collect content
     * @param id
     * @return
     */
    @PostMapping(path = "/content/{id}/collect")
    public ResponseEntity<?> collect(@Valid @PathVariable Integer id) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Content old = contentRepository.getById(id);
        if (old == null) {
            return ResponseUtil.renderError("Missing parameter:id");
        }
        Content collect = contentService.collect(old, user.get());

        return ResponseUtil.renderSuccess("Created successfully", ContentMapper.INSTANCE.modelToDTO(collect));
    }

    /**
     * Get a list of collect
     * @return
     */
    @GetMapping("/collect-contents")
    public ResponseEntity<?> getCollectContents() {
        Optional<User> userOptional = userService.getCurrentUser();
        if (!userOptional.isPresent()) {
            return ResponseUtil.expires();
        }
        // Get the content list of all collect
        List<Collect> collects = collectRepository.findByUserIdAndContentIdIsNotNull(userOptional.get().getId());
        // Query the content that have been collected
        List<ContentDTO> collect = contentRepository.findByIdIn(collects.stream().map(x -> x.getContentId()).collect(Collectors.toList())).stream().map(x -> ContentMapper.INSTANCE.modelToDTO(x)).collect(Collectors.toList());
        return ResponseUtil.renderSuccess(collect);
    }


    /**
     * Search for content
     * @param pageable
     * @param keyword
     * @return search result
     */
    @GetMapping("/content/searchByKeyword")
    public ResponseEntity<?> searchByKeyword(@PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable, String keyword) {
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            return ResponseUtil.renderError(-9, "Not login");
        }
        Page<ContentDTO> page = contentService.searchByKeyword(keyword, pageable).map(ContentMapper.INSTANCE::modelToDTO);
        return ResponseUtil.renderSuccess(page);
    }
}
