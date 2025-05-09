import { check, signout } from "./auth.js"

const base_url = 'http://localhost:8081'

document.onreadystatechange = () => {
    setInterval(check, 10000);
    $("#signout-b").click(signout);
    $("#add-task").click(addTask);
    $("#new-task").on('keyup', filterTasks);
    loadTasks();
}

const addTask = () => {
    const description = $('#new-task').val();
    const body = `{"description": "${description}"}`;

    $.ajax({
        type: 'POST',
        url: `${base_url}/task`,
        headers: {"token": localStorage.getItem("token")},
        contentType: 'application/json',
        data: body,
        success: (res) => { loadTasks(); }
    });
}

const loadTasks = () => {
    $.ajax({
        type: 'GET',
        url: `${base_url}/task`,
        headers: {"token": localStorage.getItem("token")},
        contentType: 'application/json',
        dataType: 'json',
        success: (response) => { renderTasks(response) }
    });
}

const renderTasks = (tasks) => {
    console.log(tasks)
    const ul = $("<ul>")
    tasks.forEach(e => {
       
        const li = $("<li>")
        const lbl = $(`<span class="${e.status == 'DONE' ? 'task-done':'task-pending'}">${e.description}</span>`)
        lbl.click(() => toggleTask(e))
        ul.append(li.append(lbl))
    })

    $("#tasks>ul").empty();
    $("#tasks").append(ul);
}

const toggleTask = (task) => {
    task.status = task.status == "DONE" ? "PENDING" : "DONE";

    $.ajax({
        type: 'PUT',
        url: `${base_url}/task/${task.id}`,
        headers: {"token": localStorage.getItem("token")},
        contentType: 'application/json',
        data: JSON.stringify(task),
        success: (res) => { loadTasks(); }
    });
}

const filterTasks = () => {
    const query = $("#new-task").val()

    $.ajax({
        type: 'GET',
        url: `${base_url}/task/search?q=${query}`,
        headers: {"token": localStorage.getItem("token")},
        contentType: 'application/json',
        dataType: 'json',
        success: (response) => { renderTasks(response); }
    });
}