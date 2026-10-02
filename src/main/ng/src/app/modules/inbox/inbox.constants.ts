// Tasks waiting for longer than this many days are highlighted as overdue
export const TASK_OVERDUE_DAYS = 7;

const PROCESS_LABELS: Record<string, string> = {
    'BusinessPartner': 'Business Partner',
    'ReferenceInterestRateValue': 'Reference Interest Rate',
};

const PROCESS_COLORS: Record<string, number> = {
    'Process Enquiry': 1,
    'ICC In-Principal Approval': 2,
    'Prelim Risk Assessment': 3,
    'Application Fee': 4,
    'Board Approval': 5,
    'Sanction': 6,
    'BusinessPartner': 7,
    'ReferenceInterestRateValue': 8,
};

/**
 * Display label of a workflow process name
 */
export function getProcessLabel(processName: string): string {
    return PROCESS_LABELS[processName] || processName;
}

/**
 * Badge color index (1 - 8) of a workflow process name
 */
export function getProcessColor(processName: string): number {
    return PROCESS_COLORS[processName] || 8;
}

/**
 * Number of whole days between the request date and today
 */
export function getTaskAgeInDays(requestDate: string): number {
    const requested = new Date(requestDate);
    if (isNaN(requested.getTime())) {
        return 0;
    }
    const today = new Date();
    const startOfDay = (date: Date) => new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
    return Math.max(0, Math.round((startOfDay(today) - startOfDay(requested)) / 86400000));
}

/**
 * Relative age of a task, e.g. "Today" or "3 days ago"
 */
export function getTaskAgeLabel(requestDate: string): string {
    if (!requestDate || isNaN(new Date(requestDate).getTime())) {
        return '';
    }
    const days = getTaskAgeInDays(requestDate);
    return days === 0 ? 'Today' : days === 1 ? '1 day ago' : days + ' days ago';
}
