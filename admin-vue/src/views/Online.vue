<template>
  <div>

    <el-table :data="tableData"  stripe >
      <el-table-column prop="username" label="用户名称"></el-table-column>
      <el-table-column prop="loginTime" label="登陆时间"></el-table-column>
      <el-table-column prop="ipaddr" label="登录IP"></el-table-column>
      <el-table-column prop="browser" label="浏览器"></el-table-column>
      <el-table-column prop="os" label="操作系统"></el-table-column>
      <el-table-column prop="loginLocation" label="登录地点"></el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button @click="breakLine(scope.row)" style="color: red" disabled="">下线</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

export default {
  data() {
    return {
      tableData: [],
      form: {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/online").then(res => {
        if (res.code == 200){
          this.tableData = res.data
        }
      })
    },
    breakLine(row){
      this.request.delete("/break/" + row.token ).then(res =>{
        if (res.code == 200){
          this.$message.success("下线成功！")
          this.load()
        }
      })
    }
  }
}
</script>

