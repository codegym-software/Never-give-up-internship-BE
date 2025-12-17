import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useChat } from '../context/ChatContext';

export const ProgramDetailPage = () => {
    const { programId } = useParams<{ programId: string }>();
    const { setProgramId } = useChat();

    useEffect(() => {
        if (programId) {
            setProgramId(Number(programId));
        }

        // Cleanup function to reset the programId when the component unmounts
        return () => {
            setProgramId(null);
        };
    }, [programId, setProgramId]);

    return (
        <div style={{ padding: '100px 20px', textAlign: 'center' }}>
            <h1>Chi tiết Chương trình Thực tập</h1>
            <p>Program ID: {programId}</p>
            <p>Bây giờ bạn có thể mở widget chat ở góc dưới bên phải để bắt đầu cuộc trò chuyện.</p>
        </div>
    );
};