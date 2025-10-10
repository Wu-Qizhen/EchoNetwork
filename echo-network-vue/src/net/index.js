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

const defaultError = (err) => {
    console.error(err)
    ElMessage.error("发生一些错误，请联系客服")
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
    return authObj.token
}

function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function storeAccessToken(token, remember, expire) {
    const authObj = {
        token: token,
        expire: expire
    }
    const str = JSON.stringify(authObj);
    if (remember) {
        localStorage.setItem(authItemName, str)
    } else {
        sessionStorage.setItem(authItemName, str)
    }
}

function accessHeader() {
    const take = takeAccessToken()
    return take ? {Authorization: `Bearer ${take}`} : {}
}

function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}

function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader(), success, failure)
}

function internalPost(url, data, header, success, failure, error = defaultError) {
    axios.post(url, data, {headers: header})
        .then(({data}) => { // 等价于 then((response) => { const data = response.data; ... })
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }).catch(err => error(err))
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
        "/api/auth/login",
        {username, password},
        {
            'Content-Type': 'application/x-www-form-urlencoded' // 将表单字段转换为 key=value&key2=value2 的字符串形式进行传输
        },
        (data) => {
            storeAccessToken(data.token, remember, data.expire)
            ElMessage.success(`登录成功，欢迎 ${data.username}`)
            success(data)
        }, failure)
}

function logout(success, failure = defaultFailure) {
    get("/api/auth/logout", () => {
        deleteAccessToken()
        ElMessage.success("登出成功")
        success()
    }, failure)
}

function isAuthorized() {
    return takeAccessToken() !== null
}

export {login, logout, isAuthorized, get, post}
