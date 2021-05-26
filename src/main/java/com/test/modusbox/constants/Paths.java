package com.test.modusbox.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Paths {
    // GENERAL
    //THE BASE URL http://localhost:8080 SHOULD BE A PROPERTY in each environment, I WILL LEAVE IN THIS WAY FOR TEST
    public static final String BASE_URL = "http://localhost:8080";
    public static final String API_1 = "/api/v1";

    // SUPPLIERS
    public static final String SUPPLIERS_PATH = API_1 + "/suppliers";
    public static final String CREATE_SUPPLIER_PATH = "/create";
    public static final String UPDATE_SUPPLIER_PATH = "/update";
    public static final String DELETE_SUPPLIER_PATH = "/delete";
    public static final String FIND_ALL_SUPPLIERS_PATH = "/findAll";
    public static final String FIND_SUPPLIER_BY_ID ="/findById";


    // TRANSACTION
    public static final String TRANSACTION_PATH = API_1 + "/transactions";
    public static final String PROCESS_TRANSACTION_PATH = "/process";
    public static final String CREATE_TRANSACTION_PATH = "/create";

    // TOKEN
    public static final String TOKEN_PATH = API_1 + "/token";
}
