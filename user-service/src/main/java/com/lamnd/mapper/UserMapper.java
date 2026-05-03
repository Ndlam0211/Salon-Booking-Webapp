package com.lamnd.mapper;

import com.lamnd.annotation.BaseMapperAnnotation;
import com.lamnd.common.BaseMapper;
import com.lamnd.dto.request.UserCreateRequest;
import com.lamnd.dto.request.UserUpdateRequest;
import com.lamnd.dto.response.UserResponse;
import com.lamnd.entity.User;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper extends BaseMapper<User, UserResponse, UserCreateRequest, UserUpdateRequest> {

    @Override
//    @BaseMapperAnnotation
//    @Mapping(target = "role", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(UserCreateRequest createRequest);

    @Override
//    @BaseMapperAnnotation
//    @Mapping(target = "role", ignore = true)
//    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UserUpdateRequest updateRequest, @MappingTarget User entity);
}
