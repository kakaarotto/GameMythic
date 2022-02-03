package com.gm.service.mapper;

import com.gm.model.ChannelsCategory;
import com.gm.dto.ChannelsCategoryDTO;
import com.gm.vm.ChannelsCategoryVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ChannelsCategoryMapper {

    ChannelsCategoryMapper INSTANCE = Mappers.getMapper(ChannelsCategoryMapper.class);

    @Mapping(source = "id", target = "channelsCategoryId")
    ChannelsCategoryDTO modelToDTO(ChannelsCategory model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(ChannelsCategoryVM vm, @MappingTarget ChannelsCategory model);

}
