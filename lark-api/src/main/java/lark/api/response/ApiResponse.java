package lark.api.response;

import lark.core.enums.BaseEnum;
import lark.core.lang.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author Andy Yuan
 * @date 2020/7/18
 */
@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    protected int code = BaseEnum.DefaultEnum.SUCCESS.getCode();
    protected String msg = BaseEnum.DefaultEnum.SUCCESS.getMsg();
    protected Long time = Long.MIN_VALUE;
    protected T data;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setProcessTime( Long time ) {
        this.time = time;
    }

    static class ErrorResponse extends ApiResponse {
        public ErrorResponse() {
            this.code = BaseEnum.DefaultEnum.ERROR.getCode();
            this.msg = BaseEnum.DefaultEnum.ERROR.getMsg();
            this.data = Strings.EMPTY;
        }
    }



    /***
     * @description:返回默认成功
     * @author: maze
     * @create: 2020/4/6-16:43
     * @return: cn.techwis.manager.dto.ApiResponse
     **/
    public static ApiResponse SUCCESS() {
        return ApiResponse.builder()
                .code(BaseEnum.DefaultEnum.SUCCESS.getCode())
                .msg(BaseEnum.DefaultEnum.SUCCESS.getMsg())
                .data(null).build();
    }


    /***
     * @description:返回默认成功
     * @author: maze
     * @create: 2020/4/6-16:43
     * @param data
     * @return: cn.techwis.manager.dto.ApiResponse
     **/
    public static ApiResponse SUCCESS(Object data) {
        return ApiResponse.builder()
                .code(BaseEnum.DefaultEnum.SUCCESS.getCode())
                .msg(BaseEnum.DefaultEnum.SUCCESS.getMsg())
                .data(data).build();
    }

    /***
     * @description: 返回默认失败
     * @author: maze
     * @create: 2020/4/6-16:45
     * @param
     * @return: cn.techwis.manager.dto.ApiResponse
     **/
    public static ApiResponse ERROR(Object data) {
        return ApiResponse.builder()
                .code(BaseEnum.DefaultEnum.ERROR.getCode())
                .msg(BaseEnum.DefaultEnum.ERROR.getMsg())
                .data(data).build();
    }


    /***
     * @description: 返回默认失败
     * @author: maze
     * @create: 2020/4/6-16:45
     * @param
     * @return: cn.techwis.manager.dto.ApiResponse
     **/
    public static ApiResponse ERROR() {
        return ApiResponse.builder()
                .code(BaseEnum.DefaultEnum.ERROR.getCode())
                .msg(BaseEnum.DefaultEnum.ERROR.getMsg())
                .data(null).build();
    }


    /***
     * @description: 返回具体数据
     * @author: maze
     * @create: 2020/4/6-16:54
     * @param data
     * @return: cn.techwis.manager.dto.ApiResponse
     **/
    public static ApiResponse RESULT(BaseEnum baseEnum, Object data, String... args) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(baseEnum.getMsg(), args);
        return ApiResponse.builder()
                .code(baseEnum.getCode())
                .msg(formattingTuple.getMessage())
                .data(data).build();
    }

    public static ApiResponse RESULT(BaseEnum baseEnum, String... args) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(baseEnum.getMsg(), args);
        return ApiResponse.builder()
                .code(baseEnum.getCode())
                .msg(formattingTuple.getMessage())
                .data(null).build();
    }


    public static ApiResponse EXCEPTION(BusinessException e) {
        return ApiResponse
                .builder()
                .msg(e.getMessage())
                .code(e.getCode())
                .data(null)
                .build();
    }

    public static ApiResponse EXCEPTION(BusinessException e, Object data) {
        return ApiResponse
                .builder()
                .msg(e.getMessage())
                .code(e.getCode())
                .data(data)
                .build();
    }







}