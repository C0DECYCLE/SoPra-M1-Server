package ch.uzh.ifi.hase.soprafs24.service;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;

@Service
@Transactional
public class UserStatusService {

    private static final long Rate = 5 * 1000;

    //private final Logger log = LoggerFactory.getLogger(UserStatusService.class);

    private final UserService userService;

    @Autowired
    public UserStatusService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedDelay = UserStatusService.Rate)
    public void scheduleWatcher() {
        userService.getUsers().forEach((User user) -> watchUserStatus(user));
    }

    private void watchUserStatus(User user) {
        if (user.getStatus() == UserStatus.OFFLINE) {
            return;
        }
        long now = System.currentTimeMillis();
        long delta = now - user.getLastStatus();
        if (delta > UserStatusService.Rate) {
            user.setStatus(UserStatus.OFFLINE);
            user.setLastStatus(now);
        }
    }
}
