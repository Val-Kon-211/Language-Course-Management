import java.io.*;
import java.util.*;

public class Main {
    public static Map<String, Object> create_request() {
        Map<String, Object> request = new HashMap<String, Object>();

        //создаём списки значений для генерации заявок
        List<String> nameList = Arrays.asList("Александр", "Аркадий", "Анастасия", "Борис", "Инна",
                "Евгения", "Светлана", "Николай", "Екатерина", "Владислав", "Валерия", "Станислав",
                "Иван", "Юлия", "Анна", "Денис", "Сергей", "Ксения");
        List<String> langList = Arrays.asList("английский", "немецкий", "французский");
        List<String> levelList = Arrays.asList("начальный", "средний", "продвинутый");
        List<String> intensList = Arrays.asList("интенсивный", "обычный", "поддерживающее обучение");

        Random r = new Random();

        String name     = nameList.get(r.nextInt(nameList.size())) + r.nextInt(100) + 1;;
        String lang     = langList.get(r.nextInt(langList.size()));
        String lvl      = levelList.get(r.nextInt(levelList.size()));
        String intens   = intensList.get(r.nextInt(intensList.size()));
        boolean privat  = false;

        request.put("name", name);
        request.put("language", lang);
        request.put("level", lvl);
        request.put("intensity", intens);
        request.put("privat", privat);

        return request;
    }

    public static Course generate_course() {
        List<String> nameList = Arrays.asList("Александр", "Аркадий", "Анастасия", "Борис", "Инна",
                "Евгения", "Светлана", "Николай", "Екатерина", "Владислав", "Валерия", "Станислав",
                "Иван", "Юлия", "Анна", "Денис", "Сергей", "Ксения");
        List<String> langList = Arrays.asList("английский", "немецкий", "французский");
        List<String> levelList = Arrays.asList("начальный", "средний", "продвинутый");
        List<String> intensList = Arrays.asList("интенсивный", "обычный", "поддерживающее обучение");

        Random r = new Random();

        String lang     = langList.get(r.nextInt(langList.size()));
        String lvl      = levelList.get(r.nextInt(levelList.size()));
        String intens   = intensList.get(r.nextInt(intensList.size()));
        boolean prvat  = false;
        int duration = r.nextInt(11) + 2;

        List<String> participants = new ArrayList<>();
        int num_of_participants = r.nextInt(6) + 5;
        for (int i = 0; i < num_of_participants; i++) {
            String name = nameList.get(r.nextInt(nameList.size())) + r.nextInt(100) + 1;
            participants.add(name);
        }

        int[] schedule = new int[] {0,0,0,0,0,0,0};                 //будущее расписание курса
        int days_schedule = r.nextInt(5) + 1;                //генерация кол-ва занятий в неделю

        //генерация расписания
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<7; i++) list.add(i);
        Collections.shuffle(list);
        for (int i=0; i<days_schedule; i++) {
            int day = list.get(i);
            schedule[day] = 1;
        }

        return new Course(lang, lvl, intens, prvat, schedule, duration, participants);
    }

    public static void main(String[] args){

        Random random = new Random();
        int months = random.nextInt(13 - 3) + 3;      //период моделирования в месяцах
        int weeks = months * 4;                             //период моделирования в неделях

        try {
            FileWriter writer = new FileWriter("Modeling_process.txt");

            //СОЗДАНИЕ НАЧАЛЬНЫХ УСЛОВЬ
            //создание начального списка курсов
            int num_of_courses = random.nextInt(2) + 3;
            List<Course> courses = new ArrayList<Course>();
            for (int i = 0; i < num_of_courses; i++)
                courses.add(generate_course());

            //создание начального списка заявок
            List<Map<String, Object>> requests = new ArrayList<Map<String, Object>>();
            int num_of_requests = random.nextInt(5) + 3;
            for (int i = 0; i < num_of_requests; i++)
                requests.add(create_request());

            //НАЧАЛО МОДЕЛИРОВАНИЯ
            //создание мэнеджера курсов с начальными данными
            Manager manager = new Manager(courses, requests);

            writer.write("Модель 3.4. Менеджмент курсов иностранного языка\n");
            writer.write("Период поделирования: " + months + " мес." + "\n");
            writer.close();

            //ПРОЦЕСС РАБОТЫ МЕНЕДЖЕРА
            for (int i = 0; i < weeks; i = i + 2) {
                //ПРОВЕРКА СТУДЕНОВ
                manager.check_participants();

                //РАБОТА С ЗАЯВКАМИ
                //поступление новых заявок
                int num_new_requests = random.nextInt(5) + 3;
                for (int r = 0; r < num_new_requests; r++)
                    manager.add_requests(create_request());

                //ТЕКУЩЕЕ РАСПИСАНИЕ
                manager.schedule();
                //СТАТИСТИКА
                manager.statistic();
            }
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}