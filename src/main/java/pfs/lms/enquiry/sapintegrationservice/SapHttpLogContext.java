package pfs.lms.enquiry.sapintegrationservice;

/**
 * Business context of the SAP calls made on the current thread, so that the log can say which process, component
 * and portal object a request belonged to. Callers set it around their gateway calls:
 * <pre>
 * try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("CollateralManagement", "Collateral Documents",
 *         documentId, pointerId)) {
 *     gateway.post(…);
 * }
 * </pre>
 * Calls made without a context are still logged (with the calling class and method found from the stack).
 */
public final class SapHttpLogContext {

    /** What the thread is doing; all fields optional. */
    public static final class Scope implements AutoCloseable {
        final String businessProcess;
        final String subProcess;
        final String businessObjectId;
        final Long pointerId;
        private final Scope previous;

        private Scope(String businessProcess, String subProcess, String businessObjectId, Long pointerId, Scope previous) {
            this.businessProcess = businessProcess;
            this.subProcess = subProcess;
            this.businessObjectId = businessObjectId;
            this.pointerId = pointerId;
            this.previous = previous;
        }

        public String getBusinessProcess() { return businessProcess; }
        public String getSubProcess() { return subProcess; }
        public String getBusinessObjectId() { return businessObjectId; }
        public Long getPointerId() { return pointerId; }

        @Override
        public void close() {
            if (CURRENT.get() == this) {
                if (previous == null) {
                    CURRENT.remove();
                } else {
                    CURRENT.set(previous);
                }
            }
        }
    }

    private static final ThreadLocal<Scope> CURRENT = new ThreadLocal<>();

    private SapHttpLogContext() {
    }

    /** Opens a context for the SAP calls that follow on this thread; close it (try-with-resources) when done. */
    public static Scope open(String businessProcess, String subProcess, Object businessObjectId, Long pointerId) {
        Scope scope = new Scope(businessProcess, subProcess,
                businessObjectId == null ? null : businessObjectId.toString(), pointerId, CURRENT.get());
        CURRENT.set(scope);
        return scope;
    }

    /** The context of the current thread, or null. */
    public static Scope current() {
        return CURRENT.get();
    }
}
