package minzdev.sns.service;

import lombok.AllArgsConstructor;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.dto.User;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.repository.UserEntityRepository;
import minzdev.sns.util.JwtTokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    public User join(String username, String password) {
        // username 중복 체크
        userEntityRepository.findByUsername(username).ifPresent(user -> {
            throw new SnsApplicationException(
                    ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
        });

        // 회원가입 진행 (user 등록)
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String username, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))
        );

        // 비밀번호 일치 여부 체크
        if(!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenUtils.generateAccessToken(username);
    }

}
