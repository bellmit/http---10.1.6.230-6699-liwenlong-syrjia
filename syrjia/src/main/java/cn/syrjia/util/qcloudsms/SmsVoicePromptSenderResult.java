package cn.syrjia.util.qcloudsms;

import org.json.JSONException;
import org.json.JSONObject;

import cn.syrjia.util.qcloudsms.httpclient.HTTPResponse;

public class SmsVoicePromptSenderResult extends SmsResultBase {

    public int result;
    public String errMsg;
    public String ext;
    public String callid;

    public SmsVoicePromptSenderResult() {
        this.errMsg = "";
        this.ext = "";
        this.callid = "";
    }

    @Override
    public SmsVoicePromptSenderResult parseFromHTTPResponse(HTTPResponse response)
            throws JSONException {

        JSONObject json = parseToJson(response);

        result = json.getInt("result");
        errMsg = json.getString("errmsg");

        if (json.has("ext")) {
            ext = json.getString("ext");
        }
        if (json.has("callid")) {
            callid = json.getString("callid");
        }

        return this;
    }
}
