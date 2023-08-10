package util;

public enum MySqlInfo {
    JDBC_DRIVER("com.mysql.jdbc.Driver"),
    DB_URL("jdbc:mysql://localhost:3306/AllSchoolAPI?useUnicode=true&characterEncoding=UTF-8"),
    USER("username"),
    PASSWORD("password");
    private final String value;

    MySqlInfo(String value) {
        this.value = value;
    };

    public String value(){
        return this.value;
    }
}
