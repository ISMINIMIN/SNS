package minzdev.sns.util;

import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.dto.User;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    public static User getUser(Authentication auth) {
        return ClassUtils.getSafeCastInstance(auth.getPrincipal(), User.class).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "[ERROR] Casting to User class failed"));
    }
}
