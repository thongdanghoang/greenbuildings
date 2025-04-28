package greenbuildings.commons.api.events;

import greenbuildings.commons.api.exceptions.BusinessErrorParam;
import lombok.Builder;

import java.util.List;

@Builder
public record RollbackEnterpriseRegisterEvent(
        String correlationId,
        String field,
        String i18nKey,
        List<BusinessErrorParam> args
) {
    public static final String TOPIC = "enterprise-create-failed-event";
}
