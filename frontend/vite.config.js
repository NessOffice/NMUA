import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  base: "https://nessoffice.github.io/NMUA/",
  plugins: [vue()]
})
