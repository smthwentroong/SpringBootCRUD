package kirill.kopienko.springbootcrud.controller;

import kirill.kopienko.springbootcrud.domain.Task;
import kirill.kopienko.springbootcrud.dto.TaskDTO;
import kirill.kopienko.springbootcrud.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        List<Task> listToShow = taskService.findAll(limit, (page - 1) * limit);
        model.addAttribute("tasksList", listToShow);
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
