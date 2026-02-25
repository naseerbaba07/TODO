import { useEffect, useState } from "react";
import API from "../api";
import "../styles/todo.css";
import Button from "@mui/material/Button";
import Chip from "@mui/material/Chip";

// Toastify
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

// Icons
import { FaTrash, FaEdit, FaPlus } from "react-icons/fa";

// Date handling
import dayjs from "dayjs";

function Todo() {
  const [todos, setTodos] = useState([]);
  const [workname, setWorkname] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [editText, setEditText] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [priority, setPriority] = useState("MEDIUM");
  const [filter, setFilter] = useState("ALL");
  const [darkMode, setDarkMode] = useState(false);


  

  useEffect(() => {
    const loadTodos = async () => {
      try {
        const res = await API.get("");
        setTodos(res.data);
      } catch (err) {
        console.error(err);
        toast.error("Failed to load tasks!");
      }
    };
    loadTodos();
  }, []);

  const refreshTodos = async () => {
    try {
      const res = await API.get("");
      setTodos(res.data);
    } catch {
      toast.error("Failed to refresh tasks!");
    }
  };

  const addTodo = async () => {
    if (!workname.trim()) return;
    try {
      await API.post("/save", { workname, dueDate, priority });
      setWorkname("");
      setDueDate("");
      setPriority("MEDIUM");
      refreshTodos();
      toast.success("Task added successfully!");
    } catch {
      toast.error("Failed to add task!");
    }
  };

  const toggleTodo = async (todo) => {
    try {
      await API.put(`/${todo.id}`, { work: !todo.work });
      refreshTodos();
      toast.success(todo.work ? "Task marked as pending!" : "Task completed!");
    } catch {
      toast.error("Failed to update task status!");
    }
  };

  const deleteTodo = async (id) => {
    try {
      await API.delete(`/${id}`);
      refreshTodos();
      toast.success("Task deleted!");
    } catch {
      toast.error("Failed to delete task!");
    }
  };

  const clearAllTodos = async () => {
    if (todos.length === 0) {
    toast.info("No tasks are there to clear");
    return;
  }
    try {
      await API.delete("/clear"); // assumes backend supports clearing all
      setTodos([]);
      toast.success("All tasks cleared!");
    } catch {
      toast.error("Failed to clear tasks!");
    }
  };

  const updateTodo = async (id) => {
    try {
      await API.put(`/${id}`, { workname: editText, dueDate, priority });
      setEditingId(null);
      setEditText("");
      setDueDate("");
      setPriority("MEDIUM");
      refreshTodos();
      toast.success("Task updated!");
    } catch {
      toast.error("Failed to update task!");
    }
  };

  const filteredTodos = todos.filter((t) => {
    if (filter === "COMPLETED") return t.work;
    if (filter === "PENDING") return !t.work;
    return true;
  });

  return (
    <>
      <ToastContainer position="top-right" />

      <div className={darkMode ? "page dark" : "page"}>
        <div className="app-card">
          {/* DARK MODE TOGGLE */}
          <Button
            variant="outlined"
            color="secondary"
            onClick={() => setDarkMode(!darkMode)}
          >
            {darkMode ? "☀ Light" : "🌙 Dark"}
          </Button>

          <h1 className="title">Work List</h1>

          {/* ADD TASK */}
          <div className="add-box">
            <input
              placeholder="Task..."
              value={workname}
              onChange={(e) => setWorkname(e.target.value)}
            />
            <input
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
            />
            <select
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
            >
              <option value="HIGH">🔴 High</option>
              <option value="MEDIUM">🟡 Medium</option>
              <option value="LOW">🟢 Low</option>
            </select>

            <Button
              variant="contained"
              color="primary"
              onClick={addTodo}
              startIcon={<FaPlus />}
            >
              Add Task
            </Button>
          </div>

          {/* FILTER CHIPS */}
          <div className="filters">

  <Button
    variant={filter === "ALL" ? "contained" : "outlined"}
    color="primary"
    className="filter-btn"
    onClick={() => setFilter("ALL")}
  >
    All
  </Button>

  <Button
    variant={filter === "COMPLETED" ? "contained" : "outlined"}
    color="success"
    className="filter-btn"
    onClick={() => setFilter("COMPLETED")}
  >
    Completed
  </Button>

  <Button
    variant={filter === "PENDING" ? "contained" : "outlined"}
    color="warning"
    className="filter-btn"
    onClick={() => setFilter("PENDING")}
  >
    Pending
  </Button>

  <Button
    variant="contained"
    color="error"
    className="filter-btn"
    startIcon={<FaTrash />}
    onClick={clearAllTodos}
  >
    Clear All
  </Button>

</div>

          {/* TODO LIST */}
          <div className="todo-grid">
            {filteredTodos.map((t) => {
              const isOverdue =
                t.dueDate && dayjs(t.dueDate).isBefore(dayjs());

              return (
                <div
                  className={`todo-card ${t.priority.toUpperCase()} ${
                    t.completed ? "done" : ""
                  }`}
                  key={t.id}
                >
                  {/* Priority Chip */}
                  <Chip
                    label={t.priority}
                    color={
                      t.priority === "HIGH"
                        ? "error"
                        : t.priority === "MEDIUM"
                        ? "warning"
                        : "success"
                    }
                    variant="outlined"
                    size="small"
                  />

                  {editingId === t.id ? (
                    <>
                      <input
                        className="edit-input"
                        value={editText}
                        onChange={(e) => setEditText(e.target.value)}
                      />
                      <input
                        type="date"
                        className="edit-date"
                        value={dueDate}
                        onChange={(e) => setDueDate(e.target.value)}
                      />
                      <select
                        value={priority}
                        onChange={(e) => setPriority(e.target.value)}
                      >
                        <option value="HIGH">🔴 High</option>
                        <option value="MEDIUM">🟡 Medium</option>
                        <option value="LOW">🟢 Low</option>
                      </select>

                      <div className="actions">
                        <Button
                          variant="contained"
                          color="primary"
                          onClick={() => updateTodo(t.id)}
                        >
                          Save
                        </Button>
                        <Button
                          variant="outlined"
                          color="secondary"
                          onClick={() => setEditingId(null)}
                        >
                          Cancel
                        </Button>
                      </div>
                    </>
                  ) : (
                    <>
                      <h3 className={`task ${t.work ? "completed" : ""}`}>
                        {t.workname}
                      </h3>

                      {t.dueDate && (
                        <p className="date">
                          📅 Due: {dayjs(t.dueDate).format("DD MMM YYYY")}
                          {isOverdue && (
                            <span style={{ color: "red", marginLeft: "8px" }}>
                              Overdue
                            </span>
                          )}
                        </p>
                      )}

                      <div className="actions">

  <div className="top-actions">
    <Button
      variant="outlined"
      color="error"
      startIcon={<FaTrash />}
      onClick={() => deleteTodo(t.id)}
    >
      Delete
    </Button>

    <Button
      variant="outlined"
      color="secondary"
      startIcon={<FaEdit />}
      onClick={() => {
        setEditingId(t.id);
        setEditText(t.workname);
        setDueDate(t.dueDate || "");
        setPriority(t.priority || "MEDIUM");
      }}
    >
      Edit
    </Button>
  </div>

  <Button
    className="complete-btn"
    variant="contained"
    color="success"
    onClick={() => toggleTodo(t)}
  >
    {t.work ? "Undo " : "Complete "}
  </Button>

</div>
                    </>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </>
  );
}

export default Todo;