import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const SOCKET_URL = import.meta.env.VITE_SOCKET_URL;

class WebSocketService {
    private stompClient: Client | null = null;
    private subscriptions: Map<string, any> = new Map();
    private connectionPromise: Promise<void> | null = null;

    connect(): Promise<void> {
        if (this.stompClient && this.stompClient.active) {
            return Promise.resolve();
        }

        if (!this.connectionPromise) {
            this.connectionPromise = new Promise((resolve, reject) => {
                const token = localStorage.getItem("accessToken");
                const connectHeaders = token ? { Authorization: `Bearer ${token}` } : {};

                this.stompClient = new Client({
                    webSocketFactory: () => new SockJS(SOCKET_URL),
                    connectHeaders: connectHeaders,
                    reconnectDelay: 5000,
                    heartbeatIncoming: 4000,
                    heartbeatOutgoing: 4000,
                    onConnect: () => {
                        console.log('WebSocket connected');
                        resolve();
                    },
                    onStompError: (frame) => {
                        console.error('Broker reported error: ' + frame.headers['message']);
                        console.error('Additional details: ' + frame.body);
                        reject(frame);
                    },
                });

                this.stompClient.activate();
            });
        }
        return this.connectionPromise;
    }

    async subscribe(topic: string, callback: (message: any) => void) {
        if (!this.stompClient || !this.stompClient.active) {
            await this.connect();
        }

        if (this.subscriptions.has(topic)) {
            return;
        }

        if (this.stompClient && this.stompClient.active) {
            const subscription = this.stompClient.subscribe(topic, (message) => {
                callback(JSON.parse(message.body));
            });
            this.subscriptions.set(topic, subscription);
            console.log(`Subscribed to ${topic}`);
        } else {
            console.error("Cannot subscribe, STOMP client is not active.");
        }
    }

    unsubscribe(topic: string) {
        if (this.subscriptions.has(topic)) {
            this.subscriptions.get(topic).unsubscribe();
            this.subscriptions.delete(topic);
            console.log(`Unsubscribed from ${topic}`);
        }
    }

    sendMessage(destination: string, body: any) {
        if (this.stompClient && this.stompClient.active) {
            this.stompClient.publish({ destination, body: JSON.stringify(body) });
        } else {
            console.error("Cannot send message, STOMP client is not active.");
        }
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.deactivate();
            this.stompClient = null;
            this.connectionPromise = null;
            this.subscriptions.clear();
            console.log('WebSocket disconnected');
        }
    }
}

const webSocketService = new WebSocketService();
export default webSocketService;
