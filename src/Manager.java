import javax.swing.*;
import java.io.*;
import java.util.*;

public class Manager {
    private List<Course> courses;
    private Map<Integer, List<Course>> schedule;
    private List<Map<String, Object>> requests;
    private List<DemoCourse> demoCourses;
    private Map<String, Integer> statistic;
    public Manager() {
        this.courses = new ArrayList<>();
        this.requests = new ArrayList<Map<String, Object>>();
        this.demoCourses = new ArrayList<DemoCourse>();
        this.schedule = new HashMap<Integer, List<Course>>();
        this.schedule.put(1, new ArrayList<Course>());
        this.schedule.put(2, new ArrayList<Course>());
        this.schedule.put(3, new ArrayList<Course>());
        this.schedule.put(4, new ArrayList<Course>());
        this.schedule.put(5, new ArrayList<Course>());
        this.schedule.put(6, new ArrayList<Course>());
        this.schedule.put(7, new ArrayList<Course>());

        this.statistic = new HashMap<String, Integer>();
        this.statistic.put("participants", 0);
        this.statistic.put("requests", 0);
    }
    public Manager(List<Course> courses, List<Map<String, Object>> requests) {
        this.courses = new ArrayList<>();
        this.courses.addAll(courses);
        this.requests = requests;
        this.demoCourses = new ArrayList<DemoCourse>();
        this.schedule = new HashMap<Integer, List<Course>>();
        this.schedule.put(1, new ArrayList<Course>());
        this.schedule.put(2, new ArrayList<Course>());
        this.schedule.put(3, new ArrayList<Course>());
        this.schedule.put(4, new ArrayList<Course>());
        this.schedule.put(5, new ArrayList<Course>());
        this.schedule.put(6, new ArrayList<Course>());
        this.schedule.put(7, new ArrayList<Course>());

        int numNewParticipants = 0;
        for (Course c : courses) {
            int[] course_schedule = c.get_schedule();
            for (int i = 0; i < 7; i++) {
                if (course_schedule[i] == 1) {
                    this.schedule.get(i + 1).add(c);
                }
            }
            numNewParticipants += c.get_num_of_participants();
        }

        this.statistic = new HashMap<String, Integer>();
        this.statistic.put("participants", numNewParticipants);
        this.statistic.put("requests", requests.size());
    }
    private void add_course(Course course) {
        this.courses.add(course);
        this.statistic.put("participants", this.statistic.get("participants") + course.get_num_of_participants());

        int[] course_schedule = course.get_schedule();
        for (int i = 0; i < 7; i++) {
            if (course_schedule[i] == 1) {
                this.schedule.get(i+1).add(course);
            }
        }
    }
    public void delete_course(Course course) {
        //удаление курса из списка курсов
        this.courses.remove(course);
        //удаление курса из расписания
        for (Integer weekday : this.schedule.keySet()) {
            List<Course> temp_course_list = this.schedule.get(weekday);
            int course_index = temp_course_list.indexOf(course);
            if (course_index != -1) {
                temp_course_list.remove(course_index);
                this.schedule.put(weekday, temp_course_list);
            }
        }
    }
    public void check_participants() {
        List<Course> deleted_courses = new ArrayList<Course>();
        //процесс исключения студентов из каждого курса + уменьшение продолжительности курса
        for (Course course : this.courses) {
            //сокращение продолжительности курса
            course.reduce_duration();
            //если курс закончился, добавляем его в список на удаление
            if (course.get_duration() < 1) {
                deleted_courses.add(course);
                continue;
            }
            //генерация числа студентов, которые прекратят заниматься (от 0 до 1 человек)
            Random random = new Random();
            int num_of_delete_part = random.nextInt(2);

            List<String> participants = course.get_participants();
            List<String> delete_parts = new ArrayList<String>();
            if (participants.size() >= num_of_delete_part) {
                for (int j = 0; j < num_of_delete_part; j++)
                    delete_parts.add(participants.get(j));

                for (int k = 0; k < num_of_delete_part; k++)
                    course.delete_participant((String) delete_parts.get(k));
            }

            //если из курса ушли все студенты, добавляем его в список на удаление
            if (course.get_num_of_participants() < 1) {
                deleted_courses.add(course);
                continue;
            }
        }
        //удаляем неактивные курсы
        for (Course c : deleted_courses) {
            delete_course(c);
            continue;
        }
    }
    private void courses_analysis() {

    }
    private void demoCourses_analysis() {
        List<DemoCourse> deleted_demoCourses = new ArrayList<DemoCourse>();
        //рассматриваем существующие демо-курсы
        for(DemoCourse demoCourse : this.demoCourses) {
            int participants_num = demoCourse.get_num_of_participants();
            //если набралось достаточное количество человек
            if (participants_num > 4 && participants_num < 11) {
                //то создаём курс (активируем курс)
                Random random = new Random();
                int[] schedule = new int[] {0,0,0,0,0,0,0};                 //будущее расписание курса
                int days_schedule = random.nextInt(5) + 1;            //генерация кол-ва занятий в неделю

                //генерация расписания
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int i=0; i<7; i++) list.add(i);
                Collections.shuffle(list);
                for (int i=0; i<days_schedule; i++) {
                    int day = list.get(i);
                    schedule[day] = 1;
                }
                Course newCourse = new Course(
                        demoCourse.get_language(),
                        demoCourse.get_level(),
                        demoCourse.get_intensity(),
                        false,
                        schedule,
                        random.nextInt(11) + 2,
                        demoCourse.get_participants());
                add_course(newCourse);
                deleted_demoCourses.add(demoCourse);
                try {
                    FileWriter writer = new FileWriter("Modeling_process.txt", true);
                    writer.write("\n-----------------------------\n");
                    writer.write("\nБыл создан новый курс\n");
                    Map<String, Object> info = newCourse.get_all_info();
                    for (String key : info.keySet()) {
                        writer.write("\t" + key + " : " + info.get(key) + "\n");
                    }
                    writer.write("\tдни : ");
                    for (int d = 0; d < 7; d++) {
                        if (schedule[d] == 1) {
                            if (d == 0) writer.write(" пн ");
                            if (d == 1) writer.write(" вт ");
                            if (d == 2) writer.write(" ср ");
                            if (d == 3) writer.write(" чт ");
                            if (d == 4) writer.write(" пт ");
                            if (d == 5) writer.write(" сб ");
                            if (d == 6) writer.write(" вс ");
                        }
                    }
                    writer.write("\n-----------------------------\n");
                    writer.close();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
        //удаляем активированные демо-курсы из списка демо-курсов
        for(DemoCourse demo : deleted_demoCourses) {
            this.demoCourses.remove(demo);
        }
        demoCourses.removeAll(deleted_demoCourses);
    }
    private void requests_analysis() {
        //флаг, что студент добавлен на курс
        boolean request_added;
        //рассматриваем заявки
        for (Map<String, Object> request : this.requests) {
            request_added = false;
            Object lang = request.get("language");
            Object lvl = request.get("level");
            Object intens = request.get("intensity");
            String name = (String) request.get("name");
            Object privatn = request.get("privat");

            //перебираем существующие курсы
            for (Course course : this.courses) {
                //проверяем, можно ли добавить студента на курс
                if (course.get_language() == lang && course.get_level() == lvl
                        && course.get_intensity() == intens && course.get_num_of_participants() < 10
                        && !((Boolean) privatn)) {
                    course.add_participant(name);
                    this.statistic.put("participants", this.statistic.get("participants") + 1);
                    request_added = true;
                    break;
                }
            }
            //если студент не добавлен ни в одну из групп
            if (!request_added) {
                //предлагаем индивидуальные занятия
                Random random = new Random();
                //если соглашается => создаём индивидуальный курс
                if (random.nextInt(100) > 90) {
                    List<String> part = new ArrayList<String>();
                    part.add(name);

                    int[] schedule = new int[] {0,0,0,0,0,0,0};                 //будущее расписание курса
                    int days_schedule = random.nextInt(5) + 1;            //генерация кол-ва занятий в неделю

                    //генерация расписания
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (int i=0; i<7; i++) list.add(i);
                    Collections.shuffle(list);
                    for (int i=0; i<days_schedule; i++) {
                        int day = list.get(i);
                        schedule[day] = 1;
                    }
                    Course newCourse = new Course(
                            (String) lang,
                            (String) lvl,
                            (String) intens,
                            true,
                            schedule,
                            random.nextInt(11) + 2,
                            part);
                }
                //если отказался, то проверяем демо-курсы
                else {
                    if (this.demoCourses.size() == 0) {
                        List<String> participants = new ArrayList<String>();
                        participants.add(name);
                        DemoCourse demoCourse = new DemoCourse(
                                (String) lang,
                                (String) lvl,
                                (String) intens,
                                participants);
                        this.demoCourses.add(demoCourse);
                    } else {
                        for (DemoCourse demoCourse : this.demoCourses) {
                            if (demoCourse.get_language() == lang
                                    && demoCourse.get_level() == lvl
                                    && demoCourse.get_intensity() == intens
                                    && demoCourse.get_num_of_participants() < 10) {
                                demoCourse.add_participant(name);
                                request_added = true;
                                break;
                            }
                        }
                        //если студент всё ещё не добавлен на курс
                        if (!request_added) {
                            //создаём новый демо-курс
                            List<String> participants = new ArrayList<String>();
                            participants.add(name);
                            DemoCourse demoCourse = new DemoCourse(
                                    (String) lang,
                                    (String) lvl,
                                    (String) intens,
                                    participants);
                            this.demoCourses.add(demoCourse);
                        }
                    }
                }
            }
        }
        //очищаем список заявок
        this.requests.removeAll(this.requests);
        //вызываем анализатор демо-курсов
        demoCourses_analysis();
    }
    public void add_requests(Map<String, Object> r) {
        this.requests.add(r);
        this.statistic.put("requests", this.statistic.get("requests") + 1);
        requests_analysis();
    }
    public void schedule() {
        try {
            FileWriter writer = new FileWriter("Modeling_process.txt", true);
            writer.write("Расписание курсов\n");
            for (Integer weekday : this.schedule.keySet()) {
                if (weekday == 1) {
                    writer.write("Понедельник\n");
                } else if (weekday == 2) {
                    writer.write("Вторник\n");
                } else if (weekday == 3) {
                    writer.write("Среда\n");
                } else if (weekday == 4) {
                    writer.write("Четверг\n");
                } else if (weekday == 5) {
                    writer.write("Пятница\n");
                } else if (weekday == 6) {
                    writer.write("Суббота\n");
                } else if (weekday == 7) {
                    writer.write("Воскресенье\n");
                }

                for (Course course : this.schedule.get(weekday)) {
                    Map<String, Object> info = course.get_all_info();
                    for (String key : info.keySet()) {
                        writer.write("\t" + key + " : " + info.get(key) + "\n");
                    }
                    writer.write("\n");
                }
            }
            writer.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void statistic() {
        try {
            FileWriter writer = new FileWriter("Modeling_process.txt", true);
            writer.write("----------------------------\n");
            writer.write("Статистика\n");
            writer.write("\tАктивных курсов : " + this.courses.size() + "\n");
            int part = 0;
            for (Course c : this.courses) {
                part += c.get_num_of_participants();
            }
            writer.write("\tЧисло студентов : " + part + "\n");
            writer.write("\n");
            writer.write("Было принято заявок : " + this.statistic.get("requests") + "\n");
            writer.write("Было принято студентов : " + this.statistic.get("participants") + "\n");
            writer.write("----------------------------\n");
            writer.write("\n");
            writer.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}
