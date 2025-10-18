
package com.example.InternShip.service.impl;
import com.example.InternShip.dto.request.ChatMessageRequest;
import com.example.InternShip.dto.response.ChatMessageResponse;
import com.example.InternShip.dto.response.ConversationListResponse;
import com.example.InternShip.dto.response.ConversationResponse;
import com.example.InternShip.entity.ChatMessage;
import com.example.InternShip.entity.Conversation;
import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ConversationNotFoundException;
import com.example.InternShip.exception.ProgramNotFoundException;
import com.example.InternShip.exception.UnauthorizedException;
import com.example.InternShip.exception.UserNotFoundException;
import com.example.InternShip.repository.ChatMessageRepository;
import com.example.InternShip.repository.ConversationRepository;
import com.example.InternShip.repository.InternshipProgramRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final InternshipProgramRepository internshipProgramRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ConversationListResponse getConversationsForHr(String hrEmail) {
        User hrUser = userRepository.findByUsername(hrEmail)
                .orElseThrow(() -> new UserNotFoundException("HR user not found with email: " + hrEmail));

        List<Conversation> assigned = conversationRepository.findByHr(hrUser);
        List<Conversation> unassigned = conversationRepository.findByHrIsNull();

        List<ConversationResponse> assignedResponses = assigned.stream()
                .map(this::mapToConversationResponse)
                .collect(Collectors.toList());

        List<ConversationResponse> unassignedResponses = unassigned.stream()
                .map(this::mapToConversationResponse)
                .collect(Collectors.toList());

        return ConversationListResponse.builder()
                .assignedConversations(assignedResponses)
                .unassignedConversations(unassignedResponses)
                .build();
    }

    @Override
    public List<ChatMessageResponse> getMessagesForConversation(Long conversationId) {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::mapToChatMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request, String senderIdentifier) {
        User sender = userRepository.findByUsernameOrEmail(senderIdentifier)
                .orElseThrow(() -> new UserNotFoundException("Sender not found with identifier: " + senderIdentifier));

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with id: " + request.getConversationId()));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversation(conversation);
        chatMessage.setSender(sender);
        chatMessage.setContent(request.getContent());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        return mapToChatMessageResponse(savedMessage);
    }

    @Override
    public Conversation findOrCreateConversation(String candidateEmail) {
        User candidate = userRepository.findByUsername(candidateEmail)
                .orElseThrow(() -> new UserNotFoundException("Candidate not found with email: " + candidateEmail));

        return conversationRepository.findByCandidate(candidate)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setCandidate(candidate);
                    newConversation.setHr(null); // HR is null until claimed
                    Conversation savedConversation = conversationRepository.save(newConversation);

                    // Notify all HRs about the new unassigned conversation
                    messagingTemplate.convertAndSend("/topic/conversations/unassigned", mapToConversationResponse(savedConversation));

                    return savedConversation;
                });
    }

    @Override
    @Transactional
    public Conversation claimConversation(Long conversationId, String hrEmail) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with id: " + conversationId));

        if (conversation.getHr() != null) {
            // Conversation already claimed
            return conversation;
        }

        User hrUser = userRepository.findByUsername(hrEmail)
                .orElseThrow(() -> new UserNotFoundException("HR user not found with email: " + hrEmail));

        conversation.setHr(hrUser);
        Conversation claimedConversation = conversationRepository.save(conversation);

        // Notify all clients that this conversation has been claimed
        messagingTemplate.convertAndSend("/topic/conversations/claimed", mapToConversationResponse(claimedConversation));

        return claimedConversation;
    }

    @Override
    @Transactional
    public void deleteConversation(Long conversationId, String hrEmail) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with id: " + conversationId));

        User hrUser = userRepository.findByUsername(hrEmail)
                .orElseThrow(() -> new UserNotFoundException("HR user not found with email: " + hrEmail));

        // Ensure the HR user owns this conversation
        if (conversation.getHr() == null || !conversation.getHr().getId().equals(hrUser.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this conversation.");
        }

        // Delete all messages in the conversation first
        chatMessageRepository.deleteAllByConversation(conversation);

        // Then delete the conversation
        conversationRepository.delete(conversation);
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation) {
        Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findFirstByConversationIdOrderByCreatedAtDesc(conversation.getId());

        String lastMessageContent = lastMessageOpt.map(ChatMessage::getContent).orElse("No messages yet.");
        var lastMessageTimestamp = lastMessageOpt.map(ChatMessage::getCreatedAt).orElse(conversation.getCreatedAt());

        return ConversationResponse.builder()
                .id(conversation.getId())
                .candidateName(conversation.getCandidate().getFullName())
                .hr(conversation.getHr())
                .lastMessage(lastMessageContent)
                .lastMessageTimestamp(lastMessageTimestamp)
                .unreadCount(0) // Placeholder for unread count
                .build();
    }


    private ChatMessageResponse mapToChatMessageResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .conversationId(chatMessage.getConversation().getId())
                .senderId(chatMessage.getSender().getId())
                .senderName(chatMessage.getSender().getFullName())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
