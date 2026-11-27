*&---------------------------------------------------------------------*
*& ZCL_ZPFS_LMS_COLLATERA_DPC_EXT — ProjectEvaluationSet (ZLMS_COLL_EVAL)
*&
*& Implementation of the five PROJECTEVALUATIO_* redefinitions, in the
*& style of the other sets of this class (create_item / update_item /
*& delete_item / read_items), but working through ZCL_LMS_PORTAL_COLL_EVAL
*& so that the portal and the SAP GUI tab "Project(Coll.) Valuation"
*& maintain the same table in the same way.
*&
*& The portal identifies a row by PTL_ID (the portal's own id); SAP's row
*& GUID (ID) is generated here by ZCL_LMS_PORTAL_COLL_EVAL->CREATE_SNG and
*& never known to the portal. Update and delete therefore locate the row by
*& BUKRS + RANL + PTL_ID and hand its ID to UPDATE_SNG / DELETE_SNG.
*&
*& Prerequisite in SEGW (see the note at the end): PtlId must be a key of
*& ProjectEvaluationSet, so that PUT and DELETE carry it.
*&---------------------------------------------------------------------*

*======================================================================*
* 1. CLASS DEFINITION — add to the PRIVATE SECTION
*======================================================================*
*  constants C_TAB_COLL_EVAL type TABNAME value 'ZLMS_COLL_EVAL'. "#EC NOTEXT
*  constants C_KEY_BUKRS type STRING value 'Bukrs'.             "#EC NOTEXT
*  constants C_KEY_RANL  type STRING value 'Ranl'.              "#EC NOTEXT
*  constants C_KEY_ITEMNO type STRING value 'ItemNo'.           "#EC NOTEXT
*
*  methods READ_COLL_EVAL_BY_PTLID
*    importing
*      !IT_KEY_TAB type /IWBEP/T_MGW_NAME_VALUE_PAIR
*    exporting
*      !ES_ROW   type ZLMS_COLL_EVAL
*      !EV_BUKRS type BUKRS
*      !EV_RANL  type RANL
*      !EV_PTLID type ZDE_PTL_ID
*    raising
*      /IWBEP/CX_MGW_BUSI_EXCEPTION .
*
*  methods COLL_EVAL_KEYS_FROM_FILTER
*    importing
*      !IT_FILTER_SO type /IWBEP/T_MGW_SELECT_OPTION
*    exporting
*      !EV_BUKRS type BUKRS
*      !EV_RANL  type RANL
*      !EV_PTLID type ZDE_PTL_ID .

*======================================================================*
* 2. CLASS IMPLEMENTATION
*======================================================================*

* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Private Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->READ_COLL_EVAL_BY_PTLID
* +-------------------------------------------------------------------------------------------------+
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [<---] ES_ROW                         TYPE        ZLMS_COLL_EVAL
* | [<---] EV_BUKRS                       TYPE        BUKRS
* | [<---] EV_RANL                        TYPE        RANL
* | [<---] EV_PTLID                       TYPE        ZDE_PTL_ID
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD read_coll_eval_by_ptlid.
    " The row of ZLMS_COLL_EVAL addressed by the OData key
    " ProjectEvaluationSet(Bukrs='PFS',Ranl='0000020000736',ItemNo='000001').
    " Found by company code + contract + line item (the SEGW keys). When the
    " portal id is also known — PtlId in the key (if ever a key), else the
    " request header X-Portal-Id sent on PUT / DELETE — the row must carry
    " that portal id, so a portal record can never touch another portal
    " record's line.
    data: lv_key     type string,
          lv_bukrs   type string,
          lv_ranl    type string,
          lv_ptlid   type string,
          lv_itemno  type string,
          lv_item_no type zlms_coll_eval-item_no,
          lt_headers type tihttpnvp,
          ls_header  type ihttpnvp.

    clear: es_row, ev_bukrs, ev_ranl, ev_ptlid.

    lv_bukrs  = get_key_value( it_key_tab = it_key_tab iv_name = c_key_bukrs ).
    lv_ranl   = get_key_value( it_key_tab = it_key_tab iv_name = c_key_ranl ).
    lv_itemno = get_key_value( it_key_tab = it_key_tab iv_name = c_key_itemno ).
    lv_ptlid  = get_key_value( it_key_tab = it_key_tab iv_name = c_key_ptlid ).
    ev_bukrs = lv_bukrs.
    ev_ranl  = lv_ranl.
    ev_ptlid = lv_ptlid.

    if ev_ptlid is initial.
      " the portal sends its record id as request header X-Portal-Id
      lt_headers = me->/iwbep/if_mgw_conv_srv_runtime~get_request_header( ).
      loop at lt_headers into ls_header.
        translate ls_header-name to lower case.
        if ls_header-name = 'x-portal-id'.
          ev_ptlid = ls_header-value.
          exit.
        endif.
      endloop.
    endif.

    if ev_bukrs is initial.
      ev_bukrs = zcc_lms_constants=>company_code.
    endif.
    if ev_ranl is initial or lv_itemno is initial.
      mo_context->get_message_container( )->add_message_text_only(
        iv_msg_type = 'E'
        iv_msg_text = 'Contract Number (Ranl) and Line Item (ItemNo) are required to address a project valuation' ).
      raise exception type /iwbep/cx_mgw_busi_exception
        exporting
          message_container = mo_context->get_message_container( )
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    endif.
    lv_item_no = lv_itemno.   " '1' or '000001' -> NUMC 6

    " Ranl arrives ALPHA-converted (SEGW conversion exit)
    select single * from zlms_coll_eval into es_row
      where bukrs   = ev_bukrs
        and ranl    = ev_ranl
        and item_no = lv_item_no.
    if sy-subrc <> 0.
      concatenate lv_bukrs '/' lv_ranl '/' lv_itemno into lv_key.
      raise_not_found( iv_tabname = c_tab_coll_eval iv_key = lv_key ).
    endif.

    " a portal record may only change / delete its own line
    if ev_ptlid is not initial and es_row-ptl_id is not initial and es_row-ptl_id <> ev_ptlid.
      mo_context->get_message_container( )->add_message_text_only(
        iv_msg_type = 'E'
        iv_msg_text = 'Line item belongs to another portal record (Portal Id does not match)' ).
      raise exception type /iwbep/cx_mgw_busi_exception
        exporting
          message_container = mo_context->get_message_container( )
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-conflict.
    endif.
    ev_ptlid = es_row-ptl_id.
    ev_ranl  = es_row-ranl.
ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Private Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->COLL_EVAL_KEYS_FROM_FILTER
* +-------------------------------------------------------------------------------------------------+
* | [--->] IT_FILTER_SO                   TYPE        /IWBEP/T_MGW_SELECT_OPTION
* | [<---] EV_BUKRS                       TYPE        BUKRS
* | [<---] EV_RANL                        TYPE        RANL
* | [<---] EV_PTLID                       TYPE        ZDE_PTL_ID
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD coll_eval_keys_from_filter.
    " $filter=Bukrs eq 'PFS' and Ranl eq '0000020000231' [and PtlId eq '…']
    DATA: ls_so  LIKE LINE OF it_filter_so,
          ls_rng TYPE /iwbep/s_cod_select_option.

    CLEAR: ev_bukrs, ev_ranl, ev_ptlid.
    LOOP AT it_filter_so INTO ls_so.
      READ TABLE ls_so-select_options INTO ls_rng INDEX 1.
      IF sy-subrc <> 0 OR ls_rng-option <> 'EQ'.
        CONTINUE.
      ENDIF.
      CASE ls_so-property.
        WHEN 'Bukrs'. ev_bukrs = ls_rng-low.
        WHEN 'Ranl'.  ev_ranl  = ls_rng-low.
        WHEN 'PtlId'. ev_ptlid = ls_rng-low.
      ENDCASE.
    ENDLOOP.
  ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Protected Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->PROJECTEVALUATIO_CREATE_ENTITY
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_ENTITY_NAME                 TYPE        STRING
* | [--->] IV_ENTITY_SET_NAME             TYPE        STRING
* | [--->] IV_SOURCE_NAME                 TYPE        STRING
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [--->] IO_TECH_REQUEST_CONTEXT        TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITY_C(optional)
* | [--->] IT_NAVIGATION_PATH             TYPE        /IWBEP/T_MGW_NAVIGATION_PATH
* | [--->] IO_DATA_PROVIDER               TYPE REF TO /IWBEP/IF_MGW_ENTRY_PROVIDER(optional)
* | [<---] ER_ENTITY                      TYPE        ZCL_ZPFS_LMS_COLLATERA_MPC=>TS_PROJECTEVALUATION
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* | [!CX!] /IWBEP/CX_MGW_TECH_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD projectevaluatio_create_entity.
" POST ProjectEvaluationSet — the portal's "New Valuation" (SAP integration mode C).
    data: ls_row       type zlms_coll_eval,
          ls_existing  type zlms_coll_eval,
          lt_coll_eval type zlms_tt_coll_eval,
          ls_coll_eval type zlms_str_coll_eval,
          lv_max_item  type zlms_coll_eval-item_no,
          lv_other_ptlid type zlms_coll_eval-ptl_id,
          lv_itemno_s  type string,
          lo_coll_eval type ref to zcl_lms_portal_coll_eval,
          lo_msg       type ref to /iwbep/if_message_container.

    lo_msg = mo_context->get_message_container( ).

    " payload into the entity structure, then into a DB row of the table
    io_data_provider->read_entry_data( importing es_data = er_entity ).
    move-corresponding er_entity to ls_row.

    " keys from the URL (an update routed here as upsert) when the payload lacks them
    if ls_row-ranl is initial.
      ls_row-ranl = get_key_value( it_key_tab = it_key_tab iv_name = c_key_ranl ).
    endif.
    if ls_row-item_no is initial.
      lv_itemno_s = get_key_value( it_key_tab = it_key_tab iv_name = c_key_itemno ).
      ls_row-item_no = lv_itemno_s.
    endif.
    if ls_row-bukrs is initial.
      ls_row-bukrs = get_key_value( it_key_tab = it_key_tab iv_name = c_key_bukrs ).
    endif.
    if ls_row-bukrs is initial.
      ls_row-bukrs = zcc_lms_constants=>company_code.
    endif.
    if ls_row-ranl is initial.
      lo_msg->add_message_text_only(
        iv_msg_type = 'E' iv_msg_text = 'Contract Number (Ranl) is required' ).
      raise exception type /iwbep/cx_mgw_busi_exception
        exporting
          message_container = lo_msg
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    endif.
    if ls_row-ptl_id is initial.
      lo_msg->add_message_text_only(
        iv_msg_type = 'E' iv_msg_text = 'Portal Id (PtlId) is required' ).
      raise exception type /iwbep/cx_mgw_busi_exception
        exporting
          message_container = lo_msg
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    endif.
    if ls_row-valuation_by is initial or ls_row-val_date is initial.
      lo_msg->add_message_text_only(
        iv_msg_type = 'E' iv_msg_text = 'Valuation By and Valuation Date are required' ).
      raise exception type /iwbep/cx_mgw_busi_exception
        exporting
          message_container = lo_msg
          http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-bad_request.
    endif.

    " Idempotent: the portal resends a failed create on the next sweep.
    " A row with this portal id already exists -> treat as an update.
    select single * from zlms_coll_eval into ls_existing
      where bukrs  = ls_row-bukrs
        and ranl   = ls_row-ranl
        and ptl_id = ls_row-ptl_id.
    if sy-subrc = 0.
      ls_row-id         = ls_existing-id.
      ls_row-created_at = ls_existing-created_at.
      ls_row-created_by = ls_existing-created_by.
      if ls_row-item_no is initial.
        ls_row-item_no = ls_existing-item_no.
      endif.
      if ls_row-sourceofentry is initial.
        ls_row-sourceofentry = ls_existing-sourceofentry.
      endif.
      create object lo_coll_eval.
      lo_coll_eval->update_sng(
        exporting iv_ranl      = ls_row-ranl
                  iv_bukrs     = ls_row-bukrs
                  is_coll_eval = ls_row
        importing et_coll_eval = lt_coll_eval ).
      write_change_doc( iv_tabname = c_tab_coll_eval iv_indicator = 'U'
                        is_new = ls_row is_old = ls_existing ).
    else.
      " line item number: the portal numbers 1, 2, 3 … per loan; when it
      " sends none, continue after the highest number of the loan
      if ls_row-item_no is initial.
        select max( item_no ) from zlms_coll_eval into lv_max_item
          where bukrs = ls_row-bukrs and ranl = ls_row-ranl.
        ls_row-item_no = lv_max_item + 1.
      endif.
      " ItemNo is a key of the entity: the same line item of the loan must
      " not exist yet (e.g. created in the SAP GUI with the same number)
      select single ptl_id from zlms_coll_eval into lv_other_ptlid
        where bukrs = ls_row-bukrs and ranl = ls_row-ranl and item_no = ls_row-item_no.
      if sy-subrc = 0.
        lo_msg->add_message_text_only(
          iv_msg_type = 'E' iv_msg_text = 'Line item number already exists for this contract' ).
        raise exception type /iwbep/cx_mgw_busi_exception
          exporting
            message_container = lo_msg
            http_status_code  = /iwbep/cx_mgw_busi_exception=>gcs_http_status_codes-conflict.
      endif.
      if ls_row-sourceofentry is initial.
        ls_row-sourceofentry = 'PORTAL'.
      endif.
      " CREATE_SNG generates the ID, sets CREATED_AT/BY and inserts the row
      " (its SOURCEOFENTRY = sy-tcode is overwritten with the portal's below)
      create object lo_coll_eval.
      lo_coll_eval->create_sng(
        exporting iv_ranl      = ls_row-ranl
                  iv_bukrs     = ls_row-bukrs
                  is_coll_eval = ls_row
        importing et_coll_eval = lt_coll_eval ).
      update zlms_coll_eval set sourceofentry = ls_row-sourceofentry
        where bukrs = ls_row-bukrs and ranl = ls_row-ranl and ptl_id = ls_row-ptl_id.
      write_change_doc( iv_tabname = c_tab_coll_eval iv_indicator = 'I'
                        is_new = ls_row is_old = ls_row ).
    endif.

    " response = the stored row (incl. the generated ID and admin fields)
    read table lt_coll_eval into ls_coll_eval with key ptl_id = ls_row-ptl_id.
    if sy-subrc = 0.
      move-corresponding ls_coll_eval to er_entity.
    else.
      move-corresponding ls_row to er_entity.
    endif.
ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Protected Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->PROJECTEVALUATIO_UPDATE_ENTITY
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_ENTITY_NAME                 TYPE        STRING
* | [--->] IV_ENTITY_SET_NAME             TYPE        STRING
* | [--->] IV_SOURCE_NAME                 TYPE        STRING
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [--->] IO_TECH_REQUEST_CONTEXT        TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITY_U(optional)
* | [--->] IT_NAVIGATION_PATH             TYPE        /IWBEP/T_MGW_NAVIGATION_PATH
* | [--->] IO_DATA_PROVIDER               TYPE REF TO /IWBEP/IF_MGW_ENTRY_PROVIDER(optional)
* | [<---] ER_ENTITY                      TYPE        ZCL_ZPFS_LMS_COLLATERA_MPC=>TS_PROJECTEVALUATION
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* | [!CX!] /IWBEP/CX_MGW_TECH_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD projectevaluatio_update_entity.
    " PUT ProjectEvaluationSet(Bukrs='…',Ranl='…',ItemNo='…') — "Change Valuation" (mode U),
    " also used when the portal changes the Major Collateral Type of the loan.
    data: ls_old       type zlms_coll_eval,
          ls_row       type zlms_coll_eval,
          lv_bukrs     type bukrs,
          lv_ranl      type ranl,
          lv_ptlid     type zde_ptl_id,
          lt_coll_eval type zlms_tt_coll_eval,
          ls_coll_eval type zlms_str_coll_eval,
          lo_coll_eval type ref to zcl_lms_portal_coll_eval,
          lv_itemno_s  type string,
          lv_item_no   type zlms_coll_eval-item_no,
          lv_bukrs_s   type string,
          lv_ranl_s    type string,
          lv_exists    type zlms_coll_eval-item_no.

    " Upsert: a PUT for a line item that does not exist yet creates it (the
    " portal may resend a change after a lost create, or SAP data was reset)
    lv_bukrs_s  = get_key_value( it_key_tab = it_key_tab iv_name = c_key_bukrs ).
    lv_ranl_s   = get_key_value( it_key_tab = it_key_tab iv_name = c_key_ranl ).
    lv_itemno_s = get_key_value( it_key_tab = it_key_tab iv_name = c_key_itemno ).
    lv_bukrs    = lv_bukrs_s.
    lv_ranl     = lv_ranl_s.
    lv_item_no  = lv_itemno_s.
    if lv_bukrs is initial.
      lv_bukrs = zcc_lms_constants=>company_code.
    endif.
    if lv_ranl is not initial and lv_item_no is not initial.
      select single item_no from zlms_coll_eval into lv_exists
        where bukrs = lv_bukrs and ranl = lv_ranl and item_no = lv_item_no.
      if sy-subrc <> 0.
        projectevaluatio_create_entity(
          exporting iv_entity_name     = iv_entity_name
                    iv_entity_set_name = iv_entity_set_name
                    iv_source_name     = iv_source_name
                    it_key_tab         = it_key_tab
                    it_navigation_path = it_navigation_path
                    io_data_provider   = io_data_provider
          importing er_entity          = er_entity ).
        return.
      endif.
    endif.

    read_coll_eval_by_ptlid(
      exporting it_key_tab = it_key_tab
      importing es_row     = ls_old
                ev_bukrs   = lv_bukrs
                ev_ranl    = lv_ranl
                ev_ptlid   = lv_ptlid ).

    " payload overwrites the business fields; keys, ID, line item and the
    " creation stamp are protected
    ls_row = ls_old.
    io_data_provider->read_entry_data( importing es_data = er_entity ).
    move-corresponding er_entity to ls_row.
    ls_row-id         = ls_old-id.
    ls_row-bukrs      = ls_old-bukrs.
    ls_row-ranl       = ls_old-ranl.
    ls_row-ptl_id     = ls_old-ptl_id.
    ls_row-created_at = ls_old-created_at.
    ls_row-created_by = ls_old-created_by.
    if ls_row-item_no is initial.
      ls_row-item_no = ls_old-item_no.
    endif.
    if ls_row-sourceofentry is initial.
      ls_row-sourceofentry = ls_old-sourceofentry.
    endif.

    " UPDATE_SNG sets CHANGED_AT/BY and updates the whole row by its ID
    create object lo_coll_eval.
    lo_coll_eval->update_sng(
      exporting iv_ranl      = ls_row-ranl
                iv_bukrs     = ls_row-bukrs
                is_coll_eval = ls_row
      importing et_coll_eval = lt_coll_eval ).

    write_change_doc( iv_tabname = c_tab_coll_eval iv_indicator = 'U'
                      is_new = ls_row is_old = ls_old ).

    read table lt_coll_eval into ls_coll_eval with key ptl_id = ls_row-ptl_id.
    if sy-subrc = 0.
      move-corresponding ls_coll_eval to er_entity.
    else.
      move-corresponding ls_row to er_entity.
    endif.
ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Protected Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->PROJECTEVALUATIO_DELETE_ENTITY
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_ENTITY_NAME                 TYPE        STRING
* | [--->] IV_ENTITY_SET_NAME             TYPE        STRING
* | [--->] IV_SOURCE_NAME                 TYPE        STRING
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [--->] IO_TECH_REQUEST_CONTEXT        TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITY_D(optional)
* | [--->] IT_NAVIGATION_PATH             TYPE        /IWBEP/T_MGW_NAVIGATION_PATH
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* | [!CX!] /IWBEP/CX_MGW_TECH_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD projectevaluatio_delete_entity.
    " DELETE ProjectEvaluationSet(Bukrs='…',Ranl='…',PtlId='…') — "Delete Valuation" (mode D).
    DATA: ls_old       TYPE zlms_coll_eval,
          lv_bukrs     TYPE bukrs,
          lv_ranl      TYPE ranl,
          lv_ptlid     TYPE zde_ptl_id,
          lt_coll_eval TYPE zlms_tt_coll_eval,
          lo_coll_eval TYPE REF TO zcl_lms_portal_coll_eval.

    read_coll_eval_by_ptlid(
      EXPORTING it_key_tab = it_key_tab
      IMPORTING es_row     = ls_old
                ev_bukrs   = lv_bukrs
                ev_ranl    = lv_ranl
                ev_ptlid   = lv_ptlid ).

    " DELETE_SNG deletes by the row's ID
    CREATE OBJECT lo_coll_eval.
    lo_coll_eval->delete_sng(
      EXPORTING iv_ranl      = ls_old-ranl
                iv_bukrs     = ls_old-bukrs
                is_coll_eval = ls_old
      IMPORTING et_coll_eval = lt_coll_eval ).

    write_change_doc( iv_tabname = c_tab_coll_eval iv_indicator = 'D'
                      is_new = ls_old is_old = ls_old ).
  ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Protected Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->PROJECTEVALUATIO_GET_ENTITY
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_ENTITY_NAME                 TYPE        STRING
* | [--->] IV_ENTITY_SET_NAME             TYPE        STRING
* | [--->] IV_SOURCE_NAME                 TYPE        STRING
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [--->] IO_REQUEST_OBJECT              TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITY(optional)
* | [--->] IO_TECH_REQUEST_CONTEXT        TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITY(optional)
* | [--->] IT_NAVIGATION_PATH             TYPE        /IWBEP/T_MGW_NAVIGATION_PATH
* | [<---] ER_ENTITY                      TYPE        ZCL_ZPFS_LMS_COLLATERA_MPC=>TS_PROJECTEVALUATION
* | [<---] ES_RESPONSE_CONTEXT            TYPE        /IWBEP/IF_MGW_APPL_SRV_RUNTIME=>TY_S_MGW_RESPONSE_ENTITY_CNTXT
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* | [!CX!] /IWBEP/CX_MGW_TECH_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD projectevaluatio_get_entity.
    " GET ProjectEvaluationSet(Bukrs='…',Ranl='…',PtlId='…')
    DATA: ls_row   TYPE zlms_coll_eval,
          lv_bukrs TYPE bukrs,
          lv_ranl  TYPE ranl,
          lv_ptlid TYPE zde_ptl_id.

    read_coll_eval_by_ptlid(
      EXPORTING it_key_tab = it_key_tab
      IMPORTING es_row     = ls_row
                ev_bukrs   = lv_bukrs
                ev_ranl    = lv_ranl
                ev_ptlid   = lv_ptlid ).

    MOVE-CORRESPONDING ls_row TO er_entity.
  ENDMETHOD.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Protected Method ZCL_ZPFS_LMS_COLLATERA_DPC_EXT->PROJECTEVALUATIO_GET_ENTITYSET
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_ENTITY_NAME                 TYPE        STRING
* | [--->] IV_ENTITY_SET_NAME             TYPE        STRING
* | [--->] IV_SOURCE_NAME                 TYPE        STRING
* | [--->] IT_FILTER_SELECT_OPTIONS       TYPE        /IWBEP/T_MGW_SELECT_OPTION
* | [--->] IS_PAGING                      TYPE        /IWBEP/S_MGW_PAGING
* | [--->] IT_KEY_TAB                     TYPE        /IWBEP/T_MGW_NAME_VALUE_PAIR
* | [--->] IT_NAVIGATION_PATH             TYPE        /IWBEP/T_MGW_NAVIGATION_PATH
* | [--->] IT_ORDER                       TYPE        /IWBEP/T_MGW_SORTING_ORDER
* | [--->] IV_FILTER_STRING               TYPE        STRING
* | [--->] IV_SEARCH_STRING               TYPE        STRING
* | [--->] IO_TECH_REQUEST_CONTEXT        TYPE REF TO /IWBEP/IF_MGW_REQ_ENTITYSET(optional)
* | [<---] ET_ENTITYSET                   TYPE        ZCL_ZPFS_LMS_COLLATERA_MPC=>TT_PROJECTEVALUATION
* | [<---] ES_RESPONSE_CONTEXT            TYPE        /IWBEP/IF_MGW_APPL_SRV_RUNTIME=>TY_S_MGW_RESPONSE_CONTEXT
* | [!CX!] /IWBEP/CX_MGW_BUSI_EXCEPTION
* | [!CX!] /IWBEP/CX_MGW_TECH_EXCEPTION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  METHOD projectevaluatio_get_entityset.
    " GET ProjectEvaluationSet?$filter=Bukrs eq 'PFS' and Ranl eq '0000020000231'
    " (optionally and PtlId eq '…'); without a contract every valuation of the
    " company code is read, archived rows (SARCHIV) excluded.
    DATA: lv_bukrs     TYPE bukrs,
          lv_ranl      TYPE ranl,
          lv_ptlid     TYPE zde_ptl_id,
          lt_coll_eval TYPE zlms_tt_coll_eval,
          ls_coll_eval TYPE zlms_str_coll_eval,
          lt_rows      TYPE STANDARD TABLE OF zlms_coll_eval,
          ls_row       TYPE zlms_coll_eval,
          ls_entity    TYPE zcl_zpfs_lms_collatera_mpc=>ts_projectevaluation,
          lo_coll_eval TYPE REF TO zcl_lms_portal_coll_eval.

    coll_eval_keys_from_filter(
      EXPORTING it_filter_so = it_filter_select_options
      IMPORTING ev_bukrs     = lv_bukrs
                ev_ranl      = lv_ranl
                ev_ptlid     = lv_ptlid ).
    IF lv_bukrs IS INITIAL.
      lv_bukrs = zcc_lms_constants=>company_code.
    ENDIF.

    IF lv_ranl IS NOT INITIAL.
      " one loan: the same read as the SAP GUI tab (with the Valuation By text)
      CREATE OBJECT lo_coll_eval.
      lo_coll_eval->get_by_loan_contract(
        EXPORTING iv_bukrs     = lv_bukrs
                  iv_ranl      = lv_ranl
        IMPORTING et_coll_eval = lt_coll_eval ).
      LOOP AT lt_coll_eval INTO ls_coll_eval.
        IF lv_ptlid IS NOT INITIAL AND ls_coll_eval-ptl_id <> lv_ptlid.
          CONTINUE.
        ENDIF.
        CLEAR ls_entity.
        MOVE-CORRESPONDING ls_coll_eval TO ls_entity.
        APPEND ls_entity TO et_entityset.
      ENDLOOP.
    ELSE.
      SELECT * FROM zlms_coll_eval INTO TABLE lt_rows
        WHERE bukrs   = lv_bukrs
          AND sarchiv = space.
      LOOP AT lt_rows INTO ls_row.
        IF lv_ptlid IS NOT INITIAL AND ls_row-ptl_id <> lv_ptlid.
          CONTINUE.
        ENDIF.
        CLEAR ls_entity.
        MOVE-CORRESPONDING ls_row TO ls_entity.
        APPEND ls_entity TO et_entityset.
      ENDLOOP.
    ENDIF.

    " default order: contract, then line item
    IF it_order IS INITIAL.
      SORT et_entityset BY ranl item_no.
    ELSE.
      /iwbep/cl_mgw_data_util=>orderby( EXPORTING it_order = it_order
                                        CHANGING  ct_data  = et_entityset ).
    ENDIF.
    es_response_context-inlinecount = lines( et_entityset ).
    IF is_paging-top > 0 OR is_paging-skip > 0.
      /iwbep/cl_mgw_data_util=>paging( EXPORTING is_paging = is_paging
                                       CHANGING  ct_data   = et_entityset ).
    ENDIF.
    IF io_tech_request_context IS BOUND AND io_tech_request_context->has_inlinecount( ) = abap_false.
      CLEAR es_response_context-inlinecount.
    ENDIF.
  ENDMETHOD.

*======================================================================*
* 3. BUILD_WHERE — optional, so that $filter on PtlId also works for the
*    other sets: add to the CASE in BUILD_WHERE
*======================================================================*
*        WHEN 'PtlId'.      lv_field = 'PTL_ID'.
*        WHEN 'ItemNo'.     lv_field = 'ITEM_NO'.

*======================================================================*
* 4. SEGW — ProjectEvaluationSet
*======================================================================*
* Keys: Bukrs, Ranl, ItemNo. The portal addresses a row as
*   ProjectEvaluationSet(Bukrs='PFS',Ranl='0000020000231',ItemNo='000001')
* and also sends its record id as PtlId (body) and header X-Portal-Id, which
* READ_COLL_EVAL_BY_PTLID uses to check the line belongs to that record.
* and the body with ItemNo zero-padded to 6 (NUMC), MajorCollType,
* ValuationBy, ValDate, TotalAsset, TotalCurrLiab, NetValAmt, ValCurr,
* Remarks, PtlId, Sourceofentry = 'PORTAL'.
* The property ValuationByDesc (from ZLMS_STR_COLL_EVAL) is read-only.
