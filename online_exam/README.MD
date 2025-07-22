
考生号生成规则
[版本][年份][类型][随机序列][校验位]
1位  2位   2位    10位      1位


```java 
// 生成考生编号并校验
CandidateInfo info = new CandidateInfo(ExamType.REGULAR, 2025);
String number = service.generateNumber(info);

assertEquals(16, number.length());
assertTrue(service.validateNumber(number));
```
```java
登录秘钥
  // 1. 生成密钥
String candidateId = "STU20230001";
String loginKey = SimpleKeyGenerator.generateLoginKey(candidateId);
System.out.println("生成的登录密钥: " + loginKey);

// 2. 验证密钥
boolean isValid = SimpleKeyGenerator.validateLoginKey(candidateId, loginKey);
System.out.println("密钥验证结果: " + isValid);

// 3. 错误示例
boolean isInvalid = SimpleKeyGenerator.validateLoginKey("wrongId", loginKey);
System.out.println("错误ID验证结果: " + isInvalid);
```
```java
//考生注册
// 创建考生记录时生成密钥
String candidateId = "STU" + System.currentTimeMillis();
String loginKey = SimpleKeyGenerator.generateLoginKey(candidateId);

// 存储到数据库
candidateDao.save(new Candidate(candidateId, loginKey, ...));

//考生登录
// 获取登录请求
String inputId = request.getParameter("id");
String inputPassword = request.getParameter("password");

// 从数据库获取存储的密钥
String storedKey = candidateDao.findKeyById(inputId);

// 验证密钥
if (SimpleKeyGenerator.validateLoginKey(inputId, storedKey)) {
        // 登录成功
        } else {
        // 登录失败
        }
```
