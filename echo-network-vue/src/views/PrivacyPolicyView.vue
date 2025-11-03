<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.10
-->
<script>
import {defineComponent} from "vue";
import XTitleBar from "@/aethex/components/XTitleBar.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";

export default defineComponent({
  components: {XSpacer, XTitleBar}
})
</script>

<template>
  <XTitleBar title="回声网络隐私政策" sub-title="更新日期：2025 年 10 月 10 日"
             logo-image="/res/logo_echo_network.svg"/>

  <div class="content">
    <h2>一、我们收集的信息</h2>
    <XSpacer height="10px"/>
    <p><strong>1.1&nbsp;您直接提供的信息</strong></p>
    <ul style="padding-left: 50px">
      <li><strong>账号信息：</strong>注册时提供的用户名、电子邮箱、密码（采用强加密存储）、手机号码（可选）。</li>
      <li><strong>个人资料：</strong>您自愿填写的个人简介、头像、性别、所在地等信息。</li>
      <li><strong>内容信息：</strong>您发布的文章、评论、点赞、收藏、关注关系列表等所有内容。</li>
      <li><strong>账户设置信息：</strong>您选择的隐私设置、通知偏好等。</li>
    </ul>
    <p><strong>1.2&nbsp;我们自动收集的信息</strong></p>
    <ul style="padding-left: 50px">
      <li><strong>日志信息：</strong>包括 IP 地址、浏览器类型、操作系统、访问时间、访问页面、停留时长、操作记录等。</li>
      <li><strong>设备信息：</strong>设备型号、操作系统版本、设备唯一标识符（如 IMEI、MAC 地址）、网络状态等。</li>
      <li><strong>Cookie 及类似技术：</strong>用于识别您的身份、记录您的偏好，以优化您的体验（例如保持登录状态、记住您的偏好设置）。
      </li>
      <li><strong>位置信息：</strong>基于您设备的位置服务，我们可能会收集您所在地区的粗略位置信息，用于提供本地化服务（如区域内容推荐）。
      </li>
    </ul>
    <p><strong>1.3&nbsp;第三方信息</strong></p>
    <ul style="padding-left: 50px">
      <li>通过第三方登录（如微信、QQ、微博等）获取的公开信息（如昵称、头像等），仅限于您授权提供的信息。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>二、我们如何使用信息</h2>
    <XSpacer height="10px"/>
    <p><strong>2.1&nbsp;核心功能</strong></p>
    <ul style="padding-left: 50px">
      <li>为您提供内容发布、关注流推荐、评论互动、点赞收藏等核心服务。</li>
      <li>通过分析您的兴趣和互动行为，优化“关注流”内容推荐。</li>
    </ul>
    <p><strong>2.2&nbsp;个性化体验</strong></p>
    <ul style="padding-left: 50px">
      <li>基于您的关注关系、阅读历史、点赞和收藏行为，在首页“关注流”中为您推荐更相关的内容。</li>
      <li>为您提供个性化设置选项，如主题颜色、字体大小等。</li>
    </ul>
    <p><strong>2.3&nbsp;服务优化与安全</strong></p>
    <ul style="padding-left: 50px">
      <li>分析用户行为以改进产品功能、用户体验和平台性能。</li>
      <li>监测和防止安全漏洞、欺诈行为和网络攻击。</li>
      <li>用于统计分析，了解用户使用习惯，持续优化平台服务。</li>
    </ul>
    <p><strong>2.4&nbsp;沟通与通知</strong></p>
    <ul style="padding-left: 50px">
      <li>向您发送与服务相关的通知（如系统维护、政策变更、安全提醒）。</li>
      <li>在获得您同意后，可能发送产品更新或相关活动信息。</li>
      <li>回复您通过平台提交的咨询和投诉。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>三、我们如何共享和披露信息</h2>
    <XSpacer height="10px"/>
    <p><strong>3.1&nbsp;共享与披露原则</strong></p>
    <ul style="padding-left: 50px">
      <li>我们不会出售您的个人数据。</li>
      <li>仅在以下情况下共享信息：</li>
      <ul style="padding-left: 30px">
        <li><strong>征得您的同意：</strong>在向您明确告知并获得您同意后。</li>
        <li><strong>依法披露：</strong>根据法律法规要求或响应司法程序时。</li>
        <li><strong>服务提供商：</strong>与为我们提供基础设施、数据分析等服务的合作方共享（例如云服务器提供商、数据分析服务提供商），但我们会要求其承担保密义务。
        </li>
        <li><strong>业务转让：</strong>如发生合并、收购、破产清算等情形，我们将告知您相关信息，并确保您的个人信息得到妥善处理。
        </li>
        <li><strong>公开信息：</strong>您的用户名、头像、个人简介及您公开发布的文章和评论，会对平台内其他用户可见。</li>
      </ul>
    </ul>
    <p><strong>3.2&nbsp;数据跨境传输</strong></p>
    <ul style="padding-left: 50px">
      <li>我们将尽可能在中国境内处理您的个人信息。</li>
      <li>如确需向境外传输，将根据国家相关法律法规要求，采取必要措施保护您的个人信息安全。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>四、您的权利</h2>
    <XSpacer height="10px"/>
    <p><strong>4.1&nbsp;访问与更正</strong></p>
    <ul style="padding-left: 50px">
      <li>您可以随时登录账号访问和编辑您的个人资料信息。</li>
      <li>如发现个人信息有误，您有权要求我们更正。</li>
    </ul>
    <p><strong>4.2&nbsp;删除权</strong></p>
    <ul style="padding-left: 50px">
      <li>您可以删除您发布的特定内容。</li>
      <li>您可以联系我们来删除您的账号，账号注销后，您的个人数据将被删除或匿名化处理，但根据法律法规需保留的部分信息除外。
      </li>
    </ul>
    <p><strong>4.3&nbsp;限制处理权</strong></p>
    <ul style="padding-left: 50px">
      <li>如您认为我们处理您的个人信息有误，或处理目的已实现，您有权要求我们限制处理您的个人信息。</li>
    </ul>
    <p><strong>4.4&nbsp;拒绝自动化决策权</strong></p>
    <ul style="padding-left: 50px">
      <li>您有权拒绝我们基于自动化决策（如个性化推荐）而做出的仅通过算法作出的决定。</li>
    </ul>
    <p><strong>4.5&nbsp;行使权利的渠道</strong></p>
    <ul style="padding-left: 50px">
      <li>您可以通过平台内的"设置-隐私与安全"功能提交相关请求。</li>
      <li>您也可以通过客服邮箱 <a href="mailto:code_intellix@163.com">code_intellix@163.com</a> 联系我们。</li>
      <li>我们将在收到请求后 15 个工作日内予以处理。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>五、数据存储与安全</h2>
    <XSpacer height="10px"/>
    <p><strong>5.1&nbsp;数据存储</strong></p>
    <ul style="padding-left: 50px">
      <li>您的数据存储在我们的服务器或合作方的服务器上，服务器位于中国境内。</li>
      <li>我们将根据数据类型和用途，确定合理的保存期限，但不会超过实现目的所必需的期限。</li>
    </ul>
    <p><strong>5.2&nbsp;数据安全措施</strong></p>
    <ul style="padding-left: 50px">
      <li>采用SSL/TLS加密技术保护数据传输安全。</li>
      <li>采用强加密技术存储敏感信息（如密码）。</li>
      <li>实施访问控制，仅限授权人员访问用户数据。</li>
      <li>定期进行安全评估和漏洞扫描。</li>
      <li>建立数据备份和恢复机制，确保数据安全。</li>
    </ul>
    <p><strong>5.3&nbsp;安全事件响应</strong></p>
    <ul style="padding-left: 50px">
      <li>如发生数据安全事件，我们将按照相关法律法规要求，及时通知您并采取补救措施。</li>
      <li>如事件可能对您的合法权益造成重大影响，我们将立即向监管部门报告。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>六、政策的变更</h2>
    <XSpacer height="10px"/>
    <p><strong>6.1&nbsp;更新机制</strong></p>
    <ul style="padding-left: 50px">
      <li>我们可能适时更新本政策。任何重大变更都会通过平台公告、站内信或邮件等方式通知您。</li>
      <li>重大变更包括但不限于：收集信息范围、使用方式、共享对象等发生重大变化。</li>
    </ul>
    <p><strong>6.2&nbsp;通知方式</strong></p>
    <ul style="padding-left: 50px">
      <li>重大变更将在实施前至少 30 天在平台首页显著位置公告。</li>
      <li>您可以通过平台内"通知中心"查看历史政策变更记录。</li>
    </ul>
    <p><strong>6.3&nbsp;您的选择</strong></p>
    <ul style="padding-left: 50px">
      <li>如您不同意更新后的隐私政策，您有权停止使用本平台服务。</li>
      <li>您继续使用本平台服务，即表示您已接受更新后的隐私政策。</li>
    </ul>
    <XSpacer height="20px"/>

    <h2>七、联系我们</h2>
    <XSpacer height="10px"/>
    <p><strong>7.1</strong>&nbsp;如您对本隐私政策有任何疑问，或希望行使您的权利，请通过以下方式联系我们：</p>
    <ul style="padding-left: 50px">
      <li><strong>电子邮箱：</strong><a href="mailto:code_intellix@163.com">code_intellix@163.com</a></li>
      <li><strong>通讯地址：</strong>中国广东省广州市番禺区回声网络总部</li>
      <li><strong>联系人：</strong>隐私保护专员</li>
    </ul>
  </div>
</template>

<style scoped>
.content {
  padding: 105px 50px 50px 50px;
  background: linear-gradient(135deg, #150d44 0%, black 40%, #022933 100%);
}

p {
  font-size: 18px;
}

a {
  color: var(--theme-color-lighten);
}
</style>
