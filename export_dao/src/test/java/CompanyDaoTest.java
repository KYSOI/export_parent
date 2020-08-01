import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
public class CompanyDaoTest {

    public static void main(String[] args) throws IOException {
        //1、加载mybatis的核心配置文件
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2、创建SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //3、创建SqlSessionFactory
        SqlSessionFactory sessionFactory = builder.build(is);
        //4、创建SqlSession
        SqlSession sqlSession = sessionFactory.openSession();
        //5、创建Dao接口的动态代理对象

        CompanyDao companyDao = sqlSession.getMapper(CompanyDao.class);

        List<Company> list = companyDao.findAll();

        for (Company company : list) {
            System.out.println(company);
        }

        //7、释放资源
        sqlSession.close();
    }
}
