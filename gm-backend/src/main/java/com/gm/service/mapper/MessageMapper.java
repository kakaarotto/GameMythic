package com.gm.service.mapper;

import com.gm.dto.MessageDTO;
import com.gm.model.Message;
import com.gm.vm.MessageVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "id", target = "messageId")
    @Mapping(source = "user.id", target = "user.userId")
    MessageDTO modelToDTO(Message model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(MessageVM vm, @MappingTarget Message model);
}
