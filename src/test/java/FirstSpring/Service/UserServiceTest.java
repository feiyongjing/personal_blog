package FirstSpring.Service;

import FirstSpring.Dao.UserMapper;
import FirstSpring.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;
    @Test
    void testSave() {
        when(mockEncoder.encode("123456")).thenReturn("myEncodedPassword");
        userService.save("MyUser","123456");
        verify(mockMapper).save("MyUser","myEncodedPassword");
    }

    @Test
    void testGetUserByUsername() {
        userService.getUserByUsername("MyUser");
        verify(mockMapper).findUserByUsername("MyUser");
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                ()->userService.loadUserByUsername("MyUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound(){
        when(mockMapper.findUserByUsername("MyUser")).thenReturn(new User(123,"MyUser","myEncodedPassword"));
        UserDetails userDetails=userService.loadUserByUsername("MyUser");
        Assertions.assertEquals("MyUser",userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword",userDetails.getPassword());

    }
}