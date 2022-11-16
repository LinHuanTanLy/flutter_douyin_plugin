# dy

封装了部分抖音sdk功能的sdk，主要包含**抖音授权**以及**抖音发布**两个模块的功能

## 项目配置

### Android

除开Anroid必要的项目初始化外，需要在AndroidManifest.xml额外添加

```xml
   <activity
          android:name="com.ly.dy.douyinapi.DouYinEntryActivity"
          android:exported="true"
          android:launchMode="singleTask"/>
```

>  这个activity为sdk指定的自定义具体回调入口，用于在里面处理抖音回调事件，而不使用默认的回调入口

### iOS

除开iOS必要的项目初始化外，额外注意要添加**LSApplicationQueriesSchemes**以及项目的**DouyinAppID**配置，具体可以查阅[相关文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/share/ios/)

## 具体API

### 相关key初始化

```dart
  ///初始化key
  Future<String?> initKey(String clientKey, String clientSecret) {
    throw UnimplementedError('initKey() has not been implemented.');
  }
```

> 只是把key存在于插件中而已，保障在调用相关sdk之前init即可

### 抖音授权

```dart
  ///登录
  Future<String?> loginInWithDouyin(String scope) {
    throw UnimplementedError('loginInWithDouyin() has not been implemented.');
  }
```

> 参数**scope**对应抖音sdk文档里的权限获取,具体可以吃参考 [相关文档]("https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/permission/overall-permission")

### 跳转去发布抖音页面

```dart
  ///分享去编辑页面
  Future<dynamic> shareToEditPage(
      List<String> imgPathList,
      List<String> videoPathList,
      List<String> mHashTagList,
      bool shareToPublish,
      String mState,
      String appId,
      String appTitle,
      String description,
      String appUrl) {
    throw UnimplementedError('shareToEditPage() has not been implemented.');
  }
```

> 1. imgPathList为对应的图片列表，注意这里Android传的是图片路径，而iOS传的是**资源在系统相册的 local identifier**
> 
> 2. videoPathList 同理
> 
> 3. 其他参数对应[相关文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/share/ios/)

### 获取事件回调

```dart
  ///增加回调
  void addDyCallbackListener(DyCallBackListener callBackListener) {
    throw UnimplementedError(
        'addDyCallbackListener() has not been implemented.');
  }
```

> 因为安卓的授权以及分享是回调到具体的activity里面处理，所以统一通过监听回调的方式进行对宿主的通知
> 
> 并额外注意，回调的数据结构是以三段式返回的
