<template>
  <div class="post-container">
    <div class="post-header">
      <h2 class="post-title">帖子广场</h2>
      <el-button type="primary" class="create-btn" @click="showCreateDialog = true">
        <el-icon><Edit /></el-icon>
        我要发帖
      </el-button>
    </div>

    <div class="post-list">
      <div v-for="post in posts" :key="post.id" class="post-card">
        <div class="post-card-header">
          <div class="post-user">
            <img v-if="post.userAvatar" :src="post.userAvatar" class="avatar-img" />
            <div v-else class="user-avatar">{{ post.userName?.charAt(0) || 'U' }}</div>
            <span class="user-name">{{ post.userName }}</span>
          </div>
          <span class="post-time">{{ formatTime(post.createTime) }}</span>
        </div>
        <h3 class="post-card-title">{{ post.title }}</h3>
        <p class="post-card-content">{{ post.content }}</p>
        <div v-if="post.images" class="post-images">
          <el-image 
            v-for="(img, idx) in post.images.split(',')" 
            :key="idx"
            :src="img" 
            :preview-src-list="post.images.split(',')"
            fit="cover"
            class="post-image"
          />
        </div>
        <div class="post-card-footer">
          <div class="action-btn" :class="{ 'liked': post.liked }" @click="handlePostLike(post)">
            <el-icon><StarFilled /></el-icon>
            <span>{{ post.likeCount || 0 }}</span>
          </div>
          <div class="action-btn" @click="toggleCommentSection(post.id)">
            <el-icon><ChatDotRound /></el-icon>
            <span>{{ post.commentCount || 0 }}</span>
          </div>
        </div>

        <div v-if="activeCommentPostId === post.id" class="comment-section">
          <div class="comment-input-wrapper">
            <el-input 
              v-model="newComment" 
              placeholder="说点什么..." 
              @keyup.enter="submitComment(post.id)"
            />
            <el-button type="primary" size="small" @click="submitComment(post.id)">评论</el-button>
          </div>

          <div class="comment-list">
            <div v-for="comment in getRootComments(post.id)" :key="comment.id" class="comment-item">
              <div class="comment-main">
                <img v-if="comment.userAvatar" :src="comment.userAvatar" class="comment-avatar-img" />
                <div v-else class="comment-avatar">{{ comment.userName?.charAt(0) || 'U' }}</div>
                <div class="comment-content-wrapper">
                  <div class="comment-user-info">
                    <span class="comment-user-name">{{ comment.userName }}</span>
                    <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                  </div>
                  <p class="comment-content">{{ comment.content }}</p>
                  <div class="comment-actions">
                    <span 
                      class="action-btn" 
                      :class="{ 'liked': comment.liked }" 
                      @click="handleCommentLike(comment)"
                    >
                      <el-icon><StarFilled /></el-icon>
                      <span>{{ comment.likeCount || 0 }}</span>
                    </span>
                    <span class="action-btn" @click="showReplyInput(comment)">
                      <el-icon><ChatLineRound /></el-icon>
                      回复
                    </span>
                  </div>
                  <div v-if="replyingCommentId === comment.id" class="reply-input-wrapper">
                    <el-input 
                      v-model="replyContent" 
                      :placeholder="`回复 @${comment.userName}`" 
                      size="small"
                      @keyup.enter="submitReply(post.id, comment)"
                    />
                    <el-button size="small" @click="submitReply(post.id, comment)">回复</el-button>
                    <el-button size="small" @click="cancelReply">取消</el-button>
                  </div>
                </div>
              </div>

              <div v-for="reply in getAllReplies(comment.id)" :key="reply.id" class="comment-reply">
                <img v-if="reply.userAvatar" :src="reply.userAvatar" class="comment-avatar-img" />
                <div v-else class="comment-avatar">{{ reply.userName?.charAt(0) || 'U' }}</div>
                <div class="comment-content-wrapper">
                  <div class="comment-user-info">
                    <span class="comment-user-name">{{ reply.userName }}</span>
                    <span v-if="reply.replyUserName" class="reply-at">@ {{ reply.replyUserName }}</span>
                    <span class="comment-time">{{ formatTime(reply.createTime) }}</span>
                  </div>
                  <p class="comment-content">{{ reply.content }}</p>
                  <div class="comment-actions">
                    <span 
                      class="action-btn" 
                      :class="{ 'liked': reply.liked }" 
                      @click="handleCommentLike(reply)"
                    >
                      <el-icon><StarFilled /></el-icon>
                      <span>{{ reply.likeCount || 0 }}</span>
                    </span>
                    <span class="action-btn" @click="showReplyInput(reply)">
                      <el-icon><ChatLineRound /></el-icon>
                      回复
                    </span>
                  </div>
                  <div v-if="replyingCommentId === reply.id" class="reply-input-wrapper">
                    <el-input 
                      v-model="replyContent" 
                      :placeholder="`回复 @${reply.userName}`" 
                      size="small"
                      @keyup.enter="submitReply(post.id, reply)"
                    />
                    <el-button size="small" @click="submitReply(post.id, reply)">回复</el-button>
                    <el-button size="small" @click="cancelReply">取消</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-if="posts.length === 0" description="暂无帖子，快来发帖吧~" />
    </div>

    <el-dialog 
      v-model="showCreateDialog" 
      title="我要发帖" 
      width="600px" 
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="postForm" :rules="postRules" ref="postFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input 
            v-model="postForm.title" 
            placeholder="请输入帖子标题（不超过15字）" 
            maxlength="15"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input 
            v-model="postForm.content" 
            type="textarea" 
            placeholder="请输入帖子内容（不超过300字）" 
            maxlength="300"
            :rows="5"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="图片">
          <div class="upload-wrapper">
            <el-upload
              v-model:file-list="fileList"
              action="#"
              list-type="picture-card"
              :auto-upload="false"
              :limit="9"
              :on-change="handleFileChange"
              :on-remove="handleFileRemove"
              :on-exceed="handleExceed"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">最多上传9张图片</div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPost" :loading="submitting">发帖</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, StarFilled, ChatDotRound, ChatLineRound, Plus } from '@element-plus/icons-vue'
