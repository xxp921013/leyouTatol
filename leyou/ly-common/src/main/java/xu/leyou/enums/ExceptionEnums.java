package xu.leyou.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400, "价格不能为空!"),
    CATEGORY_NOT_FIND(404, "商品分类未查到"),
    BRAND_NOT_FIND(404, "品牌未找到"),
    ADD_BRAND_ERROR(500, "品牌增加失败"),
    ADD_BRAND_CATEGORY_ERROR(500, "品牌分类添加失败"),
    UPLOAD_ERROR(500, "文件上传失败"),
    SPEC_GROUP_NOT_FIND(500, "未找到规格组"),
    ADD_SPEC_GROUP_ERROR(500, "添加组失败"),
    FIND_SPEC_PARAM_ERROR(500, "未找到规格属性"),
    ADD_SPEC_PARAM_ERROR(500, "添加规格属性失败"),
    UPDATE_SPEC_PARAM_ERROR(500, "修改规格属性失败"),
    DELETE_SPEC_PARAM_ERROR(500, "删除规格属性失败"),
    CAN_NOT_FIND_SPU(500, "搜索不到该类商品"),
    ADD_SKU_ERROR(500, "添加sku失败"),
    ADD_STOCK_ERROR(500, "添加库存失败"),
    ADD_SPU_ERROR(500, "添加spu失败"),
    ADD_SPU_DETAIL_ERROR(500, "添加spu细节失败"),
    FIND_SKU_ERROR(500, "未找到sku"),
    UPDATE_SPU_ERRPR(500, "修改spu失败"),
    CHECK_USER_TYPE(400, "请求参数有误"),
    Wrong_USER_CODE(400, "错误的验证码"),
    FIND_USER_ERROR(400, "用户名密码错误"),
    USER_TOKEN_CREATE_ERROR(500, "用户令牌生成失败"),
    UN_AUTHORIZED(403, "用户未授权"),
    CART_NOT_FOUND(404, "购物车不存在"),
    ORDER_CREATE_ERROR(500, "订单创建失败"),
    STOCK_IS_EMPTY(500, "库存不足"),
    ORDER_FIND_ERROR(404, "查询订单失败"),
    ORDER_DETAIL_FIND_ERROR(404, "查询订单详情失败"),
    SIGN_ERROR(500, "签名错误"),
    ;

    private int code;
    private String msg;
}
