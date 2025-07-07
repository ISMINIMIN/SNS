package minzdev.sns.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minzdev.sns.model.event.AlarmEvent;
import minzdev.sns.service.AlarmService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

    private final AlarmService alarmService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void consumeAlarm(AlarmEvent event, Acknowledgment ack) {
        log.info("[Consumer] Consume the event {}", event);
        alarmService.send(event.getReceiverUserId(), event.getAlarmType(), event.getAlarmArgs());
        ack.acknowledge();
    }

}
