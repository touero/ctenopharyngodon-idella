package util;

public enum Url {
    SCHOOL_INFO_URL("https://static-data.gaokao.cn/www/2.0/school/args/info.json"),
    SCHOOL_MAJOR_URL("https://static-data.gaokao.cn/www/2.0/school/args/pc_special.json"),
    SCHOOL_SCORE_URL("https://api.eol.cn/web/api/?e_sort=zslx_rank,min&e_sorttype=desc,desc&local_province_id&local_type_id=1&pag1&school_id=args&size&uri=apidata/api/gk/score/province&year=2022&signsafe=61631c0843e48e52f2b238637cf68f65");

    private final String url;

    Url(String url) {
        this.url = url;
    }
    public String getUrl(String school_id){
        return url.replace("args",school_id);
    }
}
