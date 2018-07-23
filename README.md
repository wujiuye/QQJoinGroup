# QQJoinGroup
QQ加群机器人

>实现的功能：
>>根据配置的关键词来搜索群并自动发送加群验证

``///////////////////////////////////////////////////////////////////``

>知识点：
>>1.AccessibilityService\
>>>@Override
>>>public void onAccessibilityEvent(AccessibilityEvent event) {\
>>>......\
>>>int eventEventType = event.getEventType();\
>>>switch (eventEventType) {\
>>>case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:\
>>>handleWindowStateChanged(event);\
>>>break;\
>>>case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:\
>>>break;\
>>>default:\
>>>break;\
>>>}\
>>>}

>>2.跨进程模拟点击事件
>>>//从下往上滑动\
>>>TouchDevice.instance.getBaseTouch()\
>>>.touch(0, touchX, 2 * itemHeight)\
>>>.move(0, touchX, (int) (1.8 * itemHeight))//模拟移动路径\
>>>.move(0, touchX, (int) (1.5 * itemHeight))\
>>>.move(0, touchX, (int) (1.3 * itemHeight))\
>>>.move(0, touchX, itemHeight)\
>>>.release();\
>>>
>>>//模拟点击
>>>TouchDevice.instance.getBaseTouch()\
>>>.touch(0, touchX, touchY)\
>>>.release();


