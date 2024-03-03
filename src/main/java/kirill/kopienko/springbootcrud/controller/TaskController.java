package kirill.kopienko.springbootcrud.controller;

import kirill.kopienko.springbootcrud.domain.Task;
import kirill.kopienko.springbootcrud.dto.TaskDTO;
import kirill.kopienko.springbootcrud.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                        @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                        @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {
        List<Task> listToShow = taskService.findAll(limit, offset);
        model.addAttribute("tasksList", listToShow);
        return "tasks";
    }

    @PostMapping("/{id}")
    public String edit(Model model,
                       @PathVariable("id") int id,
                       @RequestBody TaskDTO taskDTO) {
        if (isNull(id) || id <= 0 ) {
            throw new RuntimeException("Invalid id!");
        }

        Task edit = taskService.edit(id, taskDTO.getDescription(), taskDTO.getStatus());
        return tasks(model, 1, 10);
    }

    @PostMapping("/")
    public String add(Model model,
                      @RequestBody TaskDTO taskDTO) {
        Task task = taskService.create(taskDTO.getDescription(), taskDTO.getStatus());
        return tasks(model, 1, 10);
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable("id") int id) {
        if (isNull(id) || id <= 0 ) {
            throw new RuntimeException("Invalid id!");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);
    }
}
