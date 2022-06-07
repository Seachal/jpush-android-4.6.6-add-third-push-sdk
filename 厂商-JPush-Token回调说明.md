# JPush SDK 厂商通道Token回调说明


## 概述

在国内 Android 生态中，推送通道都是由终端与云端之间的长链接来维持，严重依赖于应用进程的存活状态。如今一些手机厂家会在自家 rom 中做系统级别的推送通道，再由系统分发给各个 app，以此提高在自家 rom 上的推送送达率。各个rom均提供了不同的回调token的接口，JPush SDK提供了统一的回调接口。

## 功能描述

###回调方式：
请参考：https://docs.jiguang.cn/jpush/client/Android/android_api/#_66

示例代码：

	@Override
	public void onCommandResult(Context context, CmdMessage cmdMessage) {
	    //注册失败+三方厂商注册回调
	    Log.e(TAG,"[onCommandResult] "+cmdMessage);
	    //cmd为10000时说明为厂商token回调
	    if(cmdMessage!=null&&cmdMessage.cmd==10000&&cmdMessage.extra!=null){
	        String token = cmdMessage.extra.getString("token");
	        int platform = cmdMessage.extra.getInt("platform");
	        String deviceName = "unkown";
	        switch (platform){
	            case 1:
	                deviceName = "小米";
	                break;
	            case 2:
	                deviceName = "华为";
	                break;
	            case 3:
	                deviceName = "魅族";
	                break;
	            case 4:
	                deviceName = "OPPO";
	                break;
	            case 5:
	                deviceName = "VIVO";
	                break;
	            case 6:
	                deviceName = "ASUS";
	                break;                
	            case 8:
	                deviceName = "FCM";
	                break;
	        }
	    Log.e(TAG,"获取到 "+deviceName+" 的token:"+token);
		}
	}
###回调时机
在sdk绑定token成功时回调,当sdk版本存在变更时会再次触发回调。

存在回调时token为null的情况:
1.当集成存在问题会回调null。
2.因为厂商服务等问题获取不到厂商token时。
