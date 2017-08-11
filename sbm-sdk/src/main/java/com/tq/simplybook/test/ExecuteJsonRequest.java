package com.tq.simplybook.test;


import org.apache.log4j.Logger;

import com.tq.simplybook.impl.ClientServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.resp.Client;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ExecuteJsonRequest {
    
    public static final Logger log = Logger.getLogger(ExecuteJsonRequest.class);

    public static final String ENDPOINT_SBM_LOGIN = "https://user-api.simplybook.me/login";
    public static final String ENDPOINT_SBM = "https://user-api.simplybook.me";
    public static final String COMAPNY_LOGIN = "phqviet93gmailcom";
    public static final String ADMIN_URL = "https://user-api.simplybook.me/admin/";
    public static final String API_KEY_SIMPLYBOOK = "";

    public static void main(String[] args) throws Exception {
        ClientServiceSbm clientService = new ClientServiceImpl();
        TokenServiceSbm tokenService = new TokenServiceImpl();

        //String token = clientService.getToken(COMAPNY_LOGIN, API_KEY_SIMPLYBOOK, ENDPOINT_SBM_LOGIN);
        String token = tokenService.getUserToken(COMAPNY_LOGIN, "admin", "",  ENDPOINT_SBM_LOGIN );
        log.info("Token : " + token);
        Client client = clientService.getClient(COMAPNY_LOGIN, ADMIN_URL, token , 1);
        log.info(client);
        //ClientReq addClient = new ClientReq(new ClientData("Vietprox", "picpicproveiet@gmail.com", "+84973508124"));
       //clientService.addClient(COMAPNY_LOGIN, "https://user-api.simplybook.me/admin/", token, addClient );
    }
}
;