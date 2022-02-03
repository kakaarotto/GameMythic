package com.gm.service.mapper;

import com.gm.dto.ChannelsCountDTO;
import com.gm.model.ChannelsCount;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ChannelsCountMapper  {
    ChannelsCountMapper INSTANCE = Mappers.getMapper(ChannelsCountMapper.class);

    @Mapping(source = "id", target = "channelsId")
    ChannelsCountDTO modelToDTO(ChannelsCount model);
}