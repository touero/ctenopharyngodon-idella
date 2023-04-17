package util;

public enum Header {
    ACCEPT("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
    ACCEPT_ENCODING("gzip, deflate, br"),
    ACCEPT_LANGUAGE("zh-CN,zh;q=0.9"),
    CONTENT_TYPE("application/json;charset=UTF-8"),
    USER_AGENT("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"),
    COOKIE("Hm_lvt_0a962de82782bad64c32994d6c9b06d3=1681049933; gr_user_id=a762e1b4-6095-4638-9b52-b40b0a4a1194");

    private final String value;

    Header(String value) {
        this.value = value;
    };

    public String value(){
        return this.value;
    }
}