import { 
  getPostsAPI, 
  createPostAPI, 
  togglePostLikeAPI, 
  getCommentsAPI, 
  createCommentAPI, 
  toggleCommentLikeAPI,
  uploadFileAPI 
} from '../api/index'

const posts = ref([])
const showCreateDialog = ref(false)
const postFormRef = ref(null)
const submitting = ref(false)
const fileList = ref([])
const uploadedImages = ref([])

const postForm = ref({
  title: '',
  content: ''
})

const postRules = {
  title: [
    { required: true, message: '请输入帖子标题', trigger: 'blur' },
    { max: 15, message: '标题不能超过15字', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' },
    { max: 300, message: '内容不能超过300字', trigger: 'blur' }
  ]
}

const newComment = ref('')
const activeCommentPostId = ref(null)
const commentsMap = ref({})
const replyingCommentId = ref(null)
const replyContent = ref('')

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 604800000) return Math.floor(diff / 86400000) + '天前'
  return date.toLocaleDateString()
}

const loadPosts = async () => {
  const res = await getPostsAPI()
  if (res.code === 1) {
    posts.value = res.data || []
  }
}

const handlePostLike = async (post) => {
  const res = await togglePostLikeAPI(post.id)
  if (res.code === 1) {
    post.liked = res.data.liked
    post.likeCount = res.data.likeCount
  }
}

const toggleCommentSection = async (postId) => {
  if (activeCommentPostId.value === postId) {
    activeCommentPostId.value = null
  } else {
    activeCommentPostId.value = postId
    const res = await getCommentsAPI(postId)
    if (res.code === 1) {
      commentsMap.value[postId] = res.data || []
    }
  }
}

const getRootComments = (postId) => {
  const comments = commentsMap.value[postId] || []
  return comments.filter(c => c.parentId === 0 || c.parentId === null || !c.parentId)
}

const getReplies = (parentId) => {
  const postId = activeCommentPostId.value
  const comments = commentsMap.value[postId] || []
  return comments.filter(c => c.parentId === parentId)
}

const getAllReplies = (rootId) => {
  const postId = activeCommentPostId.value
  const comments = commentsMap.value[postId] || []
  const result = []
  
  const findReplies = (parentId) => {
    const directReplies = comments.filter(c => c.parentId === parentId)
    for (const reply of directReplies) {
      result.push(reply)
      findReplies(reply.id)
    }
  }
  
  findReplies(rootId)
  return result
}

const showReplyInput = (comment) => {
  replyingCommentId.value = comment.id
  replyContent.value = ''
}

const cancelReply = () => {
  replyingCommentId.value = null
  replyContent.value = ''
}

