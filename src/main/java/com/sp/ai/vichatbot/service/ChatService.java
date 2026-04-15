package com.sp.ai.vichatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder,
                       @Value("classpath:data.txt") Resource dataResource) throws IOException {

        String companyData = dataResource.getContentAsString(StandardCharsets.UTF_8);

        String systemPrompt = """
                คุณคือผู้ช่วย AI ของบริษัท วิไอแชทบอท จำกัด (ViChatbot Co., Ltd.)
                
                กฎที่ต้องปฏิบัติตามอย่างเคร่งครัด:
                1. ตอบคำถามโดยอ้างอิงจากข้อมูลบริษัทด้านล่างเท่านั้น
                2. ห้ามเปลี่ยนแปลง แต่งเติม หรือสรุปข้อมูลเองโดยเด็ดขาด
                3. หากมีสรุปข้อมูลที่เกี่ยวข้อง ให้คัดลอกข้อความนั้นมาตอบตรงๆ
                4. ตอบเป็นภาษาไทยเท่านั้น
                5. หากไม่มีข้อมูลในส่วนนั้น ให้ตอบว่า 'ขออภัย ฉันไม่มีข้อมูลในส่วนนั้น'
                
                ข้อมูลบริษัท (ใช้ข้อมูลนี้เท่านั้น ห้ามใช้ความรู้อื่น):
                ---------------------
                """ + companyData + """
                ---------------------
                """;

        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt)
                .build();
    }

    public String chat(String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
