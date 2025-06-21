package tw.teddysoft.tasks.usecase.service;

import tw.teddysoft.ezddd.cqrs.usecase.CqrsOutput;
import tw.teddysoft.ezddd.cqrs.usecase.command.Command;

public interface SetDoneUseCase extends Command<SetDoneInput, CqrsOutput> {
}
