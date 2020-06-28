<template>
  <el-collapse-item :name="itemName" class="flightResultItem">
    <!-- 头部 -->
    <template slot="title">
      <div class="sectionContainer">
        <FlightSection v-for="(section,index) in flyPlan.sections" :key="index" :section="section"/>
      </div>
      <!-- 最低价格 -->
      <div class="cost" style="color: lightcoral;min-width:80px">{{flyPlan.lowestCashCost}}￥</div>
    </template>

    <!-- 所有价格 -->
    <Supplier v-for="(supplier,index) in flyPlan.supplier" :key="index" 
    :supplier="supplier"
    :flyPlan="flyPlan"/>

  </el-collapse-item>
</template>

<script>
import FlightSection from "@/components/FlightSection.vue";
import Supplier from "@/components/Supplier.vue";
export default {
  name: "FlightResultItem",
  props: ["itemName", "flyPlan"],
  components:{
    FlightSection,Supplier
  },
  data() {
    return {
    };
  },
  methods:{
  }
};
</script>

<style>
.flightResultItem .el-collapse-item__content {
  padding: 0px;
}
.flightResultItem .el-collapse-item__header {
  height: fit-content;
  border: 0px;
}
.flightResultItem .el-collapse-item__header .el-collapse-item__arrow {
  display: none;
}
.sectionContainer {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.cost {
  font-size: 25px;
  font-weight: 700;
  padding: 15px;
}
</style>