package kirill.kopienko.springbootcrud.repository;

import kirill.kopienko.springbootcrud.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query(value = "SELECT * FROM Task LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Task> findAll(int limit, int offset);
}
