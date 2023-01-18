<template>
  <div>
      <el-input style="width: 100px" placeholder="搜索用户名" v-model="username"></el-input>
      <el-input style="width: 100px" placeholder="电话号码"  v-model="telephone"></el-input>
      <el-button icon="el-icon-search"  @click="load"></el-button>
      <el-button icon="el-icon-refresh" @click="reset"></el-button>
      <el-button icon="el-icon-plus"    @click="add"></el-button>
      <el-popconfirm
          style="margin-left:10px"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="確定刪除?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference"><i class="el-icon-delete"></i></el-button>
      </el-popconfirm>
      <el-upload
                 :action="'http://localhost:9999/user/import'"
                 :show-file-list="false"
                 accept="xlsx"
                 :on-success="handleExcelImportSuccess"
                 style="display: inline-block">
        <el-button type="primary" style="margin-left:10px">Excel 导入 </el-button>
      </el-upload>
      <el-button type="primary" @click="excelExport" style="margin-left:10px">Excel 导出</el-button>

    <el-table :data="tableData"  stripe   @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>
      <el-table-column prop="telephone" label="电话号码"></el-table-column>
      <el-table-column prop="createTime" label="注册/创建时间"></el-table-column>
      <el-table-column >
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit"  circle @click="edit(scope.row)" ></el-button>
          <el-popconfirm
              style="margin-left:10px"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="確定刪除？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" icon="el-icon-delete"  slot="reference" circle> </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10,20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total">
    </el-pagination>

    <el-dialog title="添加/修改用户信息" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="分配角色">
          <el-select v-model="form.role">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.roleKey"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
``
export default {
  name: "User",
  data() {
    return {
      serverIp: 'http://localhost:9999',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      telephone: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          telephone: this.telephone,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === 200) {
          this.$message.success("操作成功!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
       })
    },
    add() {
      this.dialogFormVisible = true
      this.form = {}
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }     
       })
    },
    reset() {
      this.username = "",
      this.telephone = "",
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
    excelExport() {
      window.open('http://localhost:9999/user/export')
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>

</style>
