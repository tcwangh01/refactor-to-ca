package tw.teddysoft.tasks.usecase.service;

import tw.teddysoft.ezddd.core.usecase.Input;

public class SetDoneInput implements Input {
    public String toDoListId;
    public String taskId;
    public boolean done;
}
