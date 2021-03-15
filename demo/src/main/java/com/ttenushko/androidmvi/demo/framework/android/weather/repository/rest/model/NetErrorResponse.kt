package com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.model

class NetErrorResponse(
    code: Int,
    message: String
) : NetBaseResponse(code, message)