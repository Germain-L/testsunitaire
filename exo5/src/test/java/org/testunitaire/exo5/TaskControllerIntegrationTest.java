package org.testunitaire.exo5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testunitaire.exo5.model.Task;
import org.testunitaire.exo5.repository.TaskRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskRepository taskRepository;

	@BeforeEach
	public void setup() {
		taskRepository.deleteAll();
	}

	@Test
	public void testCreateTask() throws Exception {
		String taskJson = "{\"name\":\"Test Task\",\"description\":\"This is a test task\"}";

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(taskJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Test Task"))
				.andExpect(jsonPath("$.description").value("This is a test task"));
	}

	@Test
	public void testGetAllTasks() throws Exception {
		Task task = new Task("Sample Task", "This is a sample task");
		taskRepository.save(task);

		mockMvc.perform(get("/tasks")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Sample Task"))
				.andExpect(jsonPath("$[0].description").value("This is a sample task"));
	}

	@Test
	public void testUpdateTask() throws Exception {
		Task task = new Task("Initial Task", "Initial Description");
		task = taskRepository.save(task);

		String updatedTaskJson = "{\"name\":\"Updated Task\",\"description\":\"Updated Description\"}";

		mockMvc.perform(put("/tasks/" + task.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatedTaskJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Task"))
				.andExpect(jsonPath("$.description").value("Updated Description"));
	}

	@Test
	public void testDeleteTask() throws Exception {
		Task task = new Task("Task to delete", "This task will be deleted");
		task = taskRepository.save(task);

		mockMvc.perform(delete("/tasks/" + task.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		mockMvc.perform(get("/tasks/" + task.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testPersistenceAcrossRestarts() throws Exception {
		// Step 1: Create a task and save it
		Task task = new Task("Persistent Task", "This task should persist");
		taskRepository.save(task);

		// Simulate application restart by closing and reopening the context
		SpringApplication.run(TaskManagerApplication.class);

		// Step 2: Fetch the task to check if it still exists
		mockMvc.perform(get("/tasks")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Persistent Task"))
				.andExpect(jsonPath("$[0].description").value("This task should persist"));
	}
}
