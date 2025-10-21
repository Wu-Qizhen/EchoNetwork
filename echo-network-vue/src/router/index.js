/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.9
 */
import {createRouter, createWebHistory} from 'vue-router'
import {isAuthorized} from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    scrollBehavior(to, from, savedPosition) {
        return {
            top: 0,
            behavior: 'smooth'
        }
    },
    routes: [
        // 欢迎
        {
            path: '/welcome',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue')
        },

        // 认证
        {
            path: '/auth',
            redirect: '/auth/login',
            name: 'auth',
            component: () => import('@/views/AuthView.vue'),
            children: [
                {
                    path: 'login',
                    name: 'auth-login',
                    component: () => import('@/views/auth/LoginPage.vue')
                }, {
                    path: 'register',
                    name: 'auth-register',
                    component: () => import('@/views/auth/RegisterPage.vue')
                },
                {
                    path: 'reset',
                    name: 'auth-reset',
                    component: () => import('@/views/auth/ResetPage.vue')
                }
            ]
        },

        // 应用
        {
            path: '/',
            name: 'app',
            component: () => import('@/views/AppView.vue'),
            children: [
                {
                    path: '/',
                    name: 'home-page',
                    component: () => import('@/views/app/ArticleList.vue')
                }
            ]
        },

        // 协议
        {
            path: '/service-terms',
            name: 'service-terms',
            component: () => import('@/views/ServiceTermsView.vue')
        },

        // 隐私
        {
            path: '/privacy-policy',
            name: 'privacy-policy',
            component: () => import('@/views/PrivacyPolicyView.vue')
        },
    ]
})

router.beforeEach((to, from, next) => {
    const authorized = isAuthorized();

    // TODO 黑名单
    /*const whiteList = [
        'auth',
        'auth',
        'welcome',
        'service-terms',
        'privacy-policy'
    ];*/

    if (to.name && to.name.startsWith('auth') && authorized) {
        next('/');
        return;
    }

    /*if (!authorized && !whiteList.includes(to.name)) {
        next('/auth/login');
        return;
    }*/

    next();
});

export default router
