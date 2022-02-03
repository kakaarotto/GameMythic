package com.gm.service.mapper;

import com.gm.model.Channels;
import com.gm.dto.ChannelsDTO;
import com.gm.vm.ChannelsVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ChannelsMapper {

    ChannelsMapper INSTANCE = Mappers.getMapper(ChannelsMapper.class);

    @Mapping(source = "id", target = "channelsId")
    ChannelsDTO modelToDTO(Channels model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(ChannelsVM vm, @MappingTarget Channels model);

}
