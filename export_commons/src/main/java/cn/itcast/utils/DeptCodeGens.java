package cn.itcast.utils;

import org.springframework.util.StringUtils;

public class DeptCodeGens {
	private static final int DEFAULT_CODE = 101;
	private static final int DEFAULT_DEPT_LENGTH = 3;

	/**
	 *  根据父亲code,获取下级的下一个code
	 *  参数：
	 *      parentCode：父部门id
	 *      localCode：当前父部门下最大子部门id
	 *  逻辑：
	 *      1.如果父部门id为空表示一级部门，只需要在最大的部门id加1即可
	 *      2.如果保存的为下级部门
	 *          2.1 当localCode为空，说明还没有子部门，此时编号为  101
	 *          2.2 当localCode不为空，加1
	 */
	public static synchronized String getSubCode(String parentCode,String localCode) {
		if(StringUtils.isEmpty(parentCode) || !StringUtils.isEmpty(localCode)) {
			return getNextCode(localCode);
		}else{
			return parentCode + getStrNum(DEFAULT_CODE);
		}
	}

	/**
	 * 根据前一个code，获取同级下一个code
	 */
	private static synchronized String getNextCode(String code) {
		if (code == null || code =="") {
			return getStrNum(DEFAULT_CODE);
		} else {
			String before = code.substring(0, code.length() - DEFAULT_DEPT_LENGTH);
			String after = code.substring(code.length() - DEFAULT_DEPT_LENGTH,code.length());
			Integer after_code_num = Integer.parseInt(after);
			return before + getStrNum(++after_code_num);
		}
	}

	/**
	 * 将数字前面位数补零
	 */
	private static String getStrNum(int num) {
		return String.format("%0" + DEFAULT_DEPT_LENGTH + "d", num);
	}

	/**
	 * 计算下次保存的部门id
	 *  100 市场部     (得到父部门id)
	 *      100 101     北京事业部
	 *      100 102     上海事业部(最大的子部门id)
	 *
	 *      ----------------------
	 *      100 103     广州事业部
	 *  101
	 *      101 101
	 *      101 102
	 *  102
	 *      102 101
	 *      102 102
	 *
	 *  参数一： 父部门的ID
	 *  参数二： 当前父部门中所有子部门的最大ID
	 */
	public static void main(String[] args) {
		String subCode = DeptCodeGens.getSubCode("101", null);//获取下次保存的部门id
		System.out.println(subCode);
	}
}
