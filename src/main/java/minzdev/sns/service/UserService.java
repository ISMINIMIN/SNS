package minzdev.sns.service;

import lombok.AllArgsConstructor;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.dto.Alarm;
import minzdev.sns.model.dto.User;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.AlarmEntityRepository;
import minzdev.sns.repository.UserCacheRepository;
import minzdev.sns.repository.UserEntityRepository;
import minzdev.sns.util.JwtTokenUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserCacheRepository userCacheRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    public User join(String username, String password) {
        // username 중복 체크
        userEntityRepository.findByUsername(username).ifPresent(user -> {
            throw new SnsApplicationException(
                    ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String username, String password) {
        User user = loadUserByUsername(username);
        userCacheRepository.setUser(user);

        // 비밀번호 일치 여부 체크
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenUtils.generateAccessToken(username);
    }

    @Transactional(readOnly = true)
    public Page<Alarm> getAllAlarm(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
    }

    public User loadUserByUsername(String username) {
        return userCacheRepository.getUser(username).orElseGet(() ->
                userEntityRepository.findByUsername(username).map(User::fromEntity).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))));
    }

    public UserEntity findByUsername(String username) {
        return userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
    }

}
