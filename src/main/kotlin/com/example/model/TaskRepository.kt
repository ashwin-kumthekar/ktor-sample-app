package com.example.model



object TaskRepository{
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

//    fun allTasks(): List<Task> = tasks

    fun allTasks(): List<Task> {
        println("Returning all tesks from TaskRepository")
        return tasks
    }

    fun taskByPriority(priority: Priority) = tasks.filter{it.priority ==priority}
    fun taskByName(name:String) = tasks.filter{it.name.equals(name,ignoreCase = true)}

    fun addTask(task:Task){
        if(taskByName(task.name)!=null){
            throw IllegalArgumentException("Task with name ${task.name} already exists")
        }
        tasks.add(task)
    }
}