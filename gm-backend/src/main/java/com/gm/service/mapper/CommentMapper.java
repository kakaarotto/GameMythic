package com.gm.service.mapper;

import com.gm.model.Comment;
import com.gm.dto.CommentDTO;
import com.gm.vm.CommentVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "id", target = "commentId")
    CommentDTO modelToDTO(Comment model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(CommentVM vm, @MappingTarget Comment model);

}
