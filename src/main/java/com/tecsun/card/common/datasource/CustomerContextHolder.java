package com.tecsun.card.common.datasource;

/**
 * 
 * @author tan
 *
 */
public class CustomerContextHolder {

    public static final String ZDCX_DATASOURCE = "zdcxDataSource";
    public static final String MEDIC_DATASOURCE = "medicDataSource";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType){
        contextHolder.set(customerType);
    }

    public static String getCustomerType(){
        return contextHolder.get();
    }

    public static void removeCustomerType(){
        contextHolder.remove();
    }
}
