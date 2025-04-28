package greenbuildings.commons.api.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import greenbuildings.commons.api.BaseEnum;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum Currency implements BaseEnum {
    USD("currency.usd"),
    VND("currency.vnd"),
    CNY("currency.cny");
    
    private final String code;
}
