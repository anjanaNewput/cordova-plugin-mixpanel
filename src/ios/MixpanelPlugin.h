#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>
#import "Mixpanel.h"

@interface MixpanelPlugin : CDVPlugin
{
// empty
}

// MIXPANEL API
//@see https://mixpanel.com/site_media/doctyl/uploads/iPhone-spec/Classes/Mixpanel/index.html
-(void)alias:(CDVInvokedUrlCommand*)command;
-(void)flush:(CDVInvokedUrlCommand*)command;
-(void)identify:(CDVInvokedUrlCommand*)command;
-(void)init:(CDVInvokedUrlCommand*)command;
-(void)reset:(CDVInvokedUrlCommand*)command;
-(void)track:(CDVInvokedUrlCommand*)command;
-(void)registerSuperProperties:(CDVInvokedUrlCommand*)command;

// PEOPLE API
-(void)peopleIdentify:(CDVInvokedUrlCommand*)command;
-(void)peopleSet:(CDVInvokedUrlCommand*)command;
-(void)peopleAddPushDeviceToken:(CDVInvokedUrlCommand*)command;

@end

