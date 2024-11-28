package martproject.MartProject.service;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.message.Message;
import martproject.MartProject.domain.message.MessageDto;
import martproject.MartProject.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void sendMessage(MessageDto.RequestMessageDto Dto) {
        Message message = Dto.toEntity();
        messageRepository.save(message);
        log.info("메시지 전송 완료");
    }

    public void MessageCheck(MessageDto.RequestMessageDto Dto) {
        Message message = Dto.toEntity();
        messageRepository.save(message);
        log.info("메시지 확인");
    }
}
