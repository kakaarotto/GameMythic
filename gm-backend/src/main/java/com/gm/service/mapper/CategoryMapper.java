package com.gm.service.mapper;

import com.gm.model.Category;
import com.gm.dto.CategoryDTO;
import com.gm.vm.CategoryVM;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "id", target = "categoryId")
    CategoryDTO modelToDTO(Category model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void vmToModel(CategoryVM vm, @MappingTarget Category model);

}
