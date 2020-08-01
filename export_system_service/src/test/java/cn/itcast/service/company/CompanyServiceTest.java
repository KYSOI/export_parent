package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Test
    public void test() throws Exception {
        List<Company> list = companyService.findAll();
        for (Company company : list) {
            System.out.println(company);
        }
    }
}
