/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.9
 */
/*import {ElMessage} from "element-plus";*/
import axios from "axios";

const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
    console.warn(`请求地址：${url}，状态码：${code}，错误信息：${message || '未知错误'}`)
    ElMessage.warning(message || '未知错误')
}

/*const defaultError = (err) => {
    console.error(err)
    ElMessage.error("发生一些错误，请联系客服")
}*/

function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function storeAccessToken(token, remember, expire, user) {
    const authObj = {
        token: token,
        expire: expire,
        user: user
    }
    const str = JSON.stringify(authObj);
    if (remember) {
        localStorage.setItem(authItemName, str)
    } else {
        sessionStorage.setItem(authItemName, str)
    }
}

function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if (!str) return null
    const authObj = JSON.parse(str);
    if (authObj.expire < Date.now()) {
        deleteAccessToken()
        ElMessage.warning("登录已过期，请重新登录")
        return null
    }
    return authObj
}

function accessHeader() {
    const take = takeAccessToken()
    return take ? {Authorization: `Bearer ${take.token}`} : {}
}

function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}

function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader(), success, failure)
}

function del(url, success, failure = defaultFailure) {
    internalDelete(url, accessHeader(), success, failure)
}

function put(url, data, success, failure = defaultFailure) {
    internalPut(url, data, accessHeader(), success, failure)
}

function internalPut(url, data, header, success, failure) {
    axios.put(url, data, {headers: header})
        .then(({data}) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }).catch(err => {
        const errorMessage = err.response?.data?.message || err.message || "请求失败";
        failure(errorMessage, err.response?.status, url)
    })
}

function internalDelete(url, header, success, failure) {
    axios.delete(url, {headers: header})
        .then(({data}) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }).catch(err => {
        const errorMessage = err.response?.data?.message || err.message || "请求失败";
        failure(errorMessage, err.response?.status, url)
    })
}

function internalPost(url, data, header, success, failure) {
    axios.post(url, data, {headers: header})
        .then(({data}) => { // 等价于 then((response) => { const data = response.data; ... })
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }).catch(err => {
        const errorMessage = err.response?.data?.message || err.message || "请求失败";
        failure(errorMessage, err.response?.status, url)
    })
}

function internalGet(url, header, success, failure, /*error = defaultError*/) {
    axios.get(url, {headers: header})
        .then(({data}) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }).catch(err => {
        const errorMessage = err.response?.data?.message || err.message || "请求失败";
        failure(errorMessage, err.response?.status, url)
    })
}

function login(username, password, remember, success, failure = defaultFailure) {
    internalPost(
        "/api/users/login",
        {username, password},
        {
            'Content-Type': 'application/json'
        },
        (data) => {
            const expireTimestamp = new Date(data.expire).getTime();
            storeAccessToken(data.token, remember, expireTimestamp, data.user)
            // storeAccessToken(data.token, remember, data.expire, data.user)
            ElMessage.success(`登录成功，欢迎 ${data.user.nickname || data.user.username}`)
            success(data)
        }, failure)
}

function logout(success) {
    // 先调用后端登出接口
    internalGet("/api/users/logout", accessHeader(),
        () => {
            // 无论后端是否成功，都清理前端状态
            deleteAccessToken()
            ElMessage.success("登出成功")
            if (success) success()
        },
        (message, code, url) => {
            // 即使后端登出失败，也清理前端状态
            console.warn(`请求地址：${url}，状态码：${code}，错误信息：${message || '未知错误'}`)
            deleteAccessToken()
            ElMessage.success("已退出登录")
            if (success) success()
        }
    )
}

function isAuthorized() {
    return takeAccessToken() !== null
}

function getUserInfo() {
    const authObj = takeAccessToken()
    return authObj ? authObj.user : null
}

export {login, logout, getUserInfo, isAuthorized, get, post, del, put, defaultFailure}
