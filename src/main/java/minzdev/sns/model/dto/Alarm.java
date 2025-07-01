package minzdev.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import minzdev.sns.model.entity.AlarmArgs;
import minzdev.sns.model.entity.AlarmEntity;
import minzdev.sns.model.enumeration.AlarmType;

import java.sql.Timestamp;

@Slf4j
@Getter
@AllArgsConstructor
public class Alarm {

    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp createdAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        // log.info("----- [Alarm] Call fromEntity -----");

        return new Alarm(
                entity.getId(),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getCreatedAt(),
                entity.getDeletedAt()
        );
    }

}
