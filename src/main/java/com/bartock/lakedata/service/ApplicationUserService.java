package com.bartock.lakedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.bartock.lakedata.data.ApplicationUser;
import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.repository.ApplicationUserRepository;
import com.bartock.lakedata.security.RandomAESKeyGenerator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ApplicationUserService {
    @Autowired
    private ApplicationUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ApplicationUserDto> getAllApplicationUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ApplicationUserDto getApplicationUser(int id) {
        Optional<ApplicationUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return convertToDto(user.get());
        }
        return null;
    }

    public ApplicationUserDto getApplicationUserByApiKey(String apiKey) {
        Optional<ApplicationUser> user = userRepository.findOneByApiKey(apiKey);
        if (user.isPresent()) {
            return convertToDto(user.get());
        }
        return null;
    }

    public ApplicationUserDto saveApplicationUser(ApplicationUserDto user) {
        if (user.getApiKey() == null) {
            user.setApiKey(RandomAESKeyGenerator.generate());
        }
        ApplicationUser savedApplicationUser = userRepository.save(convertToEntity(user));

        return convertToDto(savedApplicationUser);
    }

    public void deleteApplicationUser(ApplicationUserDto user) {
        deleteApplicationUser(user.getId());
    }

    public void deleteApplicationUser(int id) {
        userRepository.deleteById(id);
    }

    public boolean existsApplicationUserByApiKey(String apiKey) {
        return userRepository.existsByApiKey(apiKey);
    }

    public boolean existsApplicationUserById(Integer id) {
        return userRepository.existsById(id);
    }

    public ApplicationUserDto convertToDto(ApplicationUser user) {
        return modelMapper.map(user, ApplicationUserDto.class);
    }

    public ApplicationUser convertToEntity(ApplicationUserDto user) {
        return modelMapper.map(user, ApplicationUser.class);
    }
}
