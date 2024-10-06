package com.example.devsyncss.service;


import com.example.devsyncss.entities.Task;

public class TaskService {
//    @Autowired
//    private TaskRepository taskRepository;
//
//    public List<Task> getOverdueTasks() {
//        return taskRepository.findOverdueTasks();
//    }
//
//    public List<Task> getUpcomingTasks(int days) {
//        LocalDateTime endDate = LocalDateTime.now().plusDays(days);
//        return taskRepository.findTasksDueSoon(endDate);
//    }
//
//    public Map<Task.TaskStatus, Long> getTaskStatusCountForUser(User user) {
//        List<Object[]> result = taskRepository.countTasksByStatusForUser(user);
//        return result.stream()
//                .collect(Collectors.toMap(
//                        row -> (Task.TaskStatus) row[0],
//                        row -> (Long) row[1]
//                ));
//    }
//
//    public List<Task> searchTasks(String searchTerm) {
//        return taskRepository.searchTasks(searchTerm);
//    }
//
//    public Double getUserProductivityRate(User user, LocalDateTime startDate, LocalDateTime endDate) {
//        return taskRepository.getTaskCompletionRate(user, startDate, endDate);
//    }
//
//    public List<Task> getStagnantTasks(int daysWithoutUpdate) {
//        LocalDateTime stagnantDate = LocalDateTime.now().minusDays(daysWithoutUpdate);
//        return taskRepository.findStagnantTasks(stagnantDate);
//    }
}