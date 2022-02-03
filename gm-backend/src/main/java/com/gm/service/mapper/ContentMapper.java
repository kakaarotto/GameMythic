package com.gm.service.mapper;

import com.gm.model.Content;
import com.gm.dto.ContentDTO;
import com.gm.vm.ContentVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ContentMapper {

    ContentMapper INSTANCE = Mappers.getMapper(ContentMapper.class);

    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "category.id", target = "category.categoryId")
    @Mapping(source = "channels.id", target = "channels.channelsId")
    @Mapping(source = "user.id", target = "user.userId")
    ContentDTO modelToDTO(Content model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(ContentVM vm, @MappingTarget Content model);

}
