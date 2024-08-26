package com.riggle.plug.utils.event;

import com.google.gson.annotations.SerializedName;
import com.riggle.plug.data.api.ApiResponse;

public class SimpleApiResponse<T> {

    @SerializedName("error")
    protected ErrorResponse error;
    @SerializedName("status")
    protected boolean status;
    @SerializedName("success")
    protected ApiResponse<T> success;

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ApiResponse<T> getSuccess() {
        return success;
    }

    public void setSuccess(ApiResponse<T> success) {
        this.success = success;
    }

}
