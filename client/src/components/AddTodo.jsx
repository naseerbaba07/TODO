import { useState } from "react";

function AddTodo({ addTodo }) {
  const [workname, setWorkname] = useState("");

  const handleSubmit = () => {
    if (!workname.trim()) return;
    addTodo(workname);
    setWorkname("");
  };

  return (
    <div className="inputBox">
      <input
        type="text"
        placeholder="Enter new task..."
        value={workname}
        onChange={(e) => setWorkname(e.target.value)}
      />
      <button onClick={handleSubmit}>Add</button>
    </div>
  );
}

export default AddTodo;