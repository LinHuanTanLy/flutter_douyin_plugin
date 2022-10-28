#import "DyPlugin.h"
#import <DouyinOpenSDK/DouyinOpenSDKApplicationDelegate.h>
#import <DouyinOpenSDK/DouyinOpenSDKAuth.h>
#import <DouyinOpenSDK/DouyinOpenSDKShare.h>

@implementation DyPlugin


static DyPlugin *instance=nil;


+(DyPlugin *) singleInstace{
    if(!instance){
        UIViewController *viewController=[UIApplication sharedApplication].delegate.window.rootViewController;
        instance = [[DyPlugin alloc] initWithViewController:viewController];
    }
    return instance;
}
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"dy"
            binaryMessenger:[registrar messenger]];

    DyPlugin*instance=[DyPlugin singleInstace];
    instance.channel=channel;
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (instancetype)initWithViewController:(UIViewController *)viewController {
    self = [super init];
    if (self) {
        self.viewController = viewController;
    }
    return self;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSDictionary *options = call.arguments;


  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  }else if([@"initSdk" isEqualToString:call.method]){
      ///初始化sdk
      [self initSdk:options result:result];
  }else if([@"loginInWithDouyin" isEqualToString:call.method]){
      ///拿到微信授权
      [self loginWithDy];
  }else if([@"shareToPublishPage" isEqualToString:call.method]){
      ///跳转到分享页
      [self shareToEditPage:options];
      
  }
  
  
  else {
    result(FlutterMethodNotImplemented);
  }
}



#pragma mark - Action
// 初始化sdk
- (void) initSdk:(NSDictionary *)options result:(FlutterResult)result{
    NSString *clientKey=options[@"clientKey"];
    BOOL initResult= [[DouyinOpenSDKApplicationDelegate sharedInstance] registerAppId:clientKey];
    NSString *formatResult=((void)(@"%@"), initResult?@"true":@"false");
    NSLog(@"%@", formatResult);
    result(formatResult);

}

#pragma mark - Action
- (void) loginWithDy{
    DouyinOpenSDKAuthRequest *request=[[DouyinOpenSDKAuthRequest alloc] init];
    request.permissions=[NSOrderedSet orderedSetWithObject:@"user_info"];
    [request sendAuthRequestViewController:[self viewController] completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {
       NSString *alertString = nil;
       if(resp.errCode==0){
           ///拿到code了
           NSString *code=resp.code;
           NSDictionary *resultMap=@{@"authCode":code};
           [self->_channel invokeMethod:@"getAccessToken" arguments:resultMap];
           alertString = [NSString stringWithFormat:@"Author Success Code : %@, permission : %@",resp.code, resp.grantedPermissions];
           NSLog(@"result is %@",alertString);
       }else{
           ///失败了
       }
    }];
}

#pragma mark - Action
- (void) shareToEditPage:(NSDictionary *) options{
    NSArray *tempImgList=options[@"imgPathList"];
    NSArray *tempVideoList=options[@"videoPathList"];
    NSLog(@"tempImgList is %@",tempImgList);
    NSLog(@"tempVideoList is %@",tempVideoList);
    DouyinOpenSDKShareRequest *req = [[DouyinOpenSDKShareRequest alloc] init];
    req.mediaType=DouyinOpenSDKShareMediaTypeImage;
    req.localIdentifiers=[[NSArray alloc] initWithObjects:tempImgList, nil];;
    req.landedPageType=DouyinOpenSDKLandedPagePublish;
    
   [req sendShareRequestWithCompleteBlock:^(DouyinOpenSDKShareResponse * _Nonnull response) {
       NSLog(@"进到分享的回调里面了");
       NSString *result=nil;
        result=response.description;
        if(response.isSucceed){
            
        }else{
            
        }
        NSLog(@"result is %@",result);
    }];

    NSLog(@"方法结束了");
}




@end






