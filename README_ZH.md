[![English Document](https://img.shields.io/badge/Language-English-green.svg)](README.md) ![](https://img.shields.io/badge/版本-1.1.0-green.svg)



# TinyPNG 简介

TinyPNG 是一个在线压缩 PNG 或 JPEG 格式图片的工具，同时压缩质量比较好。

TinyPNG 官网地址：[https://tinypng.com/](https://tinypng.com/)

附上一张官网的截图：

![](readme/skypegmwcn.png)

<br/>

## 前言

当时写这个脚本的原因主要是网上很多的 TinyPNG 的脚本都是用 Python 写，使用时需要 Python 的环境以及安装 tinify 包，太多麻烦，然后看到 TinyPNG 官网有 Java 的 API 接口，就决定自己动手写一个 jar 封装这些麻烦，只需一个 jar 就能实现图片压缩。

<br/>

## 准备工作

**一、**  到 [TinyPNG](https://tinypng.com/developers) 申请 `API_KEY`，注意：每个 `API_KEY` 每个月只能压缩 500 张图片，当然你用多个邮箱申请多个 `API_KEY`，每次压缩的图片大小不能超过 5 M。

**二、**  拉取 TinyImg 工程，并用 IDEA 打开，定位到 `TinyHelper.java` 类
将 `FC8dkDxbNVlJHL9JpmfCT0YzzRgVXZXT` 替换你的 `API_KEY`，如下：

```
static {
    setKey("FC8dkDxbNVlJHL9JpmfCT0YzzRgVXZXT");
}
```

**三、**  菜单栏选择 Build -> Build Artifacts -> TinyImg.jar -> Build，生成的 tinyimg.jar 就可以直接用了，如下：

![](readme/guide.png)

<br/>

**如果想偷懒可以试试下面的方法：**

1. 直接下载已经构建好的  [tinyimg.jar](https://raw.githubusercontent.com/ytempest/TinyImg/master/tinyimg.jar)  文件，这个文件内置了一个 `API_KEY`，但不确定是否还有效，建议还是替换掉。
2. 以压缩文件的方式打开，注意不要解压！！！找到里面的  `com/ytempest/tinyimg/TinyHelper.class` 文件，将这个文件解压出来
3. 用 notepad++ 打开这个 `class` 文件【其他编辑器也可以，只要编码格式是 `ANSI` 就可以】，
搜索`FC8dkDxbNVlJHL9JpmfCT0YzzRgVXZXT`，将其替换成你的 `API_KEY` 后保存
4. 将 [tinyimg.jar](tinyimg.jar) 文件中原来的 `TinyHelper.class` 文件替换成我们修改后的 `TinyHelper.class` 文件，这样就可以直接使用了！！！

猜测：Java 源文件编译成 `class` 文件时字符串资源是不会被修改的，只是编码格式改变了，需要注意的是通过这种方式修改的字符串长度必须和源文件的字符串长度保持一致，否则会破坏 `class` 文件的运行

<br/>

## 使用方法

### 一、环境

确保 JDK 环境，没有请自行安装配置

<br/>

### 二、使用说明

#### 脚本选项

|选项|参数|说明|示例|
| :---- | -------- | ---------------------------------- | --------------------------------------- |
|`-h`||帮助手册|`java -jar tinyimg.jar`|
|   |          | 压缩当前文件夹下图片文件       | `java -jar tinyyimg.jar`       |
| `－k` | `APK_KEY` | 从 TinyPNG 申请的 `API_KEY` | `java -jar tinyimg.jar -k API_KYE` |
| `－i` | 输入路径 | 图片路路径或文件夹路径 | `java -jar tinyimg.jar -i inputPath` |
| `-o` | 输出路劲 | 图片路径或文件夹路径 | `java -jar tinyimg.jar -o outputPath` |
| `-r` |  | 输入为文件夹时压缩文件夹下所有图片 | `java -jar tinyimg.jar -r` |

<br/>

#### 使用示例

##### 1、压缩单个图片文件：

示例：`java -jar tinyimg.jar -i inputImgPath -o outputImgPath`

说明：

- `inputImgPath `：需要压缩的图像文件路径

- `outputImgPath`：压缩后的图像输出路径【可选，不设置默认覆盖原文件】

<br/>

##### 2、压缩文件夹下的图片文件：

示例1：`java -jar tinyimg.jar`

说明：

- 默认压缩当前目录下的图片文件，并覆盖原文件；
- 如果你想压缩当前目录下的图片文件，并输出到指定目录，你可以用：
  `java -jar tinyimg.jar -i . -o outputDirPath`

<br/>

示例2：`java -jar tinyimg.jar -i inputDirPath -o outputDirPath`

说明：

- `inputDirPath  `：需要压缩的图像文件夹

- `outputDirPath`：压缩后的图像输出文件夹【可选，不设置默认覆盖原文件】

<br/>

#####  3、指定 TinyPNG 的 `API_KEY`

示例：`java -jar tingyimg.jar -s FC8dkDxbNVlJHL9JpmfCT0YzzRgVXZXT`

<br/>

#####  4、使用递归压缩文件夹下所有

示例：`java -jar tinypng.jar -r`

<br/>

## 三、更新日志

- v1.0 支持压缩单个图片文件和批量图片文件
- v1.1 添加命令选项支持，完善流程
  - 支持指定 TinyPNG 的 `API_KEY`
  - 支持递归压缩文件夹下所有图片文件

<br/>