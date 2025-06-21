package tw.teddysoft.tasks.usecase;

import tw.teddysoft.tasks.TaskList;
import tw.teddysoft.tasks.entity.*;
import tw.teddysoft.tasks.usecase.port.out.ToDoListRepository;
import tw.teddysoft.tasks.usecase.service.SetDoneInput;
import tw.teddysoft.tasks.usecase.service.SetDoneService;
import tw.teddysoft.tasks.usecase.service.SetDoneUseCase;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Execute {

    private final ToDoList toDoList;
    private final PrintWriter out;
    private final ToDoListRepository repository;


    public Execute(ToDoList toDoList, PrintWriter out,ToDoListRepository repository) {
        this.toDoList = toDoList;
        this.out = out;
        this.repository = repository;
    }

    public void execute(String commandLine) {
        String[] commandRest = commandLine.split(" ", 2);
        String command = commandRest[0];
        switch (command) {
            case "show":
                new Show(toDoList,out).show();
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
            default:
                error(command);
                break;
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
    private void check(String idString) {
        setDone(idString, true);
    }

    private void uncheck(String idString) {
        setDone(idString, false);
    }

    /*
    private void setDone(String idString, boolean done) {
        TaskId taskId = TaskId.of(idString);

        for (Project project : toDoList.getProjects()) {
            for (Task task : project.getTasks()) {
                if (task.getId().equals(taskId)) {
                    //task.setDone(done);
                    toDoList.setDone(taskId,done);
                    return;
                }
            }
        }

        out.printf("Could not find a task with an ID of %s.", taskId);
        out.println();
    }
     */
    private void setDone(String taskId, boolean done) {
        SetDoneUseCase setDoneUseCase = new SetDoneService(repository);
        SetDoneInput input = new SetDoneInput();
        input.toDoListId = TaskList.DEFAULT_TO_DO_LIST_ID;
        input.taskId = taskId;
        input.done = done;
        out.print(setDoneUseCase.execute(input).getMessage());
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

    private void addProject(String name) {
        toDoList.addProject(name, new ArrayList<Task>());
    }

    private void addTask(String project, String description) {
        List<Task> projectTasks = toDoList.getTasks(ProjectName.of(project));
        if (projectTasks == null) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
        toDoList.addTask(ProjectName.of(project),description,false);
        //projectTasks.add(new Task(TaskId.of(nextId()), description, false));
    }


}
