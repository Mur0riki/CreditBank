package neoflex.dossier.service.serviceimpl;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.dossier.service.DossierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {

    private JavaMailSender javaMailSender;
    private List<String> list = new ArrayList<>();
    private int docCount;
    private int sesCodeCount;
    private List<String> sesCodeList = new ArrayList<>();


    @Value("{$sender.email}")
    private String senderEmail;

    public void setMailSender(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @Override
    public void sendSes(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Ses code");
        message.setText(sesCodeList.get(sesCodeCount - 1));
        javaMailSender.send(message);
        log.info("message send");
    }


    @Override
    public void sendMessage(String receiver, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Оформление кредита");
        message.setText(text);
        javaMailSender.send(message);
        log.info("message send");
    }

    @Override
    public void sendDocument(String receiver) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setFrom(new InternetAddress(senderEmail, "senderText"));

            message.setSubject("Оформление кредита");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart fileBodyPart = new MimeBodyPart();

            DataSource fileDataSource = new FileDataSource("dossier/src/main/resources/documents/" + list.get(docCount - 1));
            fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
            fileBodyPart.setFileName(list.get(docCount - 1) + ".txt");
            multipart.addBodyPart(fileBodyPart);

            message.setContent(multipart);
            javaMailSender.send(message);
            log.info("message send");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(topics = {"finish-registration"
            , "create-documents"
            , "send-documents"
            , "credit-issued"
            , "statement-denied"}
            , groupId = "deal")
    private void listener(String data) {
        String theme = null;
        if (data.contains("FINISH_REGISTRATION")) {
            theme = "FINISH_REGISTRATION";
            docCount++;
        }
        if (data.contains("CREATE_DOCUMENTS")) {
            theme = "CREATE_DOCUMENTS";
            docCount++;
        }
        if (data.contains("SEND_DOCUMENTS")) {
            theme = "SEND_DOCUMENTS";
            docCount++;
        }
        if (data.contains("SEND_SES")) {
            theme = "SEND_SES";
            docCount++;
        }
        if (data.contains("CREDIT_ISSUED")) {
            theme = "CREDIT_ISSUED";
            docCount++;
        }
        if (data.contains("STATEMENT_DENIED")) {
            theme = "STATEMENT_DENIED";
            docCount++;
        }
        System.out.println(theme);
        list.add(theme);
    }


    @KafkaListener(topics = "send-ses", groupId = "deal")
    private void getSesCode(String data) {
        System.out.println("Ses code = " + data);
        sesCodeList.add(data);
        sesCodeCount++;
    }
}

