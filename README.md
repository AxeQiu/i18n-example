# i18n-example
利用spring aop实现异常信息国际化，同时对业务层无侵入

异常信息国际化是指：当程序发生异常用于提示用户的信息，使用用户所指定的特定语言显示。用户所指定的语言在http header属性"Accept-Language"中指定。

对业务层无侵入是指：用于国际化的程序组件（例如MessageSource类和Locale类)不会作为controller, service, repository等业务组件类的任何对象成员或方法传递中。
