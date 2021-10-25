package daedan.mes.common.service.util;

public class HtmlUtils {
    public static String parseBrTag(String contents){
        return contents.replace("\n","<br>");
    }
}
