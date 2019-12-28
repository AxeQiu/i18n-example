# i18n-example
利用spring aop实现异常信息国际化，同时对业务层无侵入

**异常信息国际化是指**：当程序发生异常，用于提示用户的信息，使用用户所指定的特定语言显示。用户所指定的语言在http header属性"Accept-Language"中指定。

**对业务层无侵入是指**：用于国际化的程序组件（例如MessageSource类和Locale类)不会出现在controller, service, repository等业务组件类的任何对象成员或方法传递中。

用户请求可以在http header中加入Accept-Language来指定response的语言， spring framework中可以使用java.util.Locale来接收语言信息， 例如：
```bash
curl -H "Accept-Language: zh" http://<domain>/say-hello
```


##### 对业务层有侵入的国际化实现：
Controller中：
```java
//对业务有侵入的国际化实现
@Autowired
private MyService service;

@GetMapping("/say-hello")
public ResponseEntity<?> sayHello(Locale locale) {
  log.debug("locale is: " + locale);
  String result = null;
  try {
    result = service.sayHello(locale);
    return new ResponseEntity<String>(result, HttpStatus.OK);
  } catch (Exception ex) {
    result = ex.getMessage();
     return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
```

Service中：
```java
//对业务有侵入的国际化实现
@Autowired
private MessageSource i18n;

@Service
public String sayHello(Locale locale) {
  try {
    return "HELLO";
  } catch (Exception ex) {
    throw new MyServiceException1(i18n.getMessage(messageKey, null, locale);
  }
}
```
##### 对业务层无侵入的国际化实现：
Controller中：
```java
//对业务无侵入的国际化实现
@Autowired
private MyService service;

@GetMapping("/say-hello")
public ResponseEntity<?> sayHello(Locale locale) {
  log.debug("locale is: " + locale);
  return new ResponseEntity(service.sayHello(), HttpStatus.OK);
```

Service中：
```java
//对业务无侵入的国际化实现
@Service
public String sayHello() {
  try {
    return "HELLO";
  } catch (Exception ex) {
    throw new MyServiceException1();
  }
}
```

对比两种实现，对业务层无侵入更能体现**单一职责原则**

使用aop，在切面中处理国际化逻辑，从而将其与业务层分开；同时需要使用 scope=request 的ThreadLocal<Locale>来实现切面间的Locale类传递.

Configuration中：
```java
@Bean
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public ThreadLocal<Locale> threadLocalLocale() {
  return new ThreadLocale<Locale>();
}
```

Controller Aspect中：
```java
@Autowired
private HttpServletRequest httpServletRequest;

@Autowired
private ThreadLocal<Locale> threadLocalLocale;

@Around(...)
public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
  Locale locale = httpServletRequest.getLocale();
  threadLocalLocale.set(locale);
  Object obj = null;
  try {
    log.debug("in the controller aspect, locale is: " + locale);
    obj = pjp.proceed();
    ...
  } catch (Throwable t) {
    ...
  } finally {
    threadLocalLocale.remove(); // prevent from rehash/resize of ThreadLocalMap
  }
}
```

Service Aspect中：
```java
@Autowired
ThreadLocal<Locale> threadLocalLocale;

@Around(...)
public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
  Locale locale = threadLocalLocale.get();
  Object obj = null;
  try {
    ...
  } catch (Throwable t) {
    throw new MyServiceException(i18n.getMessage(messageKey, null, locale));
  }
}
```
  
