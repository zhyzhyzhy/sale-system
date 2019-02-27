package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.common.UserRoleEnum;
import cc.lovezhy.netease.sale.entity.User;
import cc.lovezhy.netease.sale.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void insertUser(String name, String password, UserRoleEnum userRole) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setRole(userRole.getCode());

        User user = userRepository.findByName(name);
        if (Objects.nonNull(user)) {
            newUser.setId(user.getId());
            BeanUtils.copyProperties(newUser, user);
        } else {
            userRepository.save(user);
        }
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
