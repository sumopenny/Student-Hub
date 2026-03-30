import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 使用代理路径
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 检查是否有code字段
    if (res && typeof res.code !== 'undefined') {
      // 不同的后端可能有不同的code约定，这里做兼容处理
      // 如果code为0或错误码，显示错误信息
      if (res.code === 0 || res.code === 'error' || res.code === 'fail') {
        ElMessage.error(res.msg || '请求失败')
        return Promise.reject(new Error(res.msg || '请求失败'))
      }
    }
    // 返回响应数据
    return res
  },
  error => {
    // 处理错误
    if (error.response) {
      // 服务器返回错误状态码
      switch (error.response.status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          // 清除本地存储，跳转到登录页
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求地址不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(error.response.data.msg || '请求失败')
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      // 请求配置出错
      ElMessage.error('请求配置错误')
    }
    return Promise.reject(error)
  }
)

export default service