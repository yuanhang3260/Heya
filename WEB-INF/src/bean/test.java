package bean;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.*;

class test {
  static public void main(String[] args) throws JSONException {
    List<Education> list = new ArrayList<Education>();
    list.add(Education.getBuilder()
                      .setSid(0)
                      .setSchool("Shanghai JiaoTong University")
                      .setStartYear(2009)
                      .setEndYear(2013)
                      .setMajor("MicroElectronics")
                      .build());
    list.add(Education.getBuilder()
                      .setSid(1)
                      .setSchool("Haimen Middle School")
                      .setStartYear(2006)
                      .setEndYear(2009)
                      .build());
    JSONArray array = Education.toJSONArray(list);
    System.out.println(array);
    System.out.println();

    for (int i = 0; i < array.length(); i++) {
      Education education = Education.getBuilder()
                                     .buildFromJSON((JSONObject)array.get(i));
      System.out.println(education.toJSONObject());
    }
  }
}
