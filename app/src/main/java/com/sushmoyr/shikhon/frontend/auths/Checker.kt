package com.sushmoyr.shikhon.frontend.auths

fun disallowed(email: String, pass: String) : Boolean{
    return email.isEmpty() && pass.isEmpty()
}