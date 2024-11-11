package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.Title;
import pfs.lms.enquiry.businesspartner.domain.Title;
import pfs.lms.enquiry.businesspartner.repository.TitleRepository;
import pfs.lms.enquiry.businesspartner.repository.TitleRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class TitleConfig implements CommandLineRunner {

    private final TitleRepository titleRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        Title title = new Title();


        title = titleRepository.findByCode("0001");if (title == null){ title = new Title(null,"0001","Ms.","1"); titleRepository.save(title); }
        title = titleRepository.findByCode("0002");if (title == null){ title = new Title(null,"0002","Mr." ,"1"  ); titleRepository.save(title); }
        title = titleRepository.findByCode("0003");if (title == null){ title = new Title(null,"0003","Company", "2"   ); titleRepository.save(title); }
        title = titleRepository.findByCode("0004");if (title == null){ title = new Title(null,"0004","Mr. and Mrs.","3"   ); titleRepository.save(title); }

        return;
    }
}