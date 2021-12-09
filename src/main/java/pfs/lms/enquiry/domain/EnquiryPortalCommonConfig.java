package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class EnquiryPortalCommonConfig extends AggregateRoot<EnquiryPortalCommonConfig> {



     //DEV - Development
     //QA -  QA Test
     //PRD - PRD System
     private String systemId;
     private String bdTeamEmail;
}
