import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.CardManagementVO;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 0214
 * @createTime 2018/10/11
 * @description
 */
public class GongAnTest {
    private static String SERVICE_ID_01="S10-10000001";//单个人
    private static String SERVICE_ID_08="S10-10000008";//批量
    private static String SENDER_ID_06="C20-10000006";//请求方资源编号
    /**
     * 请求方ID
     */
    public static final String SENDER_ID = "senderId";
    /**
     * 服务方ID
     */
    public static final String SERVICE_ID = "serviceId";
    /**
     * 授权信息
     */
    public static final String AUTHORIZE_INFO = "authorizeInfo";
    /**
     * 请求的webService接口方法
     */
    public static final String METHOD = "method";
    /**
     * 请求的webService接口方法的参数
     */
    public static final String METHODPARAMETER = "params";
    /**
     * 操作信息
     */
    public static final String OPERATE = "operate";

    @Test
    public void test() throws Exception {
        getGonganInfo("542421196809070045");
    }

    public String post(JSONObject jObject, String url) throws Exception {
        InputStream           in     = null;
        ByteArrayOutputStream barray = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            conn.setConnectTimeout(600000);
            conn.setReadTimeout(600000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.write(jObject.toString().getBytes("utf-8"));
            // 接收返回信息
            in = new DataInputStream(conn.getInputStream());
            byte[] array = new byte[4096];
            int count = -1;
            barray = new ByteArrayOutputStream();
            while (-1 != (count = in.read(array))) {
                barray.write(array, 0, count);
            }
            byte[] data = barray.toByteArray();
            barray.close();
            String ret = new String(data, "utf-8");
            return ret;
        } catch (Exception e) {
            throw new Exception("HttpURLConnection调用公安接口异常！", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (barray != null) {
                try {
                    barray.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public String post(String serviceId, String senderId, String method, String authorizeInfo, String operate,JSONObject methodParameter, String url) throws Exception {
        JSONObject jObject = new JSONObject();
        jObject.put(SERVICE_ID, serviceId);
        jObject.put(SENDER_ID, senderId);
        jObject.put(AUTHORIZE_INFO, authorizeInfo);
        jObject.put(METHOD, method);
        jObject.put(METHODPARAMETER, methodParameter);
        jObject.put(OPERATE, operate);
//		System.out.println("====请求报文："+jObject.toString()+"====");
        return post(jObject, url);
    }
    public CardManagementVO getGonganInfo(String certNum) throws Exception {
		/*String name = "熊英杰";
		String certNum="542421196809070045";*/
        try {
            // String url = PropertyUtils.getInstance().get("");// http://111.11.198.237:84/pdn/service
//			logger.info("==========url:"+url);
            String url = "http://113.62.126.160:84/pdn/service";
            String serviceId= SERVICE_ID_01;//获取个人信息
            String senderId = SENDER_ID_06;
            String method = "Query";
            String authorizeInfo = "EFKfskoGWUWM";

            JSONObject operate = new JSONObject();
            operate.put("userId", "540000");
            operate.put("userName", "西藏自治区");
            operate.put("userDept", "540000");
            operate.put("macIp", "192.168.12.185");


            JSONObject endUser = new JSONObject();
            endUser.put("UserCardId", "540000");
            endUser.put("UserName", "西藏自治区");
            endUser.put("UserDept", "540000");


            JSONObject methodParameter = new JSONObject();
            methodParameter.put("EndUser", endUser);
            methodParameter.put("Method", "Query");
            /*
             * 戴尔 43030219860716477X 尚朋展 411381198401112619 谭军
             * 542335198405010013 徐树 230302196610304730
             */
//			methodParameter.put("Condition", "XM='"+name+"' and SFZH='"+certNum+"'");
            methodParameter.put("Condition", "SFZH='" + certNum + "'");
            methodParameter.put("OrderItems", "");
            // MZ,CSRQ,SFZH,HKSZD,JGSSX,CSD,CSDXZ,ZZSSXQ,ZZXZ,QTZZSSXQ,QTZZXZ
            //姓名，性别，民族，出生日期，证件号，证件有效期
            methodParameter.put("RequiredItems", "XM,SFZH,BDYY,BYQK,CSD,CSDGJ,CSDXZ,CSRQ,CYM,"
                    + "FWCS,HDQR,HKSZD,HYZK,JGGJ,JGSSX,MZ,QTZZSSXQ,QTZZXZ,SG,WHCD,XB,ZY,"
                    + "ZZSSXQ,ZZXZ");// XB,MZ,CSRQ,
            methodParameter.put("RowsPerPage", "10");
            methodParameter.put("PageNum", "1");
            methodParameter.put("InfoCodeMode", "0");
            methodParameter.put("SourceDataSet", "");
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            System.out.println("==============开始调用接口！"+sdf.format(new Date()));
            String result = this.post(serviceId, senderId, method, authorizeInfo, operate.toString(),methodParameter, url);
            // printResoult(result);
            System.out.println("==============调用接口结束！"+sdf.format(new Date()));
            CardManagementVO vo;
            JSONObject json = JSON.parseObject(result);
            JSONObject payLoad = json.getJSONObject("payLoad");
            if ("000".equals(payLoad.getString("Code"))) {
                JSONObject message = payLoad.getJSONObject("Message");
                JSONArray  reuoult = message.getJSONArray("Reuoult");
                if (reuoult.size() > 0) {
                    JSONObject person = reuoult.getJSONObject(0);
                    vo=new CardManagementVO();
                    vo.setAac147(certNum);
                    vo.setAac003(person.getString("XM"));
                    vo.setAac004(person.getString("XB"));
                    vo.setAac005(person.getString("MZ"));
                    vo.setAac006(person.getString("CSRQ"));
                    return vo;
                } else {
                    return null;
                }
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
