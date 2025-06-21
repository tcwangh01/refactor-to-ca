package tw.teddysoft.tasks.entity;

import edu.emory.mathcs.backport.java.util.LinkedList;
import tw.teddysoft.ezddd.core.entity.AggregateRoot;
import tw.teddysoft.ezddd.core.entity.DomainEvent;

import java.util.*;

public class ToDoList extends AggregateRoot<ToDoListId, DomainEvent> {

    private final List<Project> projects = new LinkedList();
    private final ToDoListId id;
    private long lastTaskId;

    public ToDoList(ToDoListId id) {
        this.id = id;
    }

    public List<Project> getProjects() {

        return projects.stream().map(project -> (Project) new ReadOnlyProject(project)).toList();
        //return projects;
    }

    public void addProject(String name, ArrayList<Task> tasks) {
        projects.add(new Project(ProjectName.of(name), tasks) );
    }

    public List<Task> getTasks(ProjectName projectName) {
        /*
        return projects.stream().filter(project ->
                project.getName().equals(projectName)).
                findFirst().map(Project::getTasks).orElse(null);
        */
        return projects.stream()
                .filter(x -> x.getName().equals(projectName))
                .findFirst()
                .map(project -> project.getTasks().stream()
                        .map(t -> (Task) new ReadOnlyTask(t))
                        .toList())
                .orElse(null);

    }

    public void addTask(ProjectName name, String description, boolean done) {
        Optional<Project> project = getProject(name);
        project.ifPresent(p -> p.addTask(new Task(TaskId.of(nextTaskId()), description, done)));
    }

    public Optional<Project> getProject(ProjectName projectName) {
        return projects.stream().filter(p -> p.getName().equals(projectName)).findFirst();
    }

    @Override
    public ToDoListId getId() {
        return this.id;
    }

    public void setDone(TaskId taskId, boolean done) {
        projects.stream()
                .filter(p -> p.containTask(taskId))
                .findFirst()
                .ifPresent(p -> p.setTaskDone(taskId, done));
    }

    private long nextTaskId() {
        return ++lastTaskId;
    }

    public boolean containTask(TaskId taskId) {
        return projects.stream().anyMatch(p-> p.containTask(taskId));
    }
}
