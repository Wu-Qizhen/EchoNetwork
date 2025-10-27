/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.27
 */
/**
 * 将 HTML 转换为纯文本并限制长度
 * @param {string} html - HTML 字符串
 * @param {number} maxLength - 最大长度
 * @returns {string} 纯文本
 */
export function htmlToText(html, maxLength = 150) {
    if (!html) return ''

    // 创建临时 div 元素
    const tempDiv = document.createElement('div')
    tempDiv.innerHTML = html

    // 获取纯文本内容
    let text = tempDiv.textContent || tempDiv.innerText || ''

    // 移除多余的空格和换行
    text = text.replace(/\s+/g, ' ').trim()

    // 限制长度
    if (text.length > maxLength) {
        text = text.substring(0, maxLength) + '...'
    }

    return text
}

/**
 * 提取文章摘要（更智能的版本）
 * @param {string} html - HTML 字符串
 * @param {number} maxLength - 最大长度
 * @returns {string} 文章摘要
 */
export function extractSummary(html, maxLength = 150) {
    if (!html) return ''

    const tempDiv = document.createElement('div')
    tempDiv.innerHTML = html

    // 获取纯文本
    let text = tempDiv.textContent || tempDiv.innerText || ''

    // 清理文本：移除多余空格、换行
    text = text
        .replace(/\s+/g, ' ')
        .replace(/&nbsp;/g, ' ')
        .trim()

    // 如果文本仍然包含 HTML 标签，再次清理
    text = text.replace(/<[^>]*>/g, '')

    // 限制长度
    if (text.length > maxLength) {
        text = text.substring(0, maxLength).trim() + '...'
    }

    return text
}
