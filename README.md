# rollpolling

picture roll polling never stop.


<br />
<br />


### android studio接入方式
#### 1. add the JitPack repository in your root build.gradle as follows：
![project root build.gradle](/img/root.png)


<br />

#### 2. add the dependency: 
compile 'com.github.cyfiilu:rollpolling:v1.0.2'


<br />
<br />



### use help
#### 1. use fragment label in xml.
![use fragment label in xml](/img/xml.png)

<br />
<br />


#### 2. as follows in code 
![as follows in code ](/img/code.png)
代码中可以设置一些参数：  <br />
图片高度（默认150dp）、 <br />
图片左右边距（默认10dp）、 <br />
圆角半径（默认5dp）、 <br />
指示器边距（默认5dp）、 <br />
指示器位置（默认中间底部）、 <br />
轮播时长（默认3000ms）、 <br />
开始轮播时图片的位置（默认第一张图片） <br />

这下参数设置完成后，调用startRollPolling()方法开始轮播。


<br />
<br />


#### 3. pass param as follows
![pass param as follows](/img/data.png)
传入的图片链接，只支持从网络down图片。

<br />
<br />


#### 4. click picture callback 
![click picture callback](/img/callback.png) 
<br />
点击图片的监听回调，注意：position从1开始，而不是0
