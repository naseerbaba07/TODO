import { useEffect, useState } from "react";
import API from "./api";
import "./index.css";

function App() {
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
    }
  };

  loadTodos();
}, []);

  const refreshTodos = async () => {
  const res = await API.get("");
  setTodos(res.data);
};

  // ADD TODO
  const addTodo = async () => {
    if (!workname.trim()) return;

    await API.post("/save", {
      workname,
      dueDate,
      priority
    });

    setWorkname("");
    setDueDate("");
    setPriority("MEDIUM");
    refreshTodos();
  };

  // TOGGLE COMPLETE
  const toggleTodo = async (todo) => {
    await API.put(`/${todo.id}`, {
      work: !todo.work
    });
    refreshTodos();
  };

  // DELETE
  const deleteTodo = async (id) => {
    await API.delete(`/${id}`);
    refreshTodos();
  };

  // UPDATE TODO
  const updateTodo = async (id) => {
    await API.put(`/${id}`, {
      workname: editText,
      dueDate: dueDate,
      priority: priority
    });

    setEditingId(null);
    setEditText("");
    setDueDate("");
    setPriority("MEDIUM");
    refreshTodos();
  };

  // FILTER LOGIC
  const filteredTodos = todos.filter(t => {
    if (filter === "COMPLETED") return t.work;
    if (filter === "PENDING") return !t.work;
    return true;
  });

  return (
    <div className={darkMode ? "page dark" : "page"}>
      <div className="app-card">

        {/* DARK MODE TOGGLE */}
        <button
          className="dark-toggle"
          onClick={() => setDarkMode(!darkMode)}
        >
          {darkMode ? "☀ Light" : "🌙 Dark"}
        </button>

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

          <button className="add-btn" onClick={addTodo}>
            Add
          </button>
        </div>

        {/* FILTER BUTTONS */}
        <div className="filters">
          <button onClick={() => setFilter("ALL")}>All</button>
          <button onClick={() => setFilter("COMPLETED")}>Completed</button>
          <button onClick={() => setFilter("PENDING")}>Pending</button>
        </div>

        {/* TODO LIST */}
        <div className="todo-grid">
          {filteredTodos.map((t) => (
            <div className="todo-card" key={t.id}>

              <div className={`priority ${t.priority}`}>
                {t.priority}
              </div>

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
                    <button className="save-btn" onClick={() => updateTodo(t.id)}>
                      Save
                    </button>
                    <button className="delete-btn" onClick={() => setEditingId(null)}>
                      Cancel
                    </button>
                  </div>
                </>
              ) : (
                <>
                  <h3 className={`task ${t.work ? "completed" : ""}`}>
                    {t.workname}
                  </h3>

                  {t.dueDate && (
                    <p className="date">📅 Due: {t.dueDate}</p>
                  )}

                  <div className="actions">
                    <button
                      className="complete-btn"
                      onClick={() => toggleTodo(t)}
                    >
                      {t.work ? "Undo" : "Complete"}
                    </button>

                    <button
                      className="edit-btn"
                      onClick={() => {
                        setEditingId(t.id);
                        setEditText(t.workname);
                        setDueDate(t.dueDate || "");
                        setPriority(t.priority || "MEDIUM");
                      }}
                    >
                      Edit
                    </button>

                    <button
                      className="delete-btn"
                      onClick={() => deleteTodo(t.id)}
                    >
                      Delete
                    </button>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default App;