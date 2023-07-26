# Hadoop, MapReduce分布式爬取全中国大学的数据信息

<img src="https://img.shields.io/badge/license_-MIT-green" alt="">  <img src="https://img.shields.io/badge/license_-Apache-blue" alt=""> <img src="https://img.shields.io/badge/Java_-red" alt=""> <img src="https://img.shields.io/badge/Maven_-red" alt=""> 

仓库介绍

  广泛的MapReduce分布式爬虫还是推荐使用Jsoup, 但是它无法解析由JavaScrip加载而来的数据. 因此这是一个利用FastJson爬取全中国大学的数据信息的仓库, 它利用的是Hadoop生态中MapReduce分布式计算爬虫. 目前我的编程环境Windows 10，测试环境Windows 10下虚拟的hadoop暂时还没办法在linux、mac上进行测试，且目前判断linux则为hdfs路径，有兴趣请提交Issues、Pr.

![img.png](img.png)

本仓库包含以下内容：

1. Windows下模拟的分布式环境搭建
2. 爬取掌上高考
3. 数据存储


## 安装

这个项目使用 [Java](https://www.java.com/) [Git](https://git-scm.com/) 请确保你本地安装了它们。

```shell
$ git clone https://github.com/weiensong/ScrapySchoolAll.git
```



## 运行
- 真正的分布式环境
```shell
$ mvn package

# 在Master上
$ hadoop jar PackageName.jar
```
- Windows模拟的分布式环境
	- 直接以管理员运行initTest.bat
	- ```shell
		$ cd /d "%~dp0"
		$ copy hadoop.dll C:\Windows\System32
		$ cd /src/main/java/job
		$ javac MyJob.java
		$ java MyJob
		```
		
		

## 相关仓库

- [hadoop](https://github.com/apache/hadoop) —Apache Hadoop
- [allSchoolAPI](https://github.com/weiensong/allSchoolAPI) — 中国全国大学基本信息API搭建



## 相关链接

- [Hadoop](https://hadoop.apache.org/)
- [Maven中央仓库](https://mvnrepository.com/)
- [掌上高考](https://www.gaokao.cn/) 





## 维护者

[@weiensong](https://github.com/weiensong)



## 如何贡献

非常欢迎你的加入！[提一个 Issue](https://github.com/weiensong/ScrapySchoolAll/issues) 或者提交一个 Pull Request。


标准 Java 遵循 [Google apache](https://google.github.io/styleguide/javaguide.html) 行为规范。

### 贡献者

感谢参与项目的所有人



## 使用许可

[MIT](LICENSE) © weiensong

