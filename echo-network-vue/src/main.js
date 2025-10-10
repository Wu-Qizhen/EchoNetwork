/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.9
 */
import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";
import axios from "axios";
import '@/aethex/foundation/colors/colors.css'
import '@/aethex/foundation/typography/font.css'
import '@/aethex/foundation/typography/layout.css'
import '@/aethex/foundation/ui/components.css'
// import ElementPlus from 'element-plus'
// import 'element-plus/theme-chalk/dark/css-vars.css' // 引入暗黑主题

axios.defaults.baseURL = "http://localhost:8080"

const app = createApp(App)

app.use(router)
// app.use(ElementPlus)
app.mount('#app')
