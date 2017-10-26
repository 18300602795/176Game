package com.i76game.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * json数据解析工具
 * 如果要解析的json数据根元素是一个集合，则使用{@link #json2Bean(String, Type)}
 * </pre>
 */
public class JsonUtil {

	private static  JsonUtil jsonUtil;
	public static JsonUtil getJsonUtil(){
		if (jsonUtil==null){
			jsonUtil=new JsonUtil();
		}
		return jsonUtil;
	}

	
	/** 用于解析json的类 */
	//private static Gson GSON = new Gson();
	//private static Gson GSON = new GsonBuilder().serializeNulls().create();
	private Gson GSON  = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();


	
	/**
	 * 把json字符串转换为JavaBean。如果json的根节点就是一个集合，则使用此方法<p>
	 * type参数的获取方式为：Type type = new TypeToken<集合泛型>(){}.getType();
	 * @param json json字符串
	 * @return type 指定要解析成的数据类型
	 */
	public  <T> T json2Bean(String json, Type type) {
		T bean = null;
		try {
			bean = GSON.fromJson(json, type);
			GSON.toJson(type);
		} catch (Exception e) {
			Log.i("JsonUtil", "解析json数据时出现异常\njson = " + json, e);
		}
		return bean;
	}

	/**
	 * json转换成列表
	 *7
	 * @param data
	 * @param class1
	 * @return
	 */
	public static <T> List<T> parseList(String data, Class<T> class1) {
		if (TextUtils.isEmpty(data)) {
			return null;
		}
		List<T> mList = new ArrayList<T>();
		try {
			JSONArray mArray = new JSONArray(data);
			final int size = mArray.length();
			for (int i = 0; i < size; i++) {
				T t = parse(mArray.getJSONObject(i).toString(), class1);
				mList.add(t);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mList;
	}

	/**
	 * json转换成类
	 *
	 * @param data
	 * @param class1
	 * @return
	 */
	public static <T> T parse(String data, Class<T> class1) {
		return new Gson().fromJson(data, class1);
	}



	public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
		@SuppressWarnings("unchecked")
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			Class<T> rawType = (Class<T>) type.getRawType();
			if (rawType != String.class) {
				return null;
			}
			return (TypeAdapter<T>) new StringNullAdapter();
		}
	}



	public class StringNullAdapter extends TypeAdapter<String> {
		@Override
		public String read(JsonReader reader) throws IOException {
			// TODO Auto-generated method stub
			if (reader.peek() == JsonToken.NULL) {
				reader.nextNull();
				return "";
			}
			return reader.nextString();
		}
		@Override
		public void write(JsonWriter writer, String value) throws IOException {
			// TODO Auto-generated method stub
			if (value == null) {
				writer.value("");
				return;
			}
			writer.value(value);
		}
	}

}



