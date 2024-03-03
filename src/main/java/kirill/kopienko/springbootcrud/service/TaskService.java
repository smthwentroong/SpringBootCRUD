package kirill.kopienko.springbootcrud.service;

import jakarta.transaction.Transactional;
import kirill.kopienko.springbootcrud.domain.Status;
import kirill.kopienko.springbootcrud.domain.Task;
import kirill.kopienko.springbootcrud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(int limit, int offset) {
        return taskRepository.findAll(limit, offset);
    }

    public int getAllCount() {
        return Math.toIntExact(taskRepository.count());
    }

    public Task edit(int id, String description, Status status) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id!");
        }
        Task task = taskRepository.getReferenceById(id);
        task.setDescription(description);
        task.setStatus(status);
        taskRepository.save(task);
        return task;
    }

    public Task create(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskRepository.save(task);
        return task;
    }

    public void delete(int id) {
        Task task = taskRepository.getReferenceById(id);
        if (isNull(task)) {
            throw new RuntimeException("No matching task in repository!");
        }
        taskRepository.delete(task);
    }
}
