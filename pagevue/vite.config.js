import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        // target: 'http://121.196.167.74:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    // 调整 chunk 大小警告限制（可选）
    chunkSizeWarningLimit: 1000,
    rollupOptions: {
      output: {
        // 配置 manualChunks 进行代码分割
        manualChunks(id) {
          // 将 node_modules 中的依赖分包
          if (id.includes('node_modules')) {
            // Element Plus 单独打包
            if (id.includes('element-plus')) {
              return 'element-plus'
            }
            // Vue 生态相关打包在一起
            if (id.includes('vue') || id.includes('vue-router') || id.includes('pinia')) {
              return 'vue-vendor'
            }
            // 其他依赖打包为 vendor
            return 'vendor'
          }
        }
      }
    }
  }
})
