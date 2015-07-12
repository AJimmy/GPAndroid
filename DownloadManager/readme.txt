DownloadManager
/**
 * 下载管理器：
 * 1. 一个输入框，可以输入网址进行下载
 * 2. 可以实现一个隐式意图的Activity，把ACTION_VIEW实现，支持http网址的访问；（意图类型）
 * 3. 当意图Activity收到请求后，显示网址在输入框，点击“下载”按钮。
 * 4. 给服务发送消息，开始下载
 * 5. 还有一个Activity可以查看当前的下载进度。
 */
DownloadManagerTest
测试程序，里面有下载网址连接，点击隐式意图使用自己的应用程序打开并下载资源
