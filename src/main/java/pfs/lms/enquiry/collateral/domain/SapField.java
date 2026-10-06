package pfs.lms.enquiry.collateral.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The SAP column a portal field corresponds to. Used to map the SAP collateral tables in the migration API and,
 * later, in the SAP integration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SapField {

    /** SAP column name, e.g. "ZID_NO". Empty for SAP long texts (kept in the SAP text editor) and for portal-only fields. */
    String value();

    /** True for SAP long texts (text editor objects) that have no column in the SAP table. */
    boolean longText() default false;
}
