#import "DyPlugin.h"
#import <DouyinOpenSDK/DouyinOpenSDKApplicationDelegate.h>
#import <DouyinOpenSDK/DouyinOpenSDKAuth.h>

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
      [self initSdk:options result:result];
  }else if([@"loginInWithDouyin" isEqualToString:call.method]){
//      [self loginInWithDouyin:options result:result];
      [self loginWithDy];
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
   bool ifSendAuth= [request sendAuthRequestViewController:[self viewController] completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {
        NSLog(@"进入了回调了");
    }];
}

#pragma mark - Action
// 抖音登录
- (void) loginInWithDouyin:(NSDictionary *)options result:(FlutterResult) result{
    DouyinOpenSDKAuthRequest *request = [[DouyinOpenSDKAuthRequest alloc] init];
        request.permissions = [NSOrderedSet orderedSetWithObjects:@"user_info",  nil];
        [request sendAuthRequestViewController:[self viewController]  completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {

            NSLog(@"回调");
            NSString *alertString = nil;
            if (resp.errCode == 0) {
                alertString = [NSString stringWithFormat:@"Author Success Code : %@, permission : %@",resp.code, resp.grantedPermissions];
//                resp.errCode = 200;
//                result(@"success");
                NSLog(@"%@", alertString);
                [self.channel invokeMethod: @"getAccessToken" arguments:alertString];


            } else{
                alertString = [NSString stringWithFormat:@"Author failed code : %@, msg : %@",@(resp.errCode), resp.errString];
//                result(@"error");
                NSLog(@"%@", alertString);

            }
            [self.channel invokeMethod: @"getAccessToken" arguments:alertString];
        }];
}


@end






