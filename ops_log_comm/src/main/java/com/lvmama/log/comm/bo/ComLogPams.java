package com.lvmama.log.comm.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志表 pojo
 */
public class ComLogPams implements Serializable {

    private static final long serialVersionUID = -2926564614919451871L;

    private SYS_NAME sysName;

    private String logType;

    private String operatorName;

    private Long parentId;

    private String parentType;

    private Long objectId;

    private String objectType;

    private Date startTime;

    private Date endTime;
    
    /**
     * 新增查询条件，供O2O使用
     */
    private List<String> operatorNameIn;
    private List<String> operatorNameNotIn;


    public enum SYS_NAME {

        VST("VST系统"),
        O2O("O2O系统"),
        DEST("DEST系统");

        private String desc;

        SYS_NAME(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append("   sysName:"+sysName+",");
        stringBuffer.append("   logType:"+logType+",");
        stringBuffer.append("   operatorName:"+operatorName+",");
        stringBuffer.append("   parentId:"+parentId+",");
        stringBuffer.append("   parentType:"+parentType+",");
        stringBuffer.append("   objectId:"+objectId+",");
        stringBuffer.append("   objectType:"+objectType+",");
        stringBuffer.append("   startTime:"+startTime+",");
        stringBuffer.append("   endTime:"+endTime+",");
        stringBuffer.append("   operatorNameIn:"+operatorNameIn+",");
        stringBuffer.append("   operatorNameNotIn:"+operatorNameNotIn);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    public List<String> getOperatorNameIn() {
        return operatorNameIn;
    }

    public void setOperatorNameIn(List<String> operatorNameIn) {
        this.operatorNameIn = operatorNameIn;
    }

    public List<String> getOperatorNameNotIn() {
        return operatorNameNotIn;
    }

    public void setOperatorNameNotIn(List<String> operatorNameNotIn) {
        this.operatorNameNotIn = operatorNameNotIn;
    }

    public SYS_NAME getSysName() {
        return sysName;
    }

    public void setSysName(SYS_NAME sysName) {
        this.sysName = sysName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
