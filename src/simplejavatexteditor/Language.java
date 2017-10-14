package simplejavatexteditor;

/**
 * Created by Victor Forsgren on 2017-10-14.
 */
public class Language {
    // Dont use dots ahead of language name
    private String name = "generic";
    private String[] supportedKeywords = {"abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "System", "out", "print()", "println()",
            "new", "null", "package", "private", "protected", "public", "interface",
            "long", "native", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while", "String"};

    public Language(String name, String[] supportedKeywords) {
        this.name = name;
        this.supportedKeywords = supportedKeywords;
    }

    public String getName() {
        return name;
    }

    public String[] getSupportedKeywords() {
        return supportedKeywords;
    }

}
