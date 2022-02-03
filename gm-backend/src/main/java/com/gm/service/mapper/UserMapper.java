package com.gm.service.mapper;

import com.alibaba.fastjson.JSONArray;
import com.gm.model.User;
import com.gm.vm.UserVM;
import com.gm.dto.UserDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "userId")
    UserDTO modelToDTO(User model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(UserVM vm, @MappingTarget User model);

    @Named("formatJSON")
    default JSONArray formatJSON(Object data) {
        return data == null ? null : JSONArray.parseArray(data.toString());
    }

    @Named("formatString")
    default String formatString(Object data) {
        return data == null ? null : JSONArray.toJSONString(data);
    }
}
