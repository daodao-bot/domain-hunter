package run.ice.fun.domain.hunter.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import run.ice.lib.core.error.ErrorEnum;

@Getter
@AllArgsConstructor
public enum HunterError implements ErrorEnum {

    ERROR("100000", "ERROR"),
    DOMAIN_NOT_EXIST("100001", "域名不存在"),
    DOMAIN_ALREADY_EXIST("100002", "域名已存在"),
    TLD_ERROR("100003", "顶级域名错误"),
    BIT_ERROR("100004", "位数错误"),

    ;

    /**
     * 错误编码
     */
    public final String code;

    /**
     * 错误信息
     */
    public final String message;

}
