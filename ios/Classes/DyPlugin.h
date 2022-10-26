#import <Flutter/Flutter.h>
@interface DyPlugin : NSObject<FlutterPlugin>
@property (nonatomic, retain) FlutterMethodChannel *channel;
@property (nonatomic, retain) UIViewController *viewController;
@end
