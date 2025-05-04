package org.kai.interfaces.api.common;

/**
 * 通用API响应结果
 *
 * @param <T> 数据类型
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应
     * 
     * @return 响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }
    
    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    /**
     * 失败响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }
    
    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    /**
     * 参数错误响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message);
    }
    
    /**
     * 未授权响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message);
    }
    
    /**
     * 权限不足响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message);
    }
    
    /**
     * 资源不存在
     *
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message);
    }
    
    // Getter and Setter
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
