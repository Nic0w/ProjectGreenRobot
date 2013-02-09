/**
 * 
 */
package fr.esiea.sd.greenrobot.website.json;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author nic0w
 *
 */
public class Response {
	
	
	private final Gson gsonEngine;
	private final JsonObject response;
	
	private final Map<String, Object> arguments;
	
	/**
	 * 
	 */
	public Response() {
		this.gsonEngine = new Gson();
		this.response = new JsonObject();
		this.arguments = Maps.newHashMap();
	}
	
	public Response breakUpdateLoop() {
		this.response.addProperty("break_loop", true);
		return this;
	}
	
	public Response launchTransition() {
		this.response.addProperty("transition", true);
		return this;
	}
	
	public Response execute(String remoteJSFunction) {
		
		this.response.addProperty("execute", remoteJSFunction);
		return this;
	}
	
	public Response addArg(String key, Object value) {

		this.arguments.put(key, value);
		
		return this;
	}
	
	public void writeTo(Appendable writer) {
		
		this.response.add("args", this.gsonEngine.toJsonTree(arguments));
		
		this.gsonEngine.toJson(response, writer);
	}

}
