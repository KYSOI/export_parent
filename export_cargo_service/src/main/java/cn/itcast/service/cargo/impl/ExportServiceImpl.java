package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * 报运单service接口实现类
 */
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ExtEproductDao extEproductDao;

    /**
     * 保存报运单
     * 参数：
     * Export ：报运单对象
     * contractIds:打断字段（勾选的合同id字符串，已“，”隔开）
     * customerContract：勾选的合同号
     * state：状态（0-草稿 1-已上报）
     * proNum：货物数量
     * extNum：附件数量
     * ExportProduct：报运单商品
     * exportId：所属的报运单ID
     * ExtEproduct：报运单附件
     * exportId：所属的报运单ID
     * exportProductId：商品ID
     */
    public void save(Export export) {
        //i.根据勾选的合同生成报运单
        int proNum = 0;
        int extNum = 0;
        //1、设置id
        export.setId(UUID.randomUUID().toString());
        //2、根据勾选合同id，查询所有合同
        String cids[] = export.getContractIds().split(","); //购销合同id数组
        String customerContract = "";
        for (String cid : cids) {
            Contract contract = contractDao.selectByPrimaryKey(cid);
            //3、拼接合同号
            customerContract+= contract.getContractNo() + " ";
            //4、更新购销合同的状态  2 （已报运）
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
            proNum += contract.getProNum();
            extNum += contract.getExtNum();
        }
        export.setCustomerContract(customerContract);
        //ii.根据勾选的合同货物生成报运单商品数据
        //1、根据合同id，查询所有的合同货物 (合同id数组)
        ContractProductExample cpe = new ContractProductExample();
        ContractProductExample.Criteria cc = cpe.createCriteria();
        cc.andContractIdIn(Arrays.asList(cids));
        List<ContractProduct> cps = contractProductDao.selectByExample(cpe);
        //2、循环所有的合同货物
        for (ContractProduct cp : cps) {
            //3、一个货物构造一个商品对象数据
            ExportProduct ep = new ExportProduct();
            BeanUtils.copyProperties(cp, ep); //将cp中同名属性赋值到ep对象上
            //4、设置商品id
            ep.setId(UUID.randomUUID().toString());
            //5、设置报运单ID
            ep.setExportId(export.getId());
            exportProductDao.insertSelective(ep);
            //iii.根据勾选的合同附件生成报运单附件数据
            //1、根据当前合同货物的id，查询此货物的所有附件 (contractProductId)
            ExtCproductExample example = new ExtCproductExample();
            ExtCproductExample.Criteria criteria = example.createCriteria();
            criteria.andContractProductIdEqualTo(cp.getId());
            List<ExtCproduct> ecps = extCproductDao.selectByExample(example);
            //2、循环此合同货物的所有附件
            for (ExtCproduct ecp : ecps) {
                //3、一个合同附件，生成一个报运单附件
                ExtEproduct eep = new ExtEproduct();
                BeanUtils.copyProperties(ecp, eep);
                //4、设置附件id
                eep.setId(UUID.randomUUID().toString());
                //5、设置报运单id
                eep.setExportId(export.getId());
                //6、设置商品id
                eep.setExportProductId(ep.getId());
                //7、保存到数据库中
                extEproductDao.insertSelective(eep);
            }
        }

        export.setProNum(proNum);
        export.setExtNum(extNum);
        export.setState(0); //草稿
        exportDao.insertSelective(export);
    }

    /**
     * 更新
     * 参数；export对象
     * * 报运单主体数据（更新即可）
     * * 此报运单下更新的所有商品（exportProducts）
     */
    public void update(Export export) {
        //1.更新报运单
        exportDao.updateByPrimaryKeySelective(export);
        //2.循环报运单下更新的所有商品，更新每个商品
        if(export.getExportProducts() != null) {
            for (ExportProduct ep : export.getExportProducts()) {
                exportProductDao.updateByPrimaryKeySelective(ep);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(String id) {

    }

    //根据id查询
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    //分页
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo(list);
    }
}
