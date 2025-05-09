package com.salausmart.store.mappers;

import com.salausmart.store.dtos.RegisterUserRequest;
import com.salausmart.store.dtos.UpdateUserRequest;
import com.salausmart.store.dtos.UserDto;
import com.salausmart.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
