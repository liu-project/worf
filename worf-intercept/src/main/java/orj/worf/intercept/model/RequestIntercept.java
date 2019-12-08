package orj.worf.intercept.model;

public class RequestIntercept {

    private Integer id;

    // 请求路径
    private String uri;

    // 路径描述
    private String info;

    // 是否拦截：0否1是
    private Byte interceptStatus;

    // 拦截提示信息
    private String interceptInfo;

    // 拦截IP地址，以","分割
    private String interceptIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Byte getInterceptStatus() {
        return interceptStatus;
    }

    public void setInterceptStatus(Byte interceptStatus) {
        this.interceptStatus = interceptStatus;
    }

    public String getInterceptInfo() {
        return interceptInfo;
    }

    public void setInterceptInfo(String interceptInfo) {
        this.interceptInfo = interceptInfo == null ? null : interceptInfo.trim();
    }

    public String getInterceptIp() {
        return interceptIp;
    }

    public void setInterceptIp(String interceptIp) {
        this.interceptIp = interceptIp == null ? null : interceptIp.trim();
    }

}