const submitComment = async (postId) => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  const res = await createCommentAPI(postId, {
    content: newComment.value,
    parentId: 0
  })
  if (res.code === 1) {
    ElMessage.success('评论成功')
    newComment.value = ''
    const commentsRes = await getCommentsAPI(postId)
    if (commentsRes.code === 1) {
      commentsMap.value[postId] = commentsRes.data || []
    }
    loadPosts()
  }
}

const submitReply = async (postId, parentComment) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  const res = await createCommentAPI(postId, {
    content: replyContent.value,
    parentId: parentComment.id,
    replyUserId: parentComment.userId,
    replyUserName: parentComment.userName
  })
  if (res.code === 1) {
    ElMessage.success('回复成功')
    cancelReply()
    const commentsRes = await getCommentsAPI(postId)
    if (commentsRes.code === 1) {
      commentsMap.value[postId] = commentsRes.data || []
    }
  }
}

const handleCommentLike = async (comment) => {
  const res = await toggleCommentLikeAPI(comment.id)
  if (res.code === 1) {
    comment.liked = res.data.liked
    comment.likeCount = res.data.likeCount
  }
}

const handleFileChange = async (file) => {
  const isImage = file.raw.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    fileList.value = fileList.value.filter(f => f.uid !== file.uid)
    return
  }
}

const handleFileRemove = (file) => {
  fileList.value = fileList.value.filter(f => f.uid !== file.uid)
}

const handleExceed = () => {
  ElMessage.warning('最多只能上传9张图片')
}

const resetForm = () => {
  postForm.value = { title: '', content: '' }
  fileList.value = []
  uploadedImages.value = []
}

const submitPost = async () => {
  if (!postFormRef.value) return
  await postFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      if (fileList.value.length > 0) {
        const uploadPromises = fileList.value
          .filter(f => f.raw)
          .map(async (file) => {
            const res = await uploadFileAPI(file.raw)
            if (res.code === 1 && res.data && res.data.url) {
              return res.data.url
            }
            return null
          })
        
        const results = await Promise.all(uploadPromises)
        uploadedImages.value = results.filter(Boolean)
      }

      const postData = {
        title: postForm.value.title,
        content: postForm.value.content,
        images: uploadedImages.value.join(',')
      }

      const res = await createPostAPI(postData)
      if (res.code === 1) {
        ElMessage.success('发帖成功')
        showCreateDialog.value = false
        resetForm()
        loadPosts()
      } else {
        ElMessage.error(res.msg || '发帖失败')
      }
    } catch (e) {
      ElMessage.error('发帖失败')
    } finally {
      submitting.value = false
    }
  })
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.post-container {
  max-width: 900px;
  margin: 0 auto;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  color: #6b5b5b;
}

.create-btn {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
  padding: 12px 24px;
  border-radius: 24px;
}

.create-btn:hover {
  opacity: 0.9;
  transform: scale(1.02);
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.post-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.post-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.user-name {
  font-weight: 500;
  color: #6b5b5b;
}

.post-time {
  font-size: 13px;
  color: #a89a9a;
}

.post-card-title {
  font-size: 18px;
  font-weight: 600;
  color: #6b5b5b;
  margin-bottom: 10px;
}

.post-card-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
}

.post-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.post-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
}

.post-card-footer {
  display: flex;
  gap: 20px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #a89a9a;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn:hover {
  color: #d4a5a5;
}

.action-btn.liked {
  color: #f56c6c;
}

.action-btn .el-icon {
  font-size: 18px;
}

.comment-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.comment-input-wrapper {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-item {
  padding: 12px;
  background: #fafafa;
  border-radius: 12px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-avatar-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.comment-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.comment-content-wrapper {
  flex: 1;
}

.comment-user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-user-name {
  font-weight: 500;
  color: #6b5b5b;
  font-size: 14px;
}

.reply-at {
  color: #d4a5a5;
  font-size: 13px;
}

.comment-time {
  font-size: 12px;
  color: #a89a9a;
}

.comment-content {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.comment-actions .action-btn {
  font-size: 13px;
}

.comment-reply {
  display: flex;
  gap: 12px;
  margin-top: 12px;
  margin-left: 44px;
  padding: 12px;
  background: #fff;
  border-radius: 12px;
}

.reply-input-wrapper {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.upload-wrapper {
  width: 100%;
}

.upload-tip {
  font-size: 13px;
  color: #a89a9a;
  margin-top: 8px;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 100px;
  height: 100px;
}
</style>
