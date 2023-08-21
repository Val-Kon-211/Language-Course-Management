import java.util.List;

public class DemoCourse {
    protected String language;
    protected String level;
    protected String intensity;
    protected List<String> participants;
    public DemoCourse() {
        super();
    }
    public DemoCourse(String lang, String lvl, String intnst, List<String> part) {
        this.language = lang;
        this.level = lvl;
        this.intensity = intnst;
        this.participants = part;
    }

    public String get_language() {
        return this.language;
    }
    public String get_level() {
        return this.level;
    }
    public String get_intensity() {
        return this.intensity;
    }
    public List<String> get_participants() {
        return this.participants;
    }
    public int get_num_of_participants() {
        return this.participants.size();
    }
    public void add_participant(String person) {
        this.participants.add(person);
    }
}
