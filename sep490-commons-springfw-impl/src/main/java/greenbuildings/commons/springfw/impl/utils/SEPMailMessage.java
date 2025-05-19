package greenbuildings.commons.springfw.impl.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.mail.SimpleMailMessage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SEPMailMessage extends SimpleMailMessage {

    private String templateName;

    private Map<String, Object> templateModels = new HashMap<>();

    private Map<String, File> attachments = new HashMap<>();
    
    public void addTemplateModel(String modelName, Object model) {
        if (modelName == null || model == null) {
            throw new IllegalArgumentException("Model name and model cannot be null");
        }
        this.templateModels.put(modelName, model);
    }

}
