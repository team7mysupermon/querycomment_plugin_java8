package Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommentAssistant {
    private String methodName;
    private String hostName;
    private String localHostName;
    private String iP;
    private String className;
    private String time;
    private String packageName;
    private String finalString;
    private final Logger logger;

    public CommentAssistant(Logger logger) {
        this.logger = logger;
    }

	public String addSystemData(String query) {
		StackTraceElement stackTraceElement=getStackTraceElement();
		JSONObject jsonObject = new JSONObject();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

		// Gets the current time
		LocalDateTime now = LocalDateTime.now();
		Class<?> c = null;
		try {
			c = Class.forName(stackTraceElement.getClassName());
			iP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		try {
//			Field changeMap = jsonObject.getClass().getDeclaredField("map");
//			changeMap.setAccessible(true);
//			changeMap.set(jsonObject, new LinkedHashMap<>());
//			changeMap.setAccessible(false);
//		} catch (IllegalAccessException | NoSuchFieldException e) {
//
//		}
		// setVariables();
		
		// Put in the order that they are in the JSON object
		jsonObject.put("IP_ADDRESS", iP);
		jsonObject.put("METHODNAME",stackTraceElement.getMethodName());
		jsonObject.put("CLASSNAME", stackTraceElement.getClassName());
		jsonObject.put("CURRENT_TIME", dtf.format(now));
		jsonObject.put("PACKAGENAME", c != null ? c.getPackage().getName() : "");

	//	System.out.println(jsonObject);
		if(!query.contains(" /*" + jsonObject + "*/")){
				finalString = query.concat(" /*" + jsonObject + "*/");
		}
		System.out.println(finalString);

		return finalString;
	}


    private void setVariables() {
        setHostName();
        setMethodName();
        setClassName();
        setPackageName();
        setTime();
        setIP();
        // OBS must be run after setIP and setHostname
        setLocalHostName();
    }

    private void setHostName() {
        hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }

    }

    private void setIP() {
        iP = "";

        try {
            iP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void setLocalHostName() {
        localHostName = hostName + "/" + iP;
    }

    private void setTime() {
        // The specific format the time should be printed in
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

        // Gets the current time
        LocalDateTime now = LocalDateTime.now();

        time = dtf.format(now);
    }

    public static StackTraceElement getStackTraceElement() {

        Class <?> c;
        Thread thread = Thread.currentThread();
        StackTraceElement[]    stacktrace = thread.getStackTrace();
        try {

            for (StackTraceElement element : stacktrace) {
                c = Class.forName(element.getClassName());
                String packagename = c.getPackage().getName();
                if (!packagename.equals("Util")&& !packagename.equals("org.springframework.jdbc.core.namedparam") && !packagename.equals("NamedJDBC") && !packagename.equals("java.lang")) {
                    return element;
                }
            }
        } catch (ClassNotFoundException e) {
          //  logger.log(Level.WARNING, e.getMessage());
        }

        return null;
    }

    private void setMethodName() {
        methodName = getStackTraceElement().getMethodName();
    }

    private void setClassName() {
        className = getStackTraceElement().getClassName();
    }

    private void setPackageName() {
        Class<?> c = null;

        try {
            c = Class.forName(getStackTraceElement().getClassName());
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

        packageName = c != null ? c.getPackage().getName() : "";
    }

    
    // Getters
    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }
    
    public String getPackageName() {
        return packageName;
    }

    public String getIP() {
        return iP;
    }

    public String getHostName() {
        return hostName;
    }

    public String getLocalHostName() {
        return localHostName;
    }
    
    public String getTime() {
        return time;
    }

    public String getFinalString() {
        return finalString;
    }
}
