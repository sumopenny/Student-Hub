// 学生信息类型（完整）
export class Student {
  constructor() {
    this.id = null;       // 学生ID
    this.name = '';       // 学生姓名
    this.gender = '';     // 性别
    this.age = 0;         // 年龄
    this.address = '';    // 地址
    this.phone = '';      // 电话号码
    this.username = '';   // 用户名
    this.password = '';   // 密码
  }
}

// 学生列表VO（列表展示用）
export class StudentVO {
  constructor() {
    this.id = null;       // 学号
    this.avatar = '';     // 头像
    this.username = '';   // 用户名
    this.name = '';       // 姓名
    this.gender = '';     // 性别
    this.age = 0;         // 年龄
    this.address = '';    // 地址
    this.phone = '';      // 电话
  }
}

// 登录信息类型
export class LoginInfo {
  constructor() {
    this.id = null;       // 用户ID
    this.username = '';   // 用户名
    this.name = '';       // 真实姓名
  }
}

// 分页结果类型
export class PageResult {
  constructor() {
    this.total = 0;       // 总记录数
    this.rows = [];       // 数据列表
  }
}

// 通用响应类型
export class Result {
  constructor() {
    this.code = 0;        // 状态码，1成功，0失败
    this.msg = '';        // 消息
    this.data = null;     // 数据
  }
}