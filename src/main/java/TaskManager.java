import javax.imageio.IIOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    List<Task> lst = new ArrayList<>();
    final String seperatorLine = "-----------------------------";
    File file;
    TaskManager() {
        try {
            this.file = new File("duke.txt");
            if (file.createNewFile()) {
                System.out.println("Created file: " + file.getName());
            }
            Scanner sc = new Scanner(this.file);
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                char type = str.charAt(1);
                char status = str.charAt(3);
                String desc = str.substring(7);
                if (type == 'T') {
                    lst.add(new ToDo(desc));
                } else if (type == 'D') {
                    int index = desc.indexOf('(');
                    lst.add(new Deadline(desc.substring(0, index), desc.substring(index + 5)));
                } else if (type == 'E') {
                    int index = desc.indexOf('(');
                    lst.add(new Event(desc.substring(0, index), desc.substring(index + 5)));
                }
                if (status == 'X') {
                    lst.get(lst.size() - 1).markDone();
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred.");
        }

    }

    private void addToDo(String desc) throws DukeException {
        desc = desc.trim();
        if (desc.isEmpty()) {
            throw new DukeException("The description of a todo cannot be empty.");
        }
        lst.add(new ToDo(desc));
    }

    private void addDeadline(String desc) throws DukeException {
        desc = desc.trim();
        if (desc.isEmpty()) {
            throw new DukeException("The description of a deadline cannot be empty.");
        }
        int index = desc.indexOf('/');
        lst.add(new Deadline(desc.substring(0, index - 1), desc.substring(index + 4)));
    }

    private void addEvent(String desc) throws DukeException {
        desc = desc.trim();
        if (desc.isEmpty()) {
            throw new DukeException("The description of an event cannot be empty.");
        }
        int index = desc.indexOf('/');
        lst.add(new Event(desc.substring(0, index - 1), desc.substring(index + 4)));
    }

    public void addTask(String command) throws DukeException {
        if (command.startsWith("todo")) {
            addToDo(command.substring(4));
        } else if (command.startsWith("deadline")) {
            addDeadline(command.substring(8));
        } else if (command.startsWith("event")) {
            addEvent(command.substring(5));
        } else {
            throw new DukeException("I'm sorry, but I don't know what that means :-(");
        }
        System.out.println(seperatorLine);
        System.out.println("Got it. Now I have added this " +
                "task:\n" + "  " + lst.get(lst.size() - 1) + "\n" +
                "Now you have " + lst.size() + " tasks in the list.");
        System.out.println(seperatorLine);


    }

    public void markDone(String position) {
        int index = Integer.parseInt(position) - 1;
        lst.get(index).markDone();
        System.out.println(seperatorLine);
        System.out.println("Nice! I've marked this task as done:\n" +
                "  " + lst.get(index) + "\n");
        System.out.println(seperatorLine);
    }

    public void deleteTask(String position) throws DukeException {
        int index = Integer.parseInt(position) - 1;
        if (index >= lst.size() || index < 0) {
            throw new DukeException("Index out of bounds.");
        }
        Task task = lst.remove(index);
        System.out.println(seperatorLine);
        System.out.println("Noted. I've removed this " +
                "task:\n" + "  " + task + "\n" +
                "Now you have " + lst.size() + " tasks in the list.");
        System.out.println(seperatorLine);
    }

    public void listTask() {
        System.out.println(seperatorLine);
        for (int i = 0; i < lst.size(); i++) {
            System.out.println((i + 1) + ". " + lst.get(i));
        }
        System.out.println(seperatorLine);
    }

    public void writeFile() throws DukeException {
        try {
//            File file = new File(String.valueOf(path));
            FileWriter fileWriter = new FileWriter(this.file);
            for (Task task: lst) {
                fileWriter.write(task.toString() + '\n');
            }
            fileWriter.close();
        } catch (IOException err) {
            throw new DukeException("Error during file writing.");
        }
    }

}
