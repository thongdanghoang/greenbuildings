package greenbuildings.commons.springfw.impl.utils;

import greenbuildings.commons.api.exceptions.TechnicalException;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;

@Component
@RequiredArgsConstructor
public class EmailUtil implements IEmailUtil {
    
    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final TaskExecutor taskExecutor;
    
    private static final String EMAIL_HOST_FROM = "SEP490";
    
    @Override
    public void sendMail(SEPMailMessage mailMessage) {
        taskExecutor.execute(() -> {
            try {
                String[] to = mailMessage.getTo();
                if (to == null || to.length == 0) {
                    throw new TechnicalException("Cannot send mail because to field is empty");
                }
                MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
                
                copyDataToHelper(mailMessage, helper);
                
                mailSender.send(helper.getMimeMessage());
            } catch (Exception e) {
                throw new TechnicalException(e);
            }
        });
    }
    
    private void copyContentToHelper(SEPMailMessage mailMessage, MimeMessageHelper helper) throws IOException, MessagingException, TemplateException {
        if (StringUtils.isNotEmpty(mailMessage.getTemplateName())) {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(mailMessage.getTemplateName(), LocaleContextHolder.getLocale());
            helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, mailMessage.getTemplateModels()), true);
        } else {
            String text = mailMessage.getText();
            if (StringUtils.isNotEmpty(text)) {
                helper.setText(text);
            } else {
                throw new TechnicalException("Trying to send empty email");
            }
        }
    }
    
    private void copyDataToHelper(SEPMailMessage message, MimeMessageHelper helper) throws MessagingException, TemplateException, IOException {
        copyContentToHelper(message, helper);
        copySimpleDataToHelper(message, helper);
        
        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            for (Entry<String, File> entry : message.getAttachments().entrySet()) {
                helper.addAttachment(entry.getKey(), new FileSystemResource(entry.getValue()));
            }
        }
    }
    
    
    private void copySimpleDataToHelper(SEPMailMessage message, MimeMessageHelper helper) throws MessagingException {
        helper.setFrom(EMAIL_HOST_FROM);
        
        Date sentDate = message.getSentDate();
        if (sentDate != null) {
            helper.setSentDate(sentDate);
        }
        
        String subject = message.getSubject();
        if (subject != null) {
            helper.setSubject(subject);
        }
        
        String[] messageTo = message.getTo();
        if (messageTo != null) {
            helper.setTo(messageTo);
        }
        
        String[] bcc = message.getBcc();
        if (bcc != null) {
            helper.setBcc(bcc);
        }
        
        String[] cc = message.getCc();
        if (cc != null) {
            helper.setCc(cc);
        }
        
        String replyTo = message.getReplyTo();
        if (replyTo != null) {
            helper.setReplyTo(replyTo);
        }
    }
    
    @Override
    public String maskEmail(String email) {
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];
        
        // Mask the local part
        int localVisibleLength = Math.min(3, localPart.length());
        String maskedLocal = localPart.substring(0, localVisibleLength)
                             + "•".repeat(localPart.length() - localVisibleLength);
        
        // Mask the domain part
        String[] domainParts = domainPart.split("\\.");
        
        String domainName = domainParts[0];
        int domainVisibleLength = Math.min(1, domainName.length());
        String maskedDomain = domainName.substring(0, domainVisibleLength)
                              + "•".repeat(domainName.length() - domainVisibleLength);
        
        // Join the masked domain name with the rest of the domain parts
        String maskedFullDomain = maskedDomain + "." + String.join(".", Arrays.copyOfRange(domainParts, 1, domainParts.length));
        
        
        // Combine masked local and domain parts
        return maskedLocal + "@" + maskedFullDomain;
    }
    
}
