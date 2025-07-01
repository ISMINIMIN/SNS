package minzdev.sns.util;

import java.util.Optional;

public class ClassUtils {

    public static <T> Optional<T> getSafeCastInstance(Object object, Class<T> clazz) {
        return clazz != null && clazz.isInstance(object) ? Optional.of(clazz.cast(object)) : Optional.empty();
    }

}
