<template>
  <div class="priceItem">
    <div class="agentDetail">
      <span style="font-size: 1rem;line-height: 1.25rem;font-weight: 400;">{{supplier.name}}</span>
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
      {{supplier.price}}￥
      <el-button icon="el-icon-s-promotion" round @click="jmpToResult()">选择</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Supplier",
  props: ["supplier", "flyPlan"],
  components: {},
  data() {
    return {
      value: 3.7
    };
  },
  created() {},
  methods: {
    jmpToResult() {
      this.$router.push({
        name: "SelectResult",
        params: {
          supplier: this.supplier,
          flyPlan: this.flyPlan
        }
      }); // router end
    }
  }
};
</script>

<style>
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
.priceDetail * {
  margin: auto 5px;
}
.priceDetail .el-button:hover {
  color: #64bb70;
  border-color: #18a18f;
  outline: 0;
}
</style>