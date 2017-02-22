package core.filesystem;

/**
 * Enum with all supported file types
 */
public enum SupportedFileTypes {

    TXT,    //Text file
    PNG,    //Image
    JPG,    //Image
    PDF,    //Document
    DOC,    //Word Document
    DOCX,   //Word Document
    ODT,    //Open Document
    PPT,    //PowerPoint Presentation
    PPTX,   //PowerPoint Presentation
    ODP;     //Open Presentation

    public static boolean isValidExtension(String extension) {
        for (SupportedFileTypes type : SupportedFileTypes.values())
            if (type.toString().toLowerCase().equals(extension))
                return true;

        return false;
    }

}
