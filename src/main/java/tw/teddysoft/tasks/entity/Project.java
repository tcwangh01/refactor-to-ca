package tw.teddysoft.tasks.entity;



import tw.teddysoft.ezddd.core.entity.Entity;

import java.util.List;

public class Project implements Entity<ProjectName> {
    private final ProjectName name;
    private List<Task> tasks;

    public Project(ProjectName name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public ProjectName getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public ProjectName getId() {
        return name;
    }

    public boolean containTask(TaskId taskId) {
        return tasks.stream().anyMatch(task -> task.getId().equals(taskId));
    }
    public void setTaskDone(TaskId taskId, boolean done){
        tasks.stream().filter(task -> task.getId().equals(taskId))
                .findFirst()
                .ifPresent(task -> task.setDone(done));
    }
    public void addTask(Task task){
        tasks.add(task);
    }
}
