package orj.worf.authentication.constant;

/**
 * 平台
 */
public enum PlatformEnum {

    WEB("web", "网页"),
    WAP("wap", "微网站"),
    IOS("ios", "IOS"),
    ANDROID("android", "安卓"),
    WX("wx", "微信"),
    ;

    private final String code;

    private final String name;

    private PlatformEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否包含某个code
     */
    public boolean containCode(String code) {
        PlatformEnum[] platformEnums = PlatformEnum.values();
        for (PlatformEnum platformEnum : platformEnums) {
            if (platformEnum.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
