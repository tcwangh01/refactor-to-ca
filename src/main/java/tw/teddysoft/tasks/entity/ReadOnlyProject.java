package tw.teddysoft.tasks.entity;

import java.util.List;

public class ReadOnlyProject extends Project{
    private final Project real;

    public ReadOnlyProject(Project real) {
        super(real.getName(), real.getTasks());
        this.real = real;
    }

    @Override
    public List<Task> getTasks() {
        return real.getTasks().stream().map( task -> (Task) new ReadOnlyTask(task)).toList();
    }

}
