function TodoItem({ todo, deleteTodo }) {
  return (
    <li className="todoItem">
      <span>{todo.workname}</span>
      <button className="deleteBtn" onClick={() => deleteTodo(todo.id)}>
        Delete
      </button>
    </li>
  );
}

export default TodoItem;