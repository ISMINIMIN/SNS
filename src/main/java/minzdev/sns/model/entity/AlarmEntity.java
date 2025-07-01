package minzdev.sns.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import minzdev.sns.model.enumeration.AlarmType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "\"alarm\"", indexes = {@Index(name = "user_id_idx", columnList = "user_id")})
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알림을 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private AlarmArgs args;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
        AlarmEntity alarmEntity = new AlarmEntity();
        alarmEntity.setUser(userEntity);
        alarmEntity.setAlarmType(alarmType);
        alarmEntity.setArgs(args);

        return alarmEntity;
    }

}
