package xu.leyou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

//使用hibernate对数据进行校验
@Table(name = "tb_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //hibernate非空
    @NotEmpty(message = "用户名不能为空")
    //hibernate长度
    @Length(min = 8, max = 16, message = "用户名长度必须在8-16位")
    private String username;// 用户名

    @JsonIgnore
    @Length(min = 8, max = 16, message = "用户名长度必须在8-16位")
    private String password;// 密码
    //正则
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号不正确")
    private String phone;// 电话
    //被注释的元素必须是一个过去的日期
    @Past
    private Date created;// 创建时间

    @JsonIgnore
    private String salt;// 密码的盐值

}