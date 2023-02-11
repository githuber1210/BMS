<template>
  <div>
    <el-input style="width: 100px" placeholder="搜索操作人" v-model="username"></el-input>
    <el-button icon="el-icon-search"  @click="load"></el-button>
    <el-button icon="el-icon-refresh" @click="reset"></el-button>

    <el-table :data="tableData"  stripe >
      <el-table-column prop="content" label="操作名称"></el-table-column>
      <el-table-column prop="time" label="操作时间"></el-table-column>
      <el-table-column prop="user" label="操作人"></el-table-column>
      <el-table-column prop="ip" label="操作人ip"></el-table-column>
    </el-table>

    <div style="padding: 30px 0;text-align: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      form: {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/log/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.username = "",
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>


<style>

</style>
