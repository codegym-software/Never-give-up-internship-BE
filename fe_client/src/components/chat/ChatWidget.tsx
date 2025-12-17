import React, { useState, useEffect, useRef } from "react";
import { MessageSquare, X, Send, Loader2 } from "lucide-react";
import {
  findOrCreateConversationApi,
  getMessagesApi,
  findOrCreateGuestConversationApi,
} from "../../api/chatApi";
import webSocketService from "../../api/webSocketService";
import { useAuth } from "../../context/AuthContext";
import { v4 as uuidv4 } from "uuid";
import "./ChatWidget.css";

export const ChatWidget = () => {
  const { user } = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const [conversationId, setConversationId] = useState<number | null>(null);
  const [messages, setMessages] = useState<any[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [guestId, setGuestId] = useState<string | null>(null);
  const messagesEndRef = useRef<null | HTMLDivElement>(null);

  useEffect(() => {
    if (!user) {
      let currentGuestId = localStorage.getItem("chatGuestId");
      if (!currentGuestId) {
        currentGuestId = uuidv4();
        localStorage.setItem("chatGuestId", currentGuestId);
      }
      setGuestId(currentGuestId);
    }
  }, [user]);

  useEffect(() => {
    if (isOpen && !conversationId) {
      const initConversation = async () => {
        setIsLoading(true);
        try {
          let response;
          if (user) {
            response = await findOrCreateConversationApi();
          } else if (guestId) {
            response = await findOrCreateGuestConversationApi(guestId);
          }
          if (response) {
            setConversationId(response.data.conversationId);
          }
        } catch (error) {
          console.error("Failed to init conversation", error);
        } finally {
          setIsLoading(false);
        }
      };
      initConversation();
    }
  }, [isOpen, conversationId, user, guestId]);

  useEffect(() => {
    if (!conversationId) return;

    const setupConnection = async () => {
      setIsLoading(true);
      try {
        const messagesResponse = await getMessagesApi(conversationId);
        setMessages(messagesResponse.data);

        await webSocketService.connect();
        const topic = `/topic/conversation/${conversationId}`;
        webSocketService.subscribe(topic, (newMessage) => {
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        });
      } catch (error) {
        console.error("Failed to setup connection or fetch messages", error);
      } finally {
        setIsLoading(false);
      }
    };

    setupConnection();

    return () => {
      const topic = `/topic/conversation/${conversationId}`;
      webSocketService.unsubscribe(topic);
    };
  }, [conversationId]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  const handleSendMessage = () => {
    if (newMessage.trim() && conversationId) {
      const payload: any = {
        conversationId: conversationId,
        content: newMessage.trim(),
      };

      if (!user && guestId) {
        payload.guestId = guestId;
      }

      webSocketService.sendMessage("/app/chat.sendMessage", payload);
      setNewMessage("");
    }
  };

  if (!isOpen) {
    return (
      <button title="Chat" onClick={toggleChat} className="chat-widget-button">
        <MessageSquare className="chat-widget-icon" />
      </button>
    );
  }

  return (
    <div className="chat-widget-window">
      <div className="chat-widget-header">
        <h3>Chat với chúng tôi</h3>
        <button title="Đóng chat" onClick={toggleChat}>
          <X size={20} />
        </button>
      </div>
      <div className="chat-widget-body">
        {!localStorage.getItem("accessToken") ? (
          <div className="flex flex-col items-center justify-center h-full px-4 text-center">
            <p className="text-gray-600 text-sm leading-relaxed">
              Bạn cần{" "}
              <span className="font-semibold text-gray-800">đăng nhập </span>
              để sử dụng tính năng chat hỗ trợ.
            </p>
          </div>
        ) : isLoading ? (
          <Loader2 className="animate-spin self-center mt-4" />
        ) : (
          messages.map((msg, index) => {
            const isSentByMe =
              (user && user.id === msg.senderId) ||
              (guestId && guestId === msg.guestId);

            return (
              <div
                key={index}
                className={`message ${isSentByMe ? "sent" : "received"}`}
              >
                {msg.content}
              </div>
            );
          })
        )}
        <div ref={messagesEndRef} />
      </div>

      <div className="chat-widget-footer">
        {!localStorage.getItem("accessToken") ? (
          <div className="w-full cursor-not-allowed">
            <input
              type="text"
              disabled
              placeholder="Vui lòng đăng nhập để sử dụng chat"
              className="opacity-60 w-full cursor-not-allowed"
            />
          </div>
        ) : (
          <>
            <input
              type="text"
              placeholder="Nhập tin nhắn..."
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              onKeyPress={(e) => e.key === "Enter" && handleSendMessage()}
            />
            <button title="Gửi tin nhắn" onClick={handleSendMessage}>
              <Send size={20} />
            </button>
          </>
        )}
      </div>
    </div>
  );
};
