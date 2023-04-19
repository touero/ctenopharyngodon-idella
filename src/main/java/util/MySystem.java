package util;

public enum MySystem{
    WINDOWS("Windows"),
    LINUX("Linux"),
    MAC("Mac");

    private final String value;

    MySystem(String value) {
        this.value = value;
    }
    public String value(){
        return this.value;
    }
}