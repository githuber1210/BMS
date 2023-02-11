<template>
  <div>
    <el-button icon="el-icon-plus" @click="add">添加角色</el-button>
    <el-table :data="tableData" stripe>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="roleKey" label="权限字符"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="createBy" label="创建人" width="100"></el-table-column>
      <el-table-column prop="updateTime" label="最近修改时间"></el-table-column>
      <el-table-column prop="updateBy" label="最近修改人" width="100"></el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button icon="el-icon-menu" @click="selectMenu(scope.row)" circle></el-button>
          <el-button icon="el-icon-edit" @click="edit(scope.row)" type="primary" circle></el-button>
          <el-popconfirm
              style="margin-left: 10px"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="確定刪除？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" icon="el-icon-delete" circle></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="角色信息表" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="权限字符">
          <el-input v-model="form.roleKey"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="权限分配" :visible.sync="menuDialogVis">
      <el-tree
          :check-strictly="true"
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          default-expand-all
          :default-checked-keys="checks"
          ref="tree"
      >
         <span slot-scope="{ data }"> <i :class="data.icon"></i> {{ data.name }} </span>
      </el-tree>
      <div slot="footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenus">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      form: {},
      name: "",
      dialogFormVisible: false,
      menuDialogVis: false,
      menuData: [],
      props: {},
      checks: [],
      roleId: 0,
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/role").then(res => {
        this.tableData = res.data
      })
      this.request.get("/menu/ids").then(res => {
        this.ids = res.data
      })
    },
    add() {
      this.dialogFormVisible = true
      this.form = {}
    },
    selectMenu(role) {
      this.request.get("/menu").then(res => {
        this.menuData = res.data
      })
      this.roleId = role.id
      this.request.get("/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.menuDialogVis = true
      })
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    save() {
      if (this.form.id != null) {
        this.request.put("/role", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/role", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("添加成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    saveRoleMenus() {
      this.request.post("/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === 200) {
          this.$message.success("绑定成功!")
          this.menuDialogVis = false
        }
      })
    },
  }
}
</script>


<style>

</style>
