安装脚手架

```shell
$ npm install -g @vue/cli
```

创建vue项目

``` shell
vue create <project name>
```

创建项目时会让你手动选择一些配置，可以按照下面的配置（当然也可以不按照）：

```shell
? Please pick a preset: Manually select features

? Check the features needed for your project: Babel, Router, Vuex, Linter

? Use history mode for router? (Requires proper server setup for index fallback in production) Yes

? Pick a linter / formatter config: Basic

? Pick additional lint features: (Press <space> to select, <a> to toggle all, <i> to invert selection)Lint on save

? Where do you prefer placing config for Babel, PostCSS, ESLint, etc.? In package.json

? Save this as a preset for future projects? (y/N) N
(注意这一个，可以选择y，取一个名字，之后就可以直接用这个了)

```

回车安装完成后，我们切换到 client 目录下，执行命令

```shell
npm run serve
```

上述命令执行完成后会有类似这样的输出

```shell
...

App running at:
- Local:   http://localhost:8080/
- Network: http://172.20.10.3:8080/

...
```

在浏览器中访问

```
http://localhost:8080/
```

如果看到包含下面文字的页面

```
Welcome to Your Vue.js App
```

说明项目安装成功



参考

1. [使用 Vue + Flask 搭建单页应用](https://juejin.im/post/5cd9fc506fb9a0324c20dcbb)
2. 