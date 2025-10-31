/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.26
 */
/* TODO 将请求代码移至这里 */
import {defaultFailure, del, get, post, put} from "@/net/index.js";

function getArticles(params, success, failure = defaultFailure) {
    const queryString = new URLSearchParams(params).toString();
    const url = `/api/articles?${queryString}`;
    get(url, success, failure)
}

function publishArticle(articleData, success, failure = defaultFailure) {
    post("/api/articles", articleData, success, failure)
}

function updateArticle(articleId, data, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}`;
    put(url, data, success, failure)
}

function deleteArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}`;
    del(url, success, failure)
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

function getArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}`;
    get(url, success, failure)
}

function likeArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}/like`;
    post(url, {}, success, failure)
}

function unlikeArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}/like`;
    del(url, {}, success, failure)
}

function starArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}/star`;
    post(url, {}, success, failure)
}

function unstarArticle(articleId, success, failure = defaultFailure) {
    const url = `/api/articles/${articleId}/star`;
    del(url, {}, success, failure)
}

function getCircles(params, success, failure = defaultFailure) {
    const queryString = new URLSearchParams(params).toString();
    const url = `/api/circles?${queryString}`;
    get(url, success, failure)
}

function joinCircle(circleId, success, failure = defaultFailure) {
    const url = `/api/circles/${circleId}/join`;
    post(url, {}, success, failure)
}

function exitCircle(circleId, success, failure = defaultFailure) {
    const url = `/api/circles/${circleId}/exit`;
    del(url, {}, success, failure)
}

function getLikedArticles(success, failure = defaultFailure) {
    const url = `/api/articles/liked`;
    get(url, success, failure)
}

function getStarredArticles(success, failure = defaultFailure) {
    const url = `/api/articles/starred`;
    get(url, success, failure)
}

export {
    getArticles,
    updateArticle,
    publishArticle,
    deleteArticle,
    getUser,
    getUsers,
    followUser,
    unfollowUser,
    getArticle,
    likeArticle,
    unlikeArticle,
    starArticle,
    unstarArticle,
    getCircles,
    joinCircle,
    exitCircle,
    getLikedArticles,
    getStarredArticles
}

export const getCircle = async (circleId, successCallback, failure = defaultFailure) => {
    const url = `/api/circles/${circleId}`;
    get(url, successCallback, failure)
}
