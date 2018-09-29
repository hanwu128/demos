package com.hw.data.service;

import com.hw.data.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
}
