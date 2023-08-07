package com.john.shardingjdbc.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * gsp订单 entity
 *
 * @author zhangjuwa zhangjuwa@gongpin.com
 * @since 1.0.0 2022-07-18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("gsp_order")
public class GspOrderEntity {

    private static final long serialVersionUID = 1L;
    //region 数据库字段
    public static final String GSP_ORDER_NO_FIELD = "gsp_order_no";
    public static final String CONFIRM_STATUS_FIELD = "confirm_status";
    public static final String GSP_CONFIRM_STATUS_FIELD = "gsp_confirm_status";
    public static final String REQUIREMENT_COMPANY_NAME_FIELD = "requirement_company_name";
    public static final String REQUIREMENT_COMPANY_CODE_FIELD = "requirement_company_code";
    public static final String PURCHASE_ORG_FIELD = "purchase_org";
    public static final String PURCHASE_GROUP_NAME_FIELD = "purchase_group_name";
    public static final String PURCHASE_GROUP_CODE_FIELD = "purchase_group_code";
    public static final String CERT_DATE_FIELD = "cert_date";
    public static final String CONFIRM_DEADLINE_FIELD = "confirm_deadline";
    public static final String CUSTOMER_CONSIGNEE_FIELD = "customer_consignee";
    public static final String GSP_AUDIT_STATUS_FIELD = "gsp_audit_status";
    public static final String CREATE_OMS_FIELD = "create_oms";
    public static final String VERSION_FIELD = "version";
    public static final String GSP_ORDER_TYPE = "gsp_order_type";
    public static final String SALES_ASSISTANT = "sales_assistant";
    public static final String SALES_ASSISTANT_CODE = "sales_assistant_code";
    public static final String GSP_CONFIRM_TIME = "gsp_confirm_time";


    //endregion
    //region 实体字段
    /**
     *
     */
    @TableField(value = GSP_ORDER_NO_FIELD)
    private String gspOrderNo;


    /**
     * 数据来源
     */
    private String source;

    /**
     * 内部确认状态1：待确认，2：部分确认，3：全部确认
     */
    @TableField(value = CONFIRM_STATUS_FIELD)
    private Integer confirmStatus;

    /**
     * gsp确认状态1：待确认，3：已确认  4：已部分确认，5：已拒绝
     */
    @TableField(value = GSP_CONFIRM_STATUS_FIELD)
    private Integer gspConfirmStatus;

    /**
     * 采购公司名称
     */
    @TableField(value = REQUIREMENT_COMPANY_NAME_FIELD)
    private String requirementCompanyName;

    /**
     * 采购公司编码
     */
    @TableField(value = REQUIREMENT_COMPANY_CODE_FIELD)
    private String requirementCompanyCode;

    /**
     * 采购组织
     */
    @TableField(value = PURCHASE_ORG_FIELD)
    private String purchaseOrg;

    /**
     * 采购组名称
     */
    @TableField(value = PURCHASE_GROUP_NAME_FIELD)
    private String purchaseGroupName;

    /**
     * 采购组编码
     */
    @TableField(value = PURCHASE_GROUP_CODE_FIELD)
    private String purchaseGroupCode;

    /**
     * 凭证日期
     */
    @TableField(value = CERT_DATE_FIELD)
    private LocalDateTime certDate;

    /**
     * 确认截至时间
     */
    @TableField(value = CONFIRM_DEADLINE_FIELD)
    private LocalDateTime confirmDeadline;

    /**
     * 收货联系人
     */
    @TableField(value = CUSTOMER_CONSIGNEE_FIELD)
    private String customerConsignee;

    /**
     * gsp审批状态
     */
    @TableField(value = GSP_AUDIT_STATUS_FIELD)
    private String gspAuditStatus;

    /**
     * 是否创建OMS订单
     */
    @TableField(value = CREATE_OMS_FIELD)
    private Boolean createOms;

    /**
     * 版本号，每次GSP订单变更确认之后会触发这里变更
     */
    @TableField(value = VERSION_FIELD)
    private Integer version;

    /**
     * gsp取消时间
     */
    private LocalDateTime gspCancelTime;
    /**
     * gsp确认时间
     */
    private LocalDateTime gspConfirmTime;
    /**
     * gsp拒绝时间
     */
    private LocalDateTime gspRejectTime;

    /**
     * gsp拒绝原因
     */
    private String rejectReason;

    /**
     * gsp订单类型
     */
    private String gspOrderType;

    /**
     * 负责的销助
     */
    private String salesAssistant;


    /**
     * 负责的销助userCode
     */
    private String salesAssistantCode;


    /**
     * id,默认使用数据库自增ID
     */
    @TableId(value = ID, type = IdType.AUTO)
    private Long id;

    /**
     * 创建人姓名/昵称 uc_user.nickname
     */
    @TableField(value = CREATOR, fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人编码 uc_user.user_code
     */
    @TableField(value = CREATOR_CODE, fill = FieldFill.INSERT)
    private String creatorCode;

    /**
     * 创建时间
     */
    @TableField(value = CREATE_TIME, fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人姓名/昵称 uc_user.nickname
     */
    @TableField(value = UPDATER, fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新人编码 uc_user.user_code
     */
    @TableField(value = UPDATER_CODE, fill = FieldFill.INSERT_UPDATE)
    private String updaterCode;

    /**
     * 更新时间
     */
    @TableField(value = UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否有效；0：否，1：是
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(value = ACTIVE)
    private Integer active = 1;


    //region 数据库字段

    /**
     * id,默认使用数据库自增ID  数据库列名
     */
    public static final String ID = "id";
    /**
     * 创建人姓名/昵称 uc_user.nickname  数据库列名
     */
    public static final String CREATOR = "creator";
    /**
     * 创建人编码 uc_user.user_code  数据库列名
     */
    public static final String CREATOR_CODE = "creator_code";
    /**
     * 创建时间  数据库列名
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 更新人姓名/昵称 uc_user.nickname  数据库列名
     */
    public static final String UPDATER = "updater";
    /**
     * 更新人编码 uc_user.user_code  数据库列名
     */
    public static final String UPDATER_CODE = "updater_code";
    /**
     * 更新时间  数据库列名
     */
    public static final String UPDATE_TIME = "update_time";

    /**
     * 是否有效；0：否，1：是  数据库列名
     */
    public static final String ACTIVE = "active";

    //endregion
}
