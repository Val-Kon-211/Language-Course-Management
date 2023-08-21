import java.util.*;
public class Course extends DemoCourse{
    private boolean privatn;
    private int[] schedule;
    private int duration;
    public Course() {
        super();
    }
    public Course(String lang,
                  String lvl,
                  String intnst,
                  boolean prvt,
                  int[] d,
                  int duration,
                  List<String> part) {
        super(lang, lvl, intnst, part);
        this.privatn = prvt;
        this.schedule = d;
        this.duration = duration;
    }
    public Map<String, Object> get_all_info() {

        Map<String, Object> course_info = new HashMap<>();
        course_info.put("язык", this.language);
        course_info.put("уровень", this.level);
        course_info.put("интенсивность", this.intensity);
        if (this.privatn) course_info.put("тип", "приватный");
        else course_info.put("тип", "групповое");
        course_info.put("продолжительность", Integer.toString(this.duration));
        course_info.put("слушатели", this.participants.size());

        return course_info;
    }
    public int get_duration() {
        return this.duration;
    }
    public int[] get_schedule() {
        return this.schedule;
    }
    public void delete_participant(String person) {
        this.participants.remove(person);
    }
    public void reduce_duration() {
        this.duration -= 2;
    }
}
