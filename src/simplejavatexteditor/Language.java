package simplejavatexteditor;

/**
 * Created by Victor Forsgren on 2017-10-14.
 */
public class Language {
    // Dont use dots ahead of language name
    private String name = "languagename";
    private String[] supportedKeywords;

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
