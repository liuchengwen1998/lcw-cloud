package com.lcw.cloud.generate.entity;

import com.lcw.cloud.generate.util.HumpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yzhang
 * @since 1.0
 */
public class ApiInfo {

  private static final String GET = "get";

  private static final String UPDATE = "update";

  private static final String SAVE = "save";

  private static final String DELETE = "delete";

  /**
   * 子系统编号
   */
  public String sysCode;

  /**
   * 一级模块名
   */
  public String moduleName;

  /**
   * 二级模块名
   */
  public String sModuleName;

  public String tableName;

  public String methodDec;

  public String methodName;

  public String noPrefixMethodName;

  public String url;

  public String methodPrefix;

  private Boolean skcApi = false;

  public ApiInfo(String methodDec, String methodName, String url) {
    this.methodDec = methodDec;
    this.methodName = methodName;
    this.url = url;
  }

  public static ApiInfo of(String moduleName,String sModuleName,String methodDec, String methodName, String url ) {
    ApiInfo apiInfo = new ApiInfo(methodDec,methodName, url);
    apiInfo.moduleName = moduleName;
    apiInfo.setsModuleName(sModuleName);
    apiInfo.methodPrefix = getPrefix(methodName);
    apiInfo.noPrefixMethodName = StringUtils.substringAfter(methodName,apiInfo.methodPrefix);
    return apiInfo;
  }

  /**
   * 获取IO的名称
   * @return
   */
  public static String getPrefix(String methodName){
    String pattern = "(get|save|update|delete|batch)(.*)";

    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(methodName);
    if (m.find()) {
      return m.group(1);
    }
    return null;
  }

  public String getVoName() {
    if (this.methodPrefix.equalsIgnoreCase(GET)) {
      String vname;
      if(getSkcApi()){
        vname = getMethodPrefix()+StringUtils.capitalize(this.getSysCode()) + this.noPrefixMethodName+"ResDTO";
      }else {
        vname = StringUtils.capitalize(this.getSysCode()) + this.noPrefixMethodName+getMethodPrefix()+"VO";
      }
      // 分页查询
      if (this.noPrefixMethodName.contains("Page")) {
          return "Pagination<"+vname+">";
          // list 查询
      } else if (this.noPrefixMethodName.contains("List")) {
          return  "List<" + vname + ">";
      }
      // 单个查询
      return vname;
    } else{
      return "String";
    }
  }

  public String getDtoReturnType() {
    String vName = getVoName();
    return StringUtils.equalsIgnoreCase("string",vName) ? "void" : vName.replace("VO","ResDTO");
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }



  public String getMethodDec() {
    return methodDec;
  }

  public void setMethodDec(String methodDec) {
    this.methodDec = methodDec;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getNoPrefixMethodName() {
    return noPrefixMethodName;
  }

  public void setNoPrefixMethodName(String noPrefixMethodName) {
    this.noPrefixMethodName = noPrefixMethodName;
  }

  public String getMethodPrefix() {
    if (this.methodPrefix.equalsIgnoreCase(GET)) {
       return StringUtils.capitalize("Query");
    }
    return StringUtils.capitalize(methodPrefix);
  }

  public void setMethodPrefix(String methodPrefix) {
    this.methodPrefix = methodPrefix;
  }

  public String getsModuleName() {
    return sModuleName;
  }

  public void setsModuleName(String sModuleName) {
    this.sModuleName = sModuleName;
  }

  public String getSysCode() {
    return sysCode;
  }

  public void setSysCode(String sysCode) {
    this.sysCode = sysCode;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public Boolean getSkcApi() {
    return skcApi;
  }

  public void setSkcApi(Boolean skcApi) {
    this.skcApi = skcApi;
  }

  public String getHumpTableName() {
    String humpTableName = HumpUtil.underlineToHump(getTableName());
    return humpTableName.replace(getSysCode().substring(0,1).toUpperCase()+getSysCode().substring(1), "");
  }

  @Override
  public String toString() {
    return "ApiInfo{" +
        "sysCode='" + sysCode + '\'' +
        ", moduleName='" + moduleName + '\'' +
        ", sModuleName='" + sModuleName + '\'' +
        ", tableName='" + tableName + '\'' +
        ", methodDec='" + methodDec + '\'' +
        ", methodName='" + methodName + '\'' +
        ", noPrefixMethodName='" + noPrefixMethodName + '\'' +
        ", url='" + url + '\'' +
        ", methodPrefix='" + methodPrefix + '\'' +
        '}';
  }



}
