<template>
    <div class="divResultSortButton">
        <p class="title">{{title}}</p>
        <div class="price">{{price}}</div>
        <p>{{time}}</p>
    </div>
</template>

<script>
export default {
    name:"ResultSortButton",
    props:['title','price','time']
}
</script>

<style scoped>

.divResultSortButton{
    display: flex;
    min-height: 79px;
    min-width: 183px;
    flex-direction: column;
    justify-content: space-between;
    text-align: left;
    padding:0px ;
}
.title{
    font-size: .875rem;
    line-height: 1.125rem;
    font-weight: 600;
    color: #444560;
}
.price{
    font-weight: 700;
    font-size: 1.25rem;
    color: #e36387;
}
</style>