/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.26
 */
/* TODO 将请求代码移至这里 */
import {defaultFailure, del, get, post} from "@/net/index.js";

function getArticles(params, success, failure = defaultFailure) {
    const queryString = new URLSearchParams(params).toString();
    const url = `/api/articles?${queryString}`;
    get(url, success, failure)
}

function getUser(pathString, success, failure = defaultFailure) {
    const url = `/api/users/${pathString}`;
    get(url, success, failure)
}

function getUsers(pathString, success, failure = defaultFailure) {
    const url = `/api/users/${pathString}`;
    get(url, success, failure)
}

function followUser(userId, success, failure = defaultFailure) {
    const url = `/api/users/${userId}/follow`;
    post(url, {}, success, failure)
}

function unfollowUser(userId, success, failure = defaultFailure) {
    const url = `/api/users/${userId}/follow`;
    del(url, {}, success, failure)
}

export {getArticles, getUser, getUsers, followUser, unfollowUser}
