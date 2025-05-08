package com.salausmart.store.mappers;

import com.salausmart.store.dtos.UserDto;
import com.salausmart.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
