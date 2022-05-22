package pfs.lms.enquiry.service.changedocs;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pfs.lms.enquiry.domain.ChangeDocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sajeev on 15-Dec-18.
 */
public interface IChangeDocumentService {

    public ChangeDocument createChangeDocument(UUID loanBusinessProcessObjectId,
                                               String entityId,
                                               String mainEntityId,
                                               String loanContractId,
                                               Object oldObject,
                                               Object changedObject,
                                               String action,
                                               String userName,
                                               String businessProcessName,
                                               String subProcessName );


    public ChangeDocument saveChangeDocument(ChangeDocument changeDocument);

    public ChangeDocument updateChangeDocument(ChangeDocument changeDocument, Object object);



    Page<ChangeDocument> findByBusinessProcessNameAndLoanContractIdAndDateBetween(String processName,
                                                                                  String loanContractId,
                                                                                  Date dateFrom,
                                                                                  Date dateTo,
                                                                                  Pageable pageable);
    Page<ChangeDocument> findByBusinessProcessNameAndLoanContractIdAndDate(String processName,
                                                                           String loanContractId,
                                                                           Date date,
                                                                           Pageable pageable);
    Page<ChangeDocument> findByBusinessProcessNameAndLoanContractId(String processName,
                                                                    String loanContractId,
                                                                    Pageable pageable);

    Page<ChangeDocument> findByBusinessProcessName(String businessProcessName, Pageable pageable);

    Page<ChangeDocument> findByBusinessProcessNameAndDateBetween(String businessProcessName, Date dateFrom, Date dateTo, Pageable pageable);
    Page<ChangeDocument> findByBusinessProcessNameAndDate(String businessProcessName, Date date,Pageable pageable);


}