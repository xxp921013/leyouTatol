package xu.leyou.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xu.leyou.dto.CartDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private Long addressId;
    @NotNull
    private Integer paymentType;
    @NotNull
    private List<CartDto> carts;
}
