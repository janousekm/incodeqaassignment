package enums;

public enum AlertMessages {

    FACE_ADDED_TO_DB("Face Added to DB"),
    FLOW_SAVED("Flow saved correctly."),
    IDENTITY_REMOVED("Identity PII data removed"),
    FLOW_REMOVED("Flow has been deleted.");


    private final String value;

    AlertMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
