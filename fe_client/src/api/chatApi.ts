import axiosClient from "./axiosClient";

const API_URL = '/chat';

export const findOrCreateConversationApi = () => {
  return axiosClient.post(`${API_URL}/initiate`);
};

export const findOrCreateGuestConversationApi = (guestId: string) => {
  return axiosClient.post(`${API_URL}/guest/conversation`, { guestId });
};

export const getMessagesApi = (conversationId: number) => {
    return axiosClient.get(`/api/v1/conversations/${conversationId}/messages`);
};
