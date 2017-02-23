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
    ODP;    //Open Presentation

    /**
     * Checks if a file extension is supported
     *
     * @param extension of the file
     * @return if extension is valid
     */
    public static boolean isValidExtension(String extension) {
        for (SupportedFileTypes type : SupportedFileTypes.values())
            if (type.toString().toLowerCase().equals(extension))
                return true;

        return false;
    }

}
