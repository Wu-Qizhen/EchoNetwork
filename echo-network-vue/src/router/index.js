/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.9
 */
import {createRouter, createWebHistory} from 'vue-router'
import {isAuthorized} from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    scrollBehavior() {
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
                    name: 'app-home',
                    component: () => import('@/views/app/HomePage.vue')
                },
                {
                    path: '/circle',
                    name: 'app-circle',
                    props: true,
                    component: () => import('@/views/app/CirclePage.vue')
                },
            ]
        },

        // 用户
        {
            path: '/user/:id',
            name: 'user',
            props: true,
            component: () => import('@/views/UserView.vue'),
            children: [
                {
                    path: '',
                    name: 'user-articles',
                    component: () => import('@/views/user/ArticlePage.vue')
                },
                {
                    path: 'followers',
                    name: 'user-followers',
                    component: () => import('@/views/user/FollowerPage.vue')
                },
                {
                    path: 'following',
                    name: 'user-following',
                    component: () => import('@/views/user/FollowingPage.vue')
                },
                {
                    path: 'stars',
                    name: 'user-stars',
                    component: () => import('@/views/user/StarPage.vue')
                },
                {
                    path: 'circles',
                    name: 'user-circles',
                    component: () => import('@/views/user/CirclePage.vue')
                },
            ]
        },

        // 设置
        {
            path: '/settings',
            name: 'settings',
            component: () => import('@/views/SettingsView.vue')
        },

        // 文章
        {
            path: '/article/:id',
            name: 'article',
            props: true,
            component: () => import('@/views/ArticleView.vue')
        },

        // 圈子
        {
            path: '/circle/:id',
            name: 'circle',
            props: true,
            component: () => import('@/views/CircleView.vue')
        },

        // 编辑
        {
            path: '/editor/:id?',
            name: 'editor',
            props: true,
            component: () => import('@/views/EditView.vue')
        },

        // 搜索
        {
            path: '/search',
            name: 'search',
            component: () => import('@/views/SearchView.vue')
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

    /*const whiteList = [
        'auth',
        'welcome',
        'service-terms',
        'privacy-policy'
    ];*/

    // TODO 游客不允许进入编辑页面
    const blackList = [
        'editor',
    ];

    if (to.name && to.name.startsWith('auth') && authorized) {
        next('/');
        return;
    }

    if (!authorized && blackList.includes(to.name)) {
        next('/auth/login');
        return;
    }

    next();
});

export default router
