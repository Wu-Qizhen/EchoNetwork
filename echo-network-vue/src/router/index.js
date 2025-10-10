/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.9
 */
import {createRouter, createWebHistory} from 'vue-router'
import {isAuthorized} from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '/',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                }, {
                    path: '/register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                },
                {
                    path: '/reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        },
        {
            path: '/index',
            name: 'index',
            component: () => import('@/views/IndexView.vue')
        },
        {
            path: '/service-terms',
            name: 'service-terms',
            component: () => import('@/views/ServiceTermsView.vue')
        },
        {
            path: '/privacy-policy',
            name: 'privacy-policy',
            component: () => import('@/views/PrivacyPolicyView.vue')
        },
    ]
})

router.beforeEach((to, from, next) => {
    const authorized = isAuthorized();
    if (to.name.startsWith('welcome-') && authorized) {
        next('/index')
    } else if (to.fullPath.startsWith('/index') && !authorized) {
        next('/')
    } else {
        next()
    }
})

export default router
