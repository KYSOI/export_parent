package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.util.ImageUploadUtils;

import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    //购销合同货物
    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    /**
     * 需求：
     * 分页查询所有的购销合同货物
     * 条件：
     * page，size，
     * contractId: 购销合同id
     * 查询：根据购销合同id （contract_id），查询所有的货物
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       String contractId) {
        //1、根据条件分页查询所有的货物
        ContractProductExample example = new ContractProductExample();
        ContractProductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        PageInfo info = contractProductService.findAll(example, page, size);
        request.setAttribute("page", info);
        //2、查询所有生产货物的生产厂家   SELECT * FROM co_factory WHERE ctype='货物'
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("货物");
        List<Factory> list = factoryService.findAll(fe);
        request.setAttribute("factoryList",list);
        //3、为了后续保存货物，需要将合同id展示
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-list";
    }


    /**
     * 进入到更新页面（根据id查询货物）
     *  参数:
     *      id : 货物id
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1、根据id查询货物
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);
        //2、查询所有生产货物的生产厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("货物");
        List<Factory> list = factoryService.findAll(fe);
        request.setAttribute("factoryList",list);
        return "cargo/product/product-update";
    }

    /**
     * 新增或者修改
     *  参数：ContractProduct
     *          属性：contractId（购销合同）
     *  业务：
     *      1、设置企业属性
     *      2、判断参数是否具有id
     *      3、没有id，保存
     *      4、有id，更新
     *      5、重定向
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct cp, MultipartFile productPhoto) throws IOException {
        //1、设置企业属性
        cp.setCompanyId(getLoginCompanyId());
        cp.setCompanyName(getLoginCompanyName());
        //2、判断参数是否具有id
        if(StringUtils.isEmpty(cp.getId())) {
            /**
             * 将图片通过工具类上传到七牛云
             */
            if(!productPhoto.isEmpty()) {
                String imageUrl = new ImageUploadUtils().upload(productPhoto.getBytes());
                cp.setProductImage(imageUrl);
            }
            //3、没有id，保存
            contractProductService.save(cp);
        }else {
            //4、有id，更新
            contractProductService.update(cp);
        }
        //5、重定向
        return "redirect:/cargo/contractProduct/list.do?contractId="+cp.getContractId();
    }

    /**
     * 删除货物
     *  参数：
     *      id : 货物id
     *      contractId: 购销合同id（为重定向拼接URL）
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId) {
        contractProductService.delete(id);
        //重定向 （参数：合同id）
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 进入到上传Excel文件的页面
     *  连接：toImport
     *  参数：
     *      contractId：购销合同id （方便保存）
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        //重定向 （参数：合同id）
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    /**
     * 上传货物的Excel文件
     *    连接import.do
     *    参数：
     *      contractId:购销合同id
     *      file：excel文件
     *    业务：
     *      通过POI的形式进行Excel文件解析，获取到文件中的货物对象集合，调用service批量保存
     *
     *     cell.getStringCellValue() : 获取单元格中的字符串数据
     *     cell.getNumericCellValue(); 获取单元格中的数字类型数据（double）
     *
     *
     */
//    @RequestMapping("/import")
//    public String importExcel(String contractId,MultipartFile file) throws Exception {
//
//        //1、根据上传的文件创建工作簿
//        Workbook wb = new XSSFWorkbook(file.getInputStream());
//        //2、获取第一页
//        Sheet sheet = wb.getSheetAt(0);
//        //3、循环每一个数据行
//        List<ContractProduct> list = new ArrayList<>();
//
//        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) { //sheet.getLastRowNum() : 最后一个有数据的行索引
//            //4、获取每一个行对象
//            Row row = sheet.getRow(i);
//            //5、获取行对象中的每一个数据单元格，封装货物对象
//            ContractProduct cp = new ContractProduct();
//
//            //设置企业参数和所属的购销合同id
//            cp.setCompanyId(getLoginCompanyId());
//            cp.setCompanyName(getLoginCompanyName());
//            cp.setContractId(contractId);
//
//            cp.setFactoryName(row.getCell(1).getStringCellValue());
//            cp.setProductNo(row.getCell(2).getStringCellValue());
//            Integer cnumber = ((Double) row.getCell(3).getNumericCellValue()).intValue();//数量
//            cp.setCnumber(cnumber);
//
//            String packingUnit = row.getCell(4).getStringCellValue(); //包装单位
//            cp.setPackingUnit(packingUnit);
//
//            String loadingRate = row.getCell(5).getNumericCellValue() + ""; //装率
//            cp.setLoadingRate(loadingRate);
//            Integer boxNum = ((Double) row.getCell(6).getNumericCellValue()).intValue();//箱数
//            cp.setBoxNum(boxNum);
//            Double price = row.getCell(7).getNumericCellValue(); //单价
//            cp.setPrice(price);
//            String productDesc = row.getCell(8).getStringCellValue(); // 货物描述
//            cp.setProductDesc(productDesc);
//            String productRequest = row.getCell(9).getStringCellValue(); // 要求
//            cp.setProductRequest(productRequest);
//
//            list.add(cp);
//        }
//
//        //6、调用service批量保存
//        contractProductService.saveAll(list);
//
//        //页面跳转
//        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
//    }


    /**
     * 通过EasyExcel完成文件解析
     *  目的：将excel文件中的内容进行解析，并且获取到对应的货物list集合
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {
        List<ContractProduct> list = EasyExcel.read(file.getInputStream())
                .head(ContractProduct.class) //设置表头，将数据转化为目标对象
                .sheet(0) //读取第一页数据
                .doReadSync(); //解析excel，获取所有的数据

        for (ContractProduct contractProduct : list) {
            System.out.println(contractProduct);
            contractProduct.setContractId(contractId);
            contractProduct.setCompanyId(getLoginCompanyId());
            contractProduct.setCompanyName(getLoginCompanyName());
        }
        contractProductService.saveAll(list);
        return  "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
}