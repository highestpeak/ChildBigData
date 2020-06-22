<template>
  <el-collapse-item :name="itemName">
    <!-- 头部 -->
    <template slot="title">
      <div class="titleHeader">
        <!-- 航空公司图标 -->
        <el-avatar icon="el-icon-user-solid"></el-avatar>
        <!-- 具体航班信息 -->
        <div class="legInfo">
          <!-- 出发地 -->
          <div class="endpoint" style="text-align: right;">
            <span class="time">17:45</span>
            <span class="place">CTU</span>
          </div>
          <!-- 经停和类型 -->
          <div class="between">
            <div>2小时35分</div>
            <ul class="stopLine">
              <i class="fa fa-telegram fa-1x" aria-hidden="true"></i>
            </ul>
            <div>直飞</div>
          </div>
          <!-- 到达地 -->
          <div class="endpoint" style="text-align: left;">
            <span class="time">20:20</span>
            <span class="place">SHA</span>
          </div>
        </div>
        <!-- 最低价格 -->
        <div class="cost" style="color: lightcoral;">325￥</div>
      </div>
    </template>

    <!-- 所有价格 -->
    <div v-for="index in 5" :key="index" class="priceItem">
      <div class="agentDetail">
        <span style="font-size: 1rem;line-height: 1.25rem;font-weight: 400;">携程旅行</span>
        <div style="display: flex;margin: .375rem 0;">
          <el-rate
            v-model="value"
            disabled
            show-score
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            text-color="#ff9900"
            score-template="{value}"
          >
            <!-- score-template="{value}" -->
          </el-rate>
        </div>
      </div>
      <div class="priceDetail" style="color: #e36387;">
          370￥
          <el-button icon="el-icon-s-promotion"  round>选择</el-button>
        </div>
    </div>
  </el-collapse-item>
</template>

<script>
export default {
  name: "FlightResultItem",
  props: ["itemName"],
  data() {
    return {
      value: 3.7
    };
  }
};
</script>

<style>
.el-collapse-item__content {
  padding: 0px;
}
.el-collapse-item__header {
  height: fit-content;
  border: 0px;
}
.el-collapse-item__header .el-collapse-item__arrow {
  display: none;
}

.titleHeader {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  align-items: center;
  padding: 5px 10px;
  margin: 20px 0px;
}

.titleHeader .cost {
  font-size: 25px;
  font-weight: 700;
}

/* 中间的时间信息 */
.legInfo {
  display: flex;
  flex-flow: row nowrap;
  flex: 1 1 78%;
  text-align: center;
  line-height: 1.5rem;

  border-right: dashed #ffcac2;
  margin-right: 10px;
}

.legInfo .endpoint {
  display: flex;
  max-width: 32%;
  padding-right: 0.375rem;
  flex-direction: column;
  flex: 0 1 32%;
  align-self: center;
}
.legInfo .between {
  max-width: 36%;
  padding: 0 0.375rem;
  text-align: center;
  flex: 0 1 36%;
}
.endpoint .time {
  font-size: 1.5rem;
  line-height: 1.9375rem;
  font-weight: 400;
}
.endpoint .place {
  color: #68697f;
}

.legInfo .stopLine {
  position: relative;
  display: block;
  width: 90%;
  height: 0.125rem;
  margin: 0.375rem auto;
  padding: 0;
  border-radius: 0.375rem;
  background-color: #68697f;
  line-height: 0;
  text-align: center;
}
.stopLine i {
  position: absolute;
  top: 50%;
  right: -0.375rem;
  display: block;
  width: 1rem;
  height: 1rem;
  margin-top: -0.5rem;
  padding-left: 0.25rem;
  background-color: #fff;
}

/* 比价的样式 */
.priceItem {
  padding: 0.75rem 0;
  box-shadow: inset 0 -1px 0 0 #dddde5;
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;

  display: flex;
  padding: 5px 10px;
  margin: 20px 10px;
}
.priceItem .agentDetail {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  text-align: left;
}
.priceItem .priceDetail {
  font-weight: 700;
  font-size: 20px;
}
.priceDetail *{
    margin: auto 5px;
}
.priceDetail .el-button:hover {
    color: #64bb70;
    border-color: #18a18f;
    outline: 0;
}
</style>