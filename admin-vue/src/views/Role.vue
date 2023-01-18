<template>
  <div>
    <el-button icon="el-icon-plus"    @click="add">添加角色</el-button>
    <el-table :data="tableData"  stripe>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="roleKey" label="权限字符"></el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button icon="el-icon-menu" @click="selectMenu(scope.row)"> </el-button>
          <el-button icon="el-icon-edit" @click="edit(scope.row)"> </el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="確定刪除？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" icon="el-icon-delete"></el-button>
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
          check-strictly="true"
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          default-expand-all
          :default-checked-keys="checks"
      >
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
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
      name: "",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      menuData: [],
      props: { label: 'name' },
      checks: [],
      roleId: 0,
      roleKey: '',
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
      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })
    },
    save() {
      this.request.post("/role", this.form).then(res => {
        if (res.code === 200) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === 200) {
          this.$message.success("菜单权限绑定成功!")
          this.menuDialogVis = false
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
      this.request.delete("/role/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    async selectMenu(role) {
      this.roleId = role.id
      this.roleKey = role.roleKey
      this.request.get("/menu").then(res => {
        this.menuData = res.data
      })
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id,false)
            })
          }
        })
        this.menuDialogVis = true
      })
    },
  }
}
</script>


<style>

</style>
