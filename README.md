# dy

封装了部分抖音sdk功能的sdk，主要包含**抖音授权**以及**抖音发布**两个模块的功能

## 具体API

### 相关key初始化

```dart
  ///初始化key
  Future<String?> initKey(String clientKey, String clientSecret) {
    throw UnimplementedError('initKey() has not been implemented.');
  }
```

> 只是把key存在于插件中而已，保障在调用相关sdk之前init即可
