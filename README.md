app
    Application类
common
    包含util, config, constant等通用包和类.
data
    包含model, api, db, pref, 网络接口实现等.
di
    依赖注入相关的类.
    根据dagger2的风格, 一般有module和component模块.
presenter
    里面根据业务模块划分.
ui
    包含UI层的所有东东. activity, fragment, widget, dialog, adapter等, 根据需求不同分包方式有出入.

    集成bugly热更新之后出现以下错误

    Error:A problem occurred configuring project ':app'. Failed to notify project evaluation listener. Tinker does not support instant run mode, please trigger build by assembleDebug or disable instant run in 'File->Settings...'. can't find tinkerProcessDebugManifest, you must init tinker plugin first!

    A： tinker不支持instant run模式，你需要找到File->Settings->Build,Execution,Deployment->instant run并关闭，日常调试可以tinker关闭来使用instant run。