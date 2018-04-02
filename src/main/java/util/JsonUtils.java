package util;

import java.io.*;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
  public static void WriteJSONResponse(HttpServletResponse response,
                                       boolean success, String err) {
    response.setContentType("application/json");
    PrintWriter out = null;
    try {
      JSONObject json_obj = new JSONObject();
      json_obj.put("success", success);
      if (!success) {
        json_obj.put("error", err);
      }
      out = response.getWriter();
      out.write(json_obj.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  public static void WriteJSONResponse(HttpServletResponse response,
                                       JSONObject result) {
    response.setContentType("application/json");
    PrintWriter out = null;
    try {
      JSONObject json_obj = new JSONObject();
      json_obj.put("success", true);
      json_obj.put("result", result);
      out = response.getWriter();
      out.write(json_obj.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  public static void WriteJSONResponse(HttpServletResponse response,
                                       Map<String, Object> result) {
    response.setContentType("application/json");
    PrintWriter out = null;
    try {
      JSONObject json_obj = new JSONObject();
      json_obj.put("success", true);

      JSONObject result_json = new JSONObject();
      for (Map.Entry<String, Object> entry : result.entrySet()) {
        result_json.put((String)entry.getKey(), entry.getValue());
      }
      json_obj.put("result", result_json);
      out = response.getWriter();
      out.write(json_obj.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}