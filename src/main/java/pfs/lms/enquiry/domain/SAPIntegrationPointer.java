package pfs.lms.enquiry.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.checkerframework.common.reflection.qual.GetClass;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sajeev on 13-Jun-21.
 */
@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPIntegrationPointer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String businessObjectId;

    private String businessProcessName;
    private String subBusinessProcessName;

    private Date creationDate;

    private Date processDate;


    //0- Not Posted
    //1- In Posting Process
    //2- Posting Failed
    //3 - Posted Successfully
    private Integer status;

    @Nullable
    private String mainEntityId;

    // C - Create
    // U - Update
    // D - Delete
    @NotNull
    private char mode;


}
