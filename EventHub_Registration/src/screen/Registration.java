package screen;

public class Registration {
    private String eventName;
    private String category;
    private String semester;

    public Registration(String eventName, String category, String semester) {
        this.eventName = eventName;
        this.category = category;
        this.semester = semester;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategory() {
        return category;
    }

    public String getSemester() {
        return semester;
    }
}