package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.UserRoleRepository;
import pfs.lms.enquiry.service.IUserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    @Override
    public List<User> searchUsers(String[] searchParameters) {

        String firstName = null;
        String lastName = null;
        String email = null;
        String role = null;

        Integer index = 0;

        List<User> users = new ArrayList<>();
        List<User> usersContaining = new ArrayList<>();


        for (String parameter:searchParameters) {

            switch (index){
                case 0:
                    if (parameter.length() > 0)
                        firstName = parameter;
                    break;
                case 1:
                    if (parameter.length() > 0)
                        lastName = parameter;
                    break;
                case 2:
                    if (parameter.length() > 0)
                        email = parameter;
                    break;
                case 3:
                    if (parameter.length() > 0)
                        role = parameter;
                    break;
            }
            index++;
        }

        if (firstName != null && lastName == null) {
            users = userRepository.findByFirstNameStartingWith(firstName);
            usersContaining = userRepository.findByFirstNameContaining(firstName);

        }

        if (firstName == null && lastName != null) {
            users = userRepository.findByLastNameStartingWith(lastName);
            usersContaining = userRepository.findByLastNameContaining(lastName);

        }

        if (firstName != null && lastName != null) {
            users = userRepository.findByFirstNameStartingWithAndLastNameStartingWith(firstName, lastName);
            usersContaining = userRepository.findByFirstNameStartingWithAndLastNameContaining(firstName, lastName);

        }

        if (email != null && role == null)
            users = userRepository.findByEmailContains(email);

        if (email !=null && role != null)
            users = userRepository.findByEmailContainsAndRole(email,role);

        if (email == null && role !=null)
            users = userRepository.findByRole(role);

        if (firstName == null && lastName == null && email == null && role == null){
            users = userRepository.findAll();
        }

        for (User user: users) {
            user.setRoleDescription(userRoleRepository.findByCode(user.getRole()).getValue());
        }

        users.removeAll(usersContaining);
        usersContaining.removeAll(users);
        users.addAll(usersContaining);
        return users;
    }



}
