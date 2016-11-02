package com.ninethree.playchannel.webservice;

import android.content.Context;
import android.widget.Toast;

import com.ninethree.playchannel.bean.AscPackIntResult;
import com.ninethree.playchannel.bean.AscPackResult;
import com.ninethree.playchannel.util.ByteUtil;
import com.ninethree.playchannel.util.Constants;
import com.ninethree.playchannel.util.GZipUtil;
import com.ninethree.playchannel.util.Log;
import com.ninethree.playchannel.util.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

import java.util.Map;

/**
 * @ClassName: DBPubService
 * @Description: 此类用于获取公共的数据，即不需要登录就能获取的数据
 */
public class DBPubService {

    /**
     * @param context
     * @param param
     * @return SoapObject
     * @throws
     * @Title: getPubDB
     * @Description: 获取公开DB数据
     */
    public static SoapObject getPubDB(Context context, String param) {


        String url = "http://m.93966.net:1210/AnWcfDbPub/DbPubService.svc";
        String soapAction = "http://m.93966.net:1210/DbPubService/AndroidDbPub";
        String methodName = "AndroidDbPub";

        SoapObject result = WebServiceUtil.loadResult(url, soapAction,
                methodName, param);

        return result;
    }

    public static SoapObject getOrg(Context context, String param) {

        String url = "http://m.93966.net:1210/AnWcfDbPub/DbPubService.svc";
        String soapAction = "http://m.93966.net:1210/DbPubService/UserOrg_QueryAll_ByUserID";
        String methodName = "UserOrg_QueryAll_ByUserID";

        SoapObject result = WebServiceUtil.loadResult(url, soapAction,
                methodName, param);

        return result;
    }

    public static SoapObject getSessionInfo(String param) {

        String url = "http://m.93966.net:1210/AnWcfDbPub/DbPubService.svc";
        String soapAction = "http://m.93966.net:1210/DbPubService/GetSessionInfo";
        String methodName = "GetSessionInfo";

        SoapObject result = WebServiceUtil.loadResult(url, soapAction,
                methodName, param);

        return result;
    }

    public static SoapObject getRetLoginGuid(String param) {

        String url = "http://m.93966.net:1210/AnWcfDbPub/DbPubService.svc";
        String soapAction = "http://m.93966.net:1210/DbPubService/RetLoginGuid";
        String methodName = "RetLoginGuid";

        SoapObject result = WebServiceUtil.loadResult(url, soapAction,
                methodName, param);

        return result;
    }

    /**
     * @return String
     * @throws
     * @Title: pubDbParamPack
     * @Description: 公开DB参数打包
     */
    public static String pubDbParamPack(Context context, int returnId,
                                        String dbId, String procedureName, Map<String, String> params) {

        byte[] bytes = new byte[0];

        // 打包GUID
        bytes = ByteUtil.ascPackUp(bytes, Constants.PUBLIC_GUID);
        // 打包returnId 1
        bytes = ByteUtil.ascPackUp(bytes, returnId);
        // 打包dbId
        bytes = ByteUtil.ascPackUp(bytes, dbId);
        // 打包procedureName
        bytes = ByteUtil.ascPackUp(bytes, procedureName);

        if (params != null && params.size() > 0) {
            // 打包参数个数
            bytes = ByteUtil.ascPackUp(bytes, params.size());
            // 打包参数
            bytes = ByteUtil.ascPackUp(bytes, ByteUtil.getBytes(params));
        }

        // 转为Base64
        String param = Base64.encode(bytes);
        //Log.i("param:" + param);

        return param;
    }

    public static String dbParamPack(Context context, String id) {

        byte[] bytes = new byte[0];

        bytes = ByteUtil.ascPackUp(bytes, id);

        String param = Base64.encode(bytes);
        //Log.i("param:" + param);
        return param;
    }

    public static String ascPackDown(Context context, String result) {
        // Base64转byte[]
        byte[] resultByte = Base64.decode(result);
        // 解Guid
        AscPackResult results = ByteUtil.ascPackDownGuid(resultByte);
        results = ByteUtil.ascPackDownBoolean(results.getResultBytes());
        boolean bool = (boolean) results.getResultValue();
        Log.i("bool:" + bool);
        //Log.i("data:" + results.getResultBytes().length);
        if (bool) {// 请求成功

            // 拆包byte[]
            AscPackIntResult byteLengthResult = ByteUtil
                    .ascPackDownBytes(results.getResultBytes());
            //Log.i("byte[]长度:" + byteLengthResult.resultValue);
            // 解压byte[]
            byte[] dataByte = GZipUtil
                    .unGZip(byteLengthResult.resultBytes);
            // byte转String
            String dataStr = ByteUtil.byteToString(dataByte);
            Log.i("dataStr:" + dataStr);
            return dataStr;
        } else {// 请求失败
            results = ByteUtil.ascPackDownString(results
                    .getResultBytes());
            String resultData = results.getResultValue().toString();
            Log.i("请求失败:" + resultData);
            try {
                JSONObject jsonObject = new JSONObject(resultData);
                String msg = jsonObject.optString("msg");
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
