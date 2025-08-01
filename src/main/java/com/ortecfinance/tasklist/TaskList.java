package com.ortecfinance.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class TaskList implements Runnable {
    private static final String QUIT = "quit";

    private final Map<String, List<Task>> tasks = new LinkedHashMap<>();
    private final BufferedReader in;
    private final PrintWriter out;

    private long lastId = 0;

    public static void startConsole() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        new TaskList(in, out).run();
    }

    public TaskList(BufferedReader reader, PrintWriter writer) {
        this.in = reader;
        this.out = writer;
    }

    public void run() {
        out.println("Welcome to TaskList! Type 'help' for available commands.");
        while (true) {
            out.print("> ");
            out.flush();
            String command;
            try {
                command = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (command.equals(QUIT)) {
                break;
            }
            execute(command);
        }
    }

    private void execute(String commandLine) {
        String[] commandRest = commandLine.split(" ", 2);
        String command = commandRest[0];
        switch (command) {
            case "show":
                show();
                break;
            case "add":
                add(commandRest[1]);
                break;
            case "check":
                check(commandRest[1]);
                break;
            case "uncheck":
                uncheck(commandRest[1]);
                break;
            case "help":
                help();
                break;
            case "deadline":
                deadline(commandRest[1]);
                break;
            case "today":
                today();
                break;
            case "view-by-deadline":
                viewByDeadline();
                break;
            default:
                error(command);
                break;
        }
    }

    private void viewByDeadline() {
        Map<LocalDate, Map<String, List<Task>>> sortedTasks = new TreeMap<>();
        Map<String, List<Task>> noDeadlineTasks = new LinkedHashMap<>();

        // tasks in the according Maps added
        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            for (Task task : project.getValue()) {
                if (task.getDeadline() != null) {
                    sortedTasks
                            .computeIfAbsent(task.getDeadline(), k -> new LinkedHashMap<>())
                            .computeIfAbsent(project.getKey(), l -> new ArrayList<>())
                            .add(task);
                } else {
                    noDeadlineTasks.computeIfAbsent(project.getKey(), k -> new ArrayList<>())
                            .add(task);
                }
            }
        }
        if (!sortedTasks.isEmpty()) {
            // Print tasks with deadlines first in order of the deadlines
            for (Map.Entry<LocalDate, Map<String, List<Task>>> entry : sortedTasks.entrySet()) {
                // get the date
                out.println(entry.getKey().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ":");

                for (Map.Entry<String, List<Task>> project : entry.getValue().entrySet()) {
                    // get project name
                    out.printf("    %s:%n", project.getKey());

                    for (Task task : project.getValue()) {
                        out.printf("        %d: %s%n", task.getId(), task.getDescription());
                    }
                }
            }
        }

        // print tasks without deadlines
        if (!noDeadlineTasks.isEmpty()){
            out.println("No deadline:");

            for (Map.Entry<String, List<Task>> project : noDeadlineTasks.entrySet()){
                out.printf("    %s:%n", project.getKey());

                for (Task task : project.getValue()){
                    out.printf("        %d: %s%n", task.getId(), task.getDescription());
                }
            }
        }

    }

    private void today() {
        LocalDate today = LocalDate.now();

        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            List<Task> tasksToday = project.getValue().stream()
                    .filter(task -> today.equals(task.getDeadline()))
                    .toList();

            if (!tasksToday.isEmpty()) {
                out.println(project.getKey());
                for (Task task : tasksToday) {
                    out.printf("    [%c] %d: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
                }
                out.println();
            }
        }
    }

    private void deadline(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        int id = Integer.parseInt(subcommandRest[0]);
        LocalDate deadline = LocalDate.parse(subcommandRest[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        for (List<Task> tasksList : tasks.values()) {
            for (Task task : tasksList) {
                if (task.getId() == id) {
                    task.setDeadline(deadline);
                    out.printf("Deadline set for task %d: %s%n", id, deadline.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    return;
                }
            }
        }
    }

    private void show() {
        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            out.println(project.getKey());
            for (Task task : project.getValue()) {
                out.printf("    [%c] %d: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    private void add(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[0];
        if (subcommand.equals("project")) {
            addProject(subcommandRest[1]);
        } else if (subcommand.equals("task")) {
            String[] projectTask = subcommandRest[1].split(" ", 2);
            addTask(projectTask[0], projectTask[1]);
        }
    }

    private void addProject(String name) {
        tasks.put(name, new ArrayList<Task>());
    }

    private void addTask(String project, String description) {
        List<Task> projectTasks = tasks.get(project);
        if (projectTasks == null) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
        projectTasks.add(new Task(nextId(), description, false));
    }

    private void check(String idString) {
        setDone(idString, true);
    }

    private void uncheck(String idString) {
        setDone(idString, false);
    }

    private void setDone(String idString, boolean done) {
        int id = Integer.parseInt(idString);
        for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
            for (Task task : project.getValue()) {
                if (task.getId() == id) {
                    task.setDone(done);
                    return;
                }
            }
        }
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
    }

    private void help() {
        out.println("Commands:");
        out.println("  show");
        out.println("  add project <project name>");
        out.println("  add task <project name> <task description>");
        out.println("  check <task ID>");
        out.println("  uncheck <task ID>");
        out.println();
    }

    private void error(String command) {
        out.printf("I don't know what the command \"%s\" is.", command);
        out.println();
    }

    private long nextId() {
        return ++lastId;
    }
}
