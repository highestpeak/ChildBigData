<template>
  <div class="section">
    <!-- 航空公司图标 -->
    <!-- <el-avatar icon="el-icon-user-solid"></el-avatar> -->
    <div style="line-height:20px;font-weight:700;width:125px;" class="airline">
      {{section.airline}}
      <br />
      {{section.flightNumber}}
    </div>
    <!-- 具体航班信息 -->
    <div class="legInfo">
      <!-- 出发地 -->
      <div class="endpoint" style="text-align: right;">
        <span class="time">{{section.startTime}}</span>
        <span class="place">{{section.startPlace}}</span>
      </div>
      <!-- 经停和类型 -->
      <div class="between">
        <div>{{section.during.str}}</div>
        <ul class="stopLine">
          <i class="fa fa-telegram fa-1x" aria-hidden="true"></i>
        </ul>
        <div>直飞</div>
      </div>
      <!-- 到达地 -->
      <div class="endpoint" style="text-align: left;">
        <span class="time">{{section.endTime}}</span>
        <span class="place">{{section.endPlace}}</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "FlightSection",
  props: ["section"],
  components: {},
  data() {
    return {};
  },
  created() {},
  methods: {}
};
</script>

<style scoped>
.section {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  align-items: center;
  padding: 5px 10px;
  margin: 5px 0px;
}
/* airline */
.airline {
  font-family: arial;
  font-size: 15px;
  font-weight: 500;
  font-style: oblique 25deg;
  color: #10375c;
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
</style>