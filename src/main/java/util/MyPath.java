package util;

import org.apache.hadoop.fs.Path;


public enum MyPath {
    WINDOWS_INPUT_PATH("InputFile/schoolUrl.txt"),
    WINDOWS_OUTPUT_PATH("OutputFile"),
    LINUX_INPUT_PATH(""),
    LINUX_OUTPUT_PATH(""),
    MAC_INPUT_PATH(""),
    MAC_OUTPUT_PATH(""),
    HADOOP_INPUT_PATH("/Input/schoolUrl.txt"),
    HADOOP_OUTPUT_PATH("/Output/schoolAll");

    private final String string_value;


    MyPath(String string_value) {
        this.string_value = string_value;
    }
    public String string_value(){
        return this.string_value;
    }

    public Path path_value(){
        return new Path(this.string_value);
    }

}
