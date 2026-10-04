import { Injectable } from '@angular/core';
import { MessageStripAlertRef, MessageStripAlertService } from '@fundamental-ngx/core';

type MessageType = 'success' | 'error' | 'warning' | 'information';

@Injectable({
    providedIn: 'root'
})
export class MessageService {

    private readonly openMessages = new Set<MessageStripAlertRef>();

    constructor(private messageStripAlertService: MessageStripAlertService) {}

    /**
     * Display success message
     */
    showSuccess(message: string): void {
        this.open(message, 'success');
    }

    /**
     * Display error message, replacing any messages still on screen
     */
    showError(message: string): void {
        this.clear();
        this.open(message, 'error');
    }

    /**
     * Display warning message
     */
    showWarning(message: string): void {
        this.open(message, 'warning');
    }

    /**
     * Display info message
     */
    showInfo(message: string, duration: number = 7000): void {
        this.open(message, 'information', duration);
    }

    /**
     * Dismiss all messages still on screen
     */
    clear(): void {
        Array.from(this.openMessages).forEach(messageRef => messageRef.dismiss());
        this.openMessages.clear();
    }

    private open(message: string, type: MessageType, duration: number = 7000): void {
        const messageRef = this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true,
            messageStrip: {
                duration: duration,
                mousePersist: true,
                type: type,
                dismissible: true
            }
        });
        this.openMessages.add(messageRef);
        messageRef.onDismiss$.subscribe(() => this.openMessages.delete(messageRef));
    }
}
