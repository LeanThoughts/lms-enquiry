import { Injectable } from '@angular/core';
import { MessageStripAlertService } from '@fundamental-ngx/core';

@Injectable({
    providedIn: 'root'
})
export class MessageService {

    constructor(private messageStripAlertService: MessageStripAlertService) {}

    /**
     * Display success message
     */
    showSuccess(message: string): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle', 
            closeOnNavigation: true,
            messageStrip: {
                duration: 7000,
                mousePersist: true,
                type: 'success',
                dismissible: true
            }
        });
    }

    /**
     * Display error message 
     */
    showError(message: string): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true, 
            messageStrip: {
                duration: 7000,
                mousePersist: true,
                type: 'error',
                dismissible: true
            }
        });
    }

    /**
     * Display warning message
     */
    showWarning(message: string): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true,
            messageStrip: {
                duration: 7000,
                mousePersist: true,
                type: 'warning',
                dismissible: true
            }
        });
    }

    /**
     * Display info message
     */
    showInfo(message: string, duration: number = 7000): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true,
            messageStrip: {
                duration: duration,
                mousePersist: true,
                type: 'information',
                dismissible: true
            }
        });
    }
}
