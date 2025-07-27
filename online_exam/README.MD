# 在线考试智能判题系统
#### 引言
> 传统的OJ系统不具备简答题批阅功能,需要人工来进行批阅,但是人工批阅由于批阅老师的能力不同,最终得分也不同,所以
> 咱们直播间学到的OJ 经过了智能判题,原本的简答题(描述题)我们通过了模版引入AI，对AI数据进行清洗,通过设置不同的
> 模版,可以让AI完成简单题的判题,而且同AI判题的给分都是稳定的


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

新增选择题
```json
{
  "examId": "1947624368519331842",
  "questionType": 1,
  "content": "Java语言的特点不包括以下哪项？",
  "options": [
    {"value": "A", "label": "面向对象"},
    {"value": "B", "label": "平台无关性"},
    {"value": "C", "label": "指针操作"},
    {"value": "D", "label": "自动内存管理"}
  ],
  "referenceAnswer": "C",
  "score": 5,
  "sort": 1
}

{
  "examId": "1947624368519331842",
  "questionType": 2,
  "content": "以下哪些是Java的基本数据类型？",
  "options": [
    {"value": "A", "label": "int"},
    {"value": "B", "label": "String"},
    {"value": "C", "label": "boolean"},
    {"value": "D", "label": "float"}
  ],
  "referenceAnswer": "A,C,D",
  "score": 5,
  "sort": 2
}

{
  "examId": "exam123",
  "questionType": 3,
  "content": "Java是一种编译型语言",
  "correctAnswer": true,
  "score": 2,
  "sort": 3,
  "createBy": "user123"
}

{
  "examId": "exam123",
  "questionType": 4,
  "content": "Java中用于表示单精度浮点数的关键字是____",
  "referenceAnswer": "float",
  "score": 3,
  "sort": 4,
  "createBy": "user123"
}

{
  "examId": "exam123",
  "questionType": 5,
  "content": "简述Java的多态性及其实现方式",
  "referenceAnswer": "多态是指同一个行为具有多个不同表现形式...",
  "score": 10,
  "sort": 5,
  "createBy": "user123"
}

```