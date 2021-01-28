package com.example.demo.service.userimpl;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User newUser) {
        Optional<User> oldUser = userRepository.findById(id);
        if (oldUser.isPresent()){
            oldUser.get().setUsername(newUser.getUsername());
            oldUser.get().setPassword(newUser.getPassword());
            return userRepository.save(oldUser.get());
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}
