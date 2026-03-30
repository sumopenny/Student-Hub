import request from '../utils/request'

// 登录API
export const loginAPI = (student) => {
  return request({
    url: '/login',
    method: 'post',
    data: student
  })
}

// 注册API
export const registerAPI = (student) => {
  return request({
    url: '/register',
    method: 'post',
    data: student
  })
}

// 检查用户名是否已存在
export const checkUsernameAPI = (username) => {
  return request({
    url: '/checkUsername',
    method: 'get',
    params: { username }
  })
}

// 获取学生列表API（分页查询）
export const getStudentListAPI = (params) => {
  return request({
    url: '/stus',
    method: 'get',
    params: params
  })
}

// 根据ID获取学生信息API
export const getStudentByIdAPI = (id) => {
  return request({
    url: `/stus/${id}`,
    method: 'get'
  })
}

// 新增学生API
export const addStudentAPI = (student) => {
  return request({
    url: '/stus',
    method: 'post',
    data: student
  })
}

// 更新学生API
export const updateStudentAPI = (student) => {
  return request({
    url: '/stus',
    method: 'put',
    data: student
  })
}

// 删除学生API
export const deleteStudentAPI = (id) => {
  return request({
    url: '/stus',
    method: 'delete',
    params: { id: id }
  })
}

// 修改密码API
export const updatePasswordAPI = (passwordData) => {
  return request({
    url: '/stus/updatePassword',
    method: 'post',
    data: passwordData
  })
}

// 文件上传API
export const uploadFileAPI = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 头像上传API - 上传头像并更新用户头像URL，同时删除旧头像
export const uploadAvatarAPI = (file, userId) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('userId', userId)
  return request({
    url: '/upload/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 提交留言API
export const submitMessageAPI = (messageData) => {
  return request({
    url: '/messages',
    method: 'post',
    data: messageData
  })
}

// 获取留言列表API
export const getMessageListAPI = (userId) => {
  return request({
    url: '/messages',
    method: 'get',
    params: { userId }
  })
}

// 获取访客数量API
export const getVisitorCountAPI = () => {
  return request({
    url: '/visitor/count',
    method: 'get'
  })
}

// 增加访客数量API
export const incrementVisitorCountAPI = () => {
  return request({
    url: '/visitor/increment',
    method: 'post'
  })
}

// 获取所有帖子
export const getPostsAPI = () => {
  return request({
    url: '/posts',
    method: 'get'
  })
}

// 发帖
export const createPostAPI = (postData) => {
  return request({
    url: '/posts',
    method: 'post',
    data: postData
  })
}

// 删除帖子
export const deletePostAPI = (id) => {
  return request({
    url: `/posts/${id}`,
    method: 'delete'
  })
}

// 帖子点赞/取消点赞
export const togglePostLikeAPI = (id) => {
  return request({
    url: `/posts/${id}/like`,
    method: 'post'
  })
}

// 获取帖子评论
export const getCommentsAPI = (postId) => {
  return request({
    url: `/posts/${postId}/comments`,
    method: 'get'
  })
}

// 添加评论
export const createCommentAPI = (postId, commentData) => {
  return request({
    url: `/posts/${postId}/comments`,
    method: 'post',
    data: commentData
  })
}

// 评论点赞/取消点赞
export const toggleCommentLikeAPI = (commentId) => {
  return request({
    url: `/posts/comments/${commentId}/like`,
    method: 'post'
  })
}