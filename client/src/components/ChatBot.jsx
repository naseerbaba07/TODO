import { useState } from "react";
import axios from "axios";
import { CHAT_API } from "../api";
import { toast } from "react-toastify";
import "../styles/ChatBot.css";

function ChatBot() {
  const [open, setOpen] = useState(false);

  const [message, setMessage] = useState("");

  const [messages, setMessages] = useState([
    {
      sender: "bot",
      text: "Hi! 👋 I'm your BabaList AI assistant. You can ask me to create, update, complete, or manage your tasks."
    }
  ]);

  const [loading, setLoading] = useState(false);

  const [confirmation, setConfirmation] = useState(null);

  const sendMessage = async () => {
    const text = message.trim();

    if (!text || loading) {
      return;
    }

    setMessages((prev) => [
      ...prev,
      {
        sender: "user",
        text
      }
    ]);

    setMessage("");
    setLoading(true);

    try {
      const response = await axios.post(CHAT_API, {
        message: text
      });

      const data = response.data;

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: data.message
        }
      ]);

      if (data.requiresConfirmation) {
        setConfirmation({
          action: data.action,
          todoId: data.todoId
        });
      }

      /*
       * Tell Todo.jsx that something changed.
       */
      if (
        data.action === "CREATE" ||
        data.action === "UPDATE" ||
        data.action === "COMPLETE"
      ) {
        window.dispatchEvent(
          new Event("todo-updated")
        );
      }

    } catch (error) {
      console.error("Chat error:", error);

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: "Sorry, I couldn't connect to the server."
        }
      ]);

      toast.error("Chatbot request failed!");
    } finally {
      setLoading(false);
    }
  };

  const confirmDelete = async () => {
    if (!confirmation?.todoId) {
      return;
    }

    try {
      /*
       * IMPORTANT:
       * Your backend confirmation endpoint is:
       *
       * DELETE /chat/{id}
       */
      await axios.delete(
        `${CHAT_API}/${confirmation.todoId}`
      );

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: "Task deleted successfully. 🗑️"
        }
      ]);

      setConfirmation(null);

      window.dispatchEvent(
        new Event("todo-updated")
      );

      toast.success("Task deleted!");
    } catch (error) {
      console.error("Delete error:", error);

      toast.error("Failed to delete task.");
    }
  };

  const confirmClearAll = async () => {
    try {
      await axios.delete(
        `${CHAT_API}/clear`
      );

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: "All tasks have been cleared. 🗑️"
        }
      ]);

      setConfirmation(null);

      window.dispatchEvent(
        new Event("todo-updated")
      );

      toast.success("All tasks cleared!");
    } catch (error) {
      console.error("Clear error:", error);

      toast.error("Failed to clear tasks.");
    }
  };

  const cancelConfirmation = () => {
    setMessages((prev) => [
      ...prev,
      {
        sender: "bot",
        text: "Okay, I cancelled that action. 👍"
      }
    ]);

    setConfirmation(null);
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      sendMessage();
    }
  };

  return (
    <>
      {!open && (
        <button
  className="chatbot-floating-button"
  onClick={() => setOpen(true)}
  aria-label="Open BabaList AI"
>
  <span className="gemini-sparkle">
    <span className="sparkle-main">🤖</span>
  </span>
</button>
      )}

      {open && (
        <div className="chatbot-window">

          {/* HEADER */}
         <div className="chatbot-header">

  <div className="chatbot-title">

    <div className="chatbot-avatar">
      <span>🌐</span>
    </div>

    <div>
      <h3>BabaList AI</h3>

      <div className="ai-status">
        <span className="status-dot"></span>
        Gemini AI
      </div>
    </div>

  </div>

  <button
    className="chatbot-close"
    onClick={() => setOpen(false)}
    aria-label="Close chatbot"
  >
    ❌
  </button>

</div>

          {/* MESSAGES */}
          <div className="chatbot-messages">

            {messages.map((item, index) => (
              <div
                key={index}
                className={
                  item.sender === "user"
                    ? "chat-message user"
                    : "chat-message bot"
                }
              >
                {item.text}
              </div>
            ))}

            {loading && (
              <div className="chat-message bot typing">
                <span>.</span>
                <span>.</span>
                <span>.</span>
              </div>
            )}

          </div>

          {/* CONFIRMATION */}
          {confirmation && (
            <div className="chatbot-confirmation">

              <p>
                Are you sure?
              </p>

              <div className="confirmation-buttons">

                {confirmation.action === "DELETE" && (
                  <button
                    className="confirm-delete"
                    onClick={confirmDelete}
                  >
                    Yes, delete
                  </button>
                )}

                {confirmation.action === "CLEAR_ALL" && (
                  <button
                    className="confirm-delete"
                    onClick={confirmClearAll}
                  >
                    Yes, delete all
                  </button>
                )}

                <button
                  className="confirm-cancel"
                  onClick={cancelConfirmation}
                >
                  Cancel
                </button>

              </div>

            </div>
          )}

          {/* INPUT */}
          <div className="chatbot-input-area">

            <input
              type="text"
              placeholder="Ask BabaList AI..."
              value={message}
              onChange={(e) =>
                setMessage(e.target.value)
              }
              onKeyDown={handleKeyDown}
              disabled={loading}
            />

            <button
              onClick={sendMessage}
              disabled={
                loading ||
                !message.trim()
              }
              aria-label="Send message"
            >
              ➤
            </button>

          </div>

        </div>
      )}
    </>
  );
}

export default ChatBot;