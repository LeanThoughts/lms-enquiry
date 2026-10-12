*&---------------------------------------------------------------------*
*& ZCL_ZPFS_LMS_COLLATERAL_DPC_EXT -> CHECKLISTHEADERS_CREATE_ENTITY
*& NetWeaver 7.30 / 7.31 syntax (no inline declarations, no @ host
*& variables, no string templates).
*&---------------------------------------------------------------------*
*& Redefinition of the SEGW-generated create for entity set
*& ChecklistHeaderSet (entity type ChecklistHeader, key ZidNo).
*&
*& Called by the portal batch pfs.lms.enquiry.collateral.batch
*& (CollateralSapIntegrationService) with
*&   POST /sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/ChecklistHeaderSet
*& and a JSON body of ChecklistHeader properties. It writes one row of
*& the collateral checklist table ZPFS_T_LN_CHKLST and returns the
*& stored row as ER_ENTITY (HTTP 201).
*&
*& Behaviour
*&  - ZLOAN_NUMBER (loan contract, CHAR13) and ZID_NO (checklist id,
*&    CHAR10) are mandatory -> HTTP 400 with a message otherwise.
*&  - ZID_NO is normalised to its 10-character form with leading zeros
*&    (the portal also pads it; this is a safety net).
*&  - Idempotent: a POST for a ZID_NO that already exists updates that
*&    row instead of failing. The portal retries failed pointers, and a
*&    row may already exist when the earlier response was lost, so a
*&    duplicate must not park the pointer at "Posting Failed" forever.
*&    The original CREATED_BY / CREATED_AT are kept; CHANGED_BY /
*&    CHANGED_AT are set on a re-post. To make the create strict instead,
*&    see the marked block below.
*&  - SOURCEOFENTRY defaults to 'PORTAL' when the caller sends none;
*&    PORTAL_ID (the portal's collateral item GUID, "Security Comp ID")
*&    and MONITOR_ID are stored as sent.
*&  - No COMMIT WORK here: the Gateway framework commits after a
*&    successful non-$batch request (and CHANGESET_END handles $batch).
*&
*& Assumptions (standard SEGW "import DDIC structure" project):
*&  - MPC type ZCL_ZPFS_LMS_COLLATERAL_MPC=>TS_CHECKLISTHEADER has the
*&    DDIC field names of ZPFS_T_LN_CHKLST (ZID_NO, ZLOAN_NUMBER, ...),
*&    so MOVE-CORRESPONDING maps it 1:1. Business dates are DATS
*&    (Edm.DateTime Precision 7), CREATED_AT / CHANGED_AT are TIMESTAMP
*&    (Edm.DateTime Precision 0), flags are ABAP_BOOL / CHAR1.
*&  - If the SEGW project or table is named differently, adjust the two
*&    type references and the table name; nothing else depends on them.
*&---------------------------------------------------------------------*
  METHOD checklistheaders_create_entity.

    CONSTANTS:
      lc_source_portal TYPE zpfs_t_ln_chklst-sourceofentry VALUE 'PORTAL'.

    DATA:
      ls_entity   TYPE zcl_zpfs_lms_collateral_mpc=>ts_checklistheader,
      ls_chklst   TYPE zpfs_t_ln_chklst,
      ls_existing TYPE zpfs_t_ln_chklst,
      lv_exists   TYPE abap_bool,
      lv_ts       TYPE timestamp,
      lv_subrc    TYPE string,
      lv_text     TYPE string,
      lo_msg      TYPE REF TO /iwbep/if_message_container.

    lo_msg = mo_context->get_message_container( ).

*-- 1. Read the posted entity -----------------------------------------
    io_data_provider->read_entry_data( IMPORTING es_data = ls_entity ).

*-- 2. Mandatory fields ------------------------------------------------
    IF ls_entity-zloan_number IS INITIAL.
      lo_msg->add_message_text_only(
        EXPORTING
          iv_msg_type = /iwbep/cl_cos_logger=>error
          iv_msg_text = 'Loan contract number (ZLOAN_NUMBER) is mandatory for a collateral item.' ).
      RAISE EXCEPTION TYPE /iwbep/cx_mgw_busi_exception
        EXPORTING
          message_container = lo_msg
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    ENDIF.

    IF ls_entity-zid_no IS INITIAL.
      lo_msg->add_message_text_only(
        EXPORTING
          iv_msg_type = /iwbep/cl_cos_logger=>error
          iv_msg_text = 'Checklist ID No. (ZID_NO) is mandatory for a collateral item.' ).
      RAISE EXCEPTION TYPE /iwbep/cx_mgw_busi_exception
        EXPORTING
          message_container = lo_msg
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    ENDIF.

*-- 3. Normalise the key: CHAR10 with leading zeros ---------------------
    CALL FUNCTION 'CONVERSION_EXIT_ALPHA_INPUT'
      EXPORTING
        input  = ls_entity-zid_no
      IMPORTING
        output = ls_entity-zid_no.

*-- 4. Does the row exist already (re-post after a lost response)? -------
    SELECT SINGLE *
      FROM zpfs_t_ln_chklst
      INTO ls_existing
      WHERE zid_no = ls_entity-zid_no.
    IF sy-subrc = 0.
      lv_exists = abap_true.
    ENDIF.

*   --- Strict alternative: reject a duplicate instead of updating it ---
*   IF lv_exists = abap_true.
*     CONCATENATE 'Collateral checklist item' ls_entity-zid_no 'already exists.'
*       INTO lv_text SEPARATED BY space.
*     lo_msg->add_message_text_only(
*       EXPORTING
*         iv_msg_type = /iwbep/cl_cos_logger=>error
*         iv_msg_text = lv_text ).
*     RAISE EXCEPTION TYPE /iwbep/cx_mgw_busi_exception
*       EXPORTING
*         message_container = lo_msg
*         http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-conflict.
*   ENDIF.
*   ----------------------------------------------------------------------

*-- 5. Map the entity to the table row --------------------------------
    MOVE-CORRESPONDING ls_entity TO ls_chklst.
    ls_chklst-mandt = sy-mandt.

    IF ls_chklst-sourceofentry IS INITIAL.
      ls_chklst-sourceofentry = lc_source_portal.
    ENDIF.

*-- 6. Audit fields ---------------------------------------------------
    GET TIME STAMP FIELD lv_ts.
    IF lv_exists = abap_true.
      " keep the original creation stamp, record this as a change
      ls_chklst-created_by = ls_existing-created_by.
      ls_chklst-created_at = ls_existing-created_at.
      ls_chklst-changed_by = sy-uname.
      ls_chklst-changed_at = lv_ts.
    ELSE.
      ls_chklst-created_by = sy-uname.
      ls_chklst-created_at = lv_ts.
      CLEAR: ls_chklst-changed_by, ls_chklst-changed_at.
    ENDIF.

*-- 7. Write ----------------------------------------------------------
    IF lv_exists = abap_true.
      UPDATE zpfs_t_ln_chklst FROM ls_chklst.
    ELSE.
      INSERT zpfs_t_ln_chklst FROM ls_chklst.
    ENDIF.

    IF sy-subrc <> 0.
      lv_subrc = sy-subrc.
      CONDENSE lv_subrc.
      CONCATENATE 'Collateral checklist item' ls_chklst-zid_no
                  'could not be saved (sy-subrc' lv_subrc ').'
        INTO lv_text SEPARATED BY space.
      lo_msg->add_message_text_only(
        EXPORTING
          iv_msg_type = /iwbep/cl_cos_logger=>error
          iv_msg_text = lv_text ).
      RAISE EXCEPTION TYPE /iwbep/cx_mgw_tech_exception
        EXPORTING
          message_container = lo_msg.
    ENDIF.

*-- 8. Return the stored row as the created entity --------------------
    MOVE-CORRESPONDING ls_chklst TO er_entity.

  ENDMETHOD.
