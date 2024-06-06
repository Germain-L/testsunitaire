package org.testunitaire.exo5.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.testunitaire.exo5.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
