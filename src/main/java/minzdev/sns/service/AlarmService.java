package minzdev.sns.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minzdev.sns.exception.ErrorCode;
import minzdev.sns.exception.SnsApplicationException;
import minzdev.sns.model.entity.AlarmArgs;
import minzdev.sns.model.entity.AlarmEntity;
import minzdev.sns.model.entity.UserEntity;
import minzdev.sns.model.enumeration.AlarmType;
import minzdev.sns.repository.AlarmEntityRepository;
import minzdev.sns.repository.EmitterRepository;
import minzdev.sns.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    private final EmitterRepository emitterRepository;
    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    public SseEmitter connect(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("Connect Completed"));
        } catch (IOException exception) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }

    public void send(Integer receiverUserId, AlarmType alarmType, AlarmArgs alarmArgs) {
        UserEntity receiver = userEntityRepository.findById(receiverUserId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(receiver, alarmType, alarmArgs));

        emitterRepository.get(receiverUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name(ALARM_NAME).data("New Alarm"));
            } catch (IOException exception) {
                emitterRepository.delete(receiverUserId);
                throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("[SSE] Emitter Not Founded"));
    }

}
