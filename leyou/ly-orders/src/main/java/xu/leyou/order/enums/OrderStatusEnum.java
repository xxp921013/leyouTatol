package xu.leyou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrderStatusEnum {
    UN_PAY(1, "未付款"),
    PAY_NOT_DELIVER(2, "已付款,未发货"),
    UN_CONFIRM(3, "已发货,未确认"),
    SUCCESS(4, "交易成功"),
    CLOSED(5, "已关闭"),
    RATED(6, "已评价,交易结束"),
    ;
    private int code;
    private String desc;

//    OrderStatusEnum(int code, String desc) {
//        this.code = code;
//        this.desc = desc;
//    }

    public int value() {
        return this.code;
    }
}
