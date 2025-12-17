import React, { createContext, useState, useContext, ReactNode, useMemo } from 'react';

interface ChatContextType {
    programId: number | null;
    setProgramId: (id: number | null) => void;
}

const ChatContext = createContext<ChatContextType | undefined>(undefined);

export const ChatProvider = ({ children }: { children: ReactNode }) => {
    const [programId, setProgramId] = useState<number | null>(null);

    const value = useMemo(() => ({ programId, setProgramId }), [programId]);

    return (
        <ChatContext.Provider value={value}>
            {children}
        </ChatContext.Provider>
    );
};

export const useChat = () => {
    const context = useContext(ChatContext);
    if (context === undefined) {
        throw new Error('useChat must be used within a ChatProvider');
    }
    return context;
};
