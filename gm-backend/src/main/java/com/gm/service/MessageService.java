package com.gm.service;

import com.gm.model.Message;
import com.gm.model.User;
import com.gm.query.MessageQuery;
import com.gm.vm.MessageVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    /**
     * Find messages
     * @param query
     * @param pageable
     * @return
     */
    Page<Message> search(final MessageQuery query, Pageable pageable);

    /**
     * Find message detail
     * @param messageId
     * @return
     */
    Message getById(Integer messageId);

    /**
     * create message
     * @param messageVM
     * @param user
     * @return
     */
    Message create(MessageVM messageVM, User user);

    /**
     * delete message
     * @param messageId
     * @return
     */
    void delete(Integer messageId);

    /**
     * update message read state
     * @param messageId
     * @param readState
     * @return
     */
    Message updateReadState(Integer messageId, Integer readState);

}
